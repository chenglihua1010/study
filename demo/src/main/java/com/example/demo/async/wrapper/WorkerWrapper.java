package com.example.demo.async.wrapper;

import cn.hutool.core.date.SystemClock;
import com.example.demo.async.callback.ICallback;
import com.example.demo.async.callback.IWorker;
import com.example.demo.async.exception.SkipException;
import com.example.demo.async.worker.DependWrapper;
import com.example.demo.async.worker.ResultState;
import com.example.demo.async.worker.WorkResult;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerWrapper<T, V> {

    private String id;
    private IWorker<T, V> worker;
    private T param;


    private ICallback<T, V> callback;


    private List<WorkerWrapper<?, ?>> nextWrappers;

    private List<DependWrapper> dependWrappers;

    private AtomicInteger state = new AtomicInteger(0);


    private Map<String, WorkerWrapper> forParamUseWrappers;

    private volatile WorkResult<V> workResult = WorkResult.defaultResult();

    private volatile boolean needCheckNextWrapperResult = true;


    private static final int FINISH = 1;

    private static final int ERROR = 2;

    private static final int WORKING = 3;

    private static final int INIT = 0;

    public WorkerWrapper(String id, IWorker<T, V> worker, T param, ICallback<T, V> callback) {

        this.id = id;
        this.worker = worker;
        this.param = param;
        this.callback = callback;
    }

    private void work(ExecutorService executorService, WorkerWrapper fromWrapper, long remainTime, Map<String,
            WorkerWrapper> forParamUseWrappers) {
        this.forParamUseWrappers = forParamUseWrappers;
        forParamUseWrappers.put(id, this);
        long now = SystemClock.now();
        //超时-快速失败
        if (remainTime <= 0) {
            fastFail(INIT, null);
            beginNext(executorService, now, remainTime);
            return;
        }

        //校验当前work状态
        if (getState() == FINISH || getState() == ERROR) {
            beginNext(executorService, now, remainTime);
            return;
        }

        //校验nextWrapper状态
        if (needCheckNextWrapperResult) {
            if (!checkNextWrapperResult()) {
                fastFail(INIT, new SkipException());
                beginNext(executorService, now, remainTime);
                return;
            }
        }

        //没有依赖任务时，完成当前任务
        if (dependWrappers == null || dependWrappers.size() == 0) {
            fire();
            beginNext(executorService, now, remainTime);
            return;
        }

        //完成依赖任务
        if (dependWrappers.size() == 1) {
            doDependOneJob(fromWrapper);
            beginNext(executorService, now, remainTime);
        } else {
            doDependsJobs(executorService, dependWrappers, fromWrapper, now, remainTime);
        }


    }

    public void work(ExecutorService executorService, long remainTime, Map<String, WorkerWrapper> forParamUseWrappers) {
        work(executorService, null, remainTime, forParamUseWrappers);

    }

    private synchronized void doDependsJobs(ExecutorService executorService, List<DependWrapper> dependWrappers,
                                            WorkerWrapper fromWrapper,
                                            long now,
                                            long remainTime) {
        if (getState() != INIT) {
            return;
        }
        boolean nowDependIsMust = false;

        Set<DependWrapper> mustWrapper = new HashSet<>();
        for (DependWrapper dependWrapper : dependWrappers) {
            if (dependWrapper.isMust()) {
                mustWrapper.add(dependWrapper);
            }
            if (dependWrapper.getDependWrapper().equals(fromWrapper)) {
                nowDependIsMust = dependWrapper.isMust();

            }
        }

        if (mustWrapper.size() == 0) {
            if (ResultState.TIMEOUT == fromWrapper.getWorkResult().getResultState()) {
                fastFail(INIT, null);
            } else {
                fire();
            }
            beginNext(executorService, now, remainTime);
            return;
        }

        if (!nowDependIsMust) {
            return;
        }

        boolean exitNotFinish = false;
        boolean hasError = false;
        for (DependWrapper dependWrapper : mustWrapper) {
            WorkerWrapper workerWrapper = dependWrapper.getDependWrapper();
            WorkResult workResult = workerWrapper.getWorkResult();

            if (workerWrapper.getState() == INIT || workerWrapper.getState() == WORKING) {
                exitNotFinish = true;
                break;
            }

            if (ResultState.TIMEOUT == workResult.getResultState()) {
                workResult = defaultResult();
                hasError = true;
                break;

            }

            if (ResultState.EXCEPTION == workResult.getResultState()) {
                workResult = defaultExResult(workerWrapper.getWorkResult().getEx());
                hasError = true;
                break;
            }


        }
        if (hasError) {
            fastFail(INIT, null);
            beginNext(executorService, now, remainTime);
        }

        if (!exitNotFinish) {
            fire();
            beginNext(executorService, now, remainTime);
            return;
        }


    }

    private void doDependOneJob(WorkerWrapper dependWrapper) {
        if (ResultState.TIMEOUT == dependWrapper.getWorkResult().getResultState()) {
            workResult = defaultResult();
            fastFail(INIT, null);
        } else if (ResultState.EXCEPTION == dependWrapper.getWorkResult().getResultState()) {
            workResult = defaultExResult(dependWrapper.getWorkResult().getEx());
            fastFail(INIT, null);
        } else {
            fire();
        }


    }

    /**
     * 判断自己的下游链路上，是否存在已经出结果或开始执行的任务
     * 如果没有则返回true,有返回false
     *
     * @return
     */
    private boolean checkNextWrapperResult() {
        if (nextWrappers == null || nextWrappers.size() == 0) {
            return getState() == INIT;
        }

        WorkerWrapper nextWrapper = nextWrappers.get(0);
        boolean state = nextWrapper.getState() == INIT;
        return state && nextWrapper.checkNextWrapperResult();

    }

    private boolean fastFail(int expect, Exception e) {
        if (!compareAndSetState(expect, ERROR)) {
            return false;
        }

        if (checkIsNullResult()) {
            if (e == null) {
                workResult = defaultResult();
            } else {
                workResult = defaultExResult(e);
            }
        }
        callback.result(false, param, workResult);
        return true;

    }

    public void stopNow() {
        if (getState() == INIT || getState() == WORKING) {
            fastFail(getState(), null);
        }
    }


    private WorkResult<V> defaultResult() {
        workResult.setResultState(ResultState.TIMEOUT);
        workResult.setResult(worker.defaultValue());
        return workResult;
    }

    private WorkResult<V> defaultExResult(Exception e) {
        workResult.setResultState(ResultState.TIMEOUT);
        workResult.setResult(worker.defaultValue());
        workResult.setEx(e);
        return workResult;

    }


    private boolean checkIsNullResult() {
        return ResultState.DEFAULT == workResult.getResultState();
    }

    private boolean compareAndSetState(int expect, int update) {
        return this.state.compareAndSet(expect, update);
    }


    private void beginNext(ExecutorService executorService, long now, long remainTIme) {

        //耗時
        long costTime = SystemClock.now() - now;
        if (nextWrappers == null) {
            return;
        }
        if (nextWrappers.size() == 1) {

            nextWrappers.get(0).work(executorService, WorkerWrapper.this, remainTIme - costTime, forParamUseWrappers);
            return;
        }
        CompletableFuture[] futures = new CompletableFuture[nextWrappers.size()];
        for (int i = 0; i < nextWrappers.size(); i++) {
            int finalI = 1;
            futures[i] = CompletableFuture.runAsync(() -> nextWrappers.get(finalI).work(executorService,
                    WorkerWrapper.this, remainTIme - costTime, forParamUseWrappers), executorService);
        }

        try {
            //等待任务结束
            CompletableFuture.allOf(futures).get(remainTIme - costTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {

        }

    }


    private void fire() {


        workResult = workerDoJob();

    }

    private WorkResult<V> workerDoJob() {

        //任务状态维护  inti->working  working->finish


        callback.begin();


        //任务处理
        V resultValue = worker.action(param, forParamUseWrappers);


        callback.result(true, param, workResult);


        return workResult;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public IWorker<T, V> getWorker() {
        return worker;
    }

    public void setWorker(IWorker<T, V> worker) {
        this.worker = worker;
    }

    public ICallback<T, V> getCallback() {
        return callback;
    }

    public void setCallback(ICallback<T, V> callback) {
        this.callback = callback;
    }

    public List<WorkerWrapper<?, ?>> getNextWrappers() {
        return nextWrappers;
    }

    public void setNextWrappers(List<WorkerWrapper<?, ?>> nextWrappers) {
        this.nextWrappers = nextWrappers;
    }

    public List<DependWrapper> getDependWrappers() {
        return dependWrappers;
    }

    public void setDependWrappers(List<DependWrapper> dependWrappers) {
        this.dependWrappers = dependWrappers;
    }

    public int getState() {
        return state.get();
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }

    public Map<String, WorkerWrapper> getForParamUseWrappers() {
        return forParamUseWrappers;
    }

    public void setForParamUseWrappers(Map<String, WorkerWrapper> forParamUseWrappers) {
        this.forParamUseWrappers = forParamUseWrappers;
    }

    public WorkResult<V> getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult<V> workResult) {
        this.workResult = workResult;
    }

    public boolean isNeedCheckNextWrapperResult() {
        return needCheckNextWrapperResult;
    }

    public void setNeedCheckNextWrapperResult(boolean needCheckNextWrapperResult) {
        this.needCheckNextWrapperResult = needCheckNextWrapperResult;
    }
}
