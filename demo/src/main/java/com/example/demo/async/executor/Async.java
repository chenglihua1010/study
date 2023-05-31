package com.example.demo.async.executor;

import com.example.demo.async.wrapper.WorkerWrapper;
import io.netty.util.Timeout;

import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class Async {

    private static final ThreadPoolExecutor COMMON_POOL = (ThreadPoolExecutor) Executors.newCachedThreadPool();


    private static ExecutorService executorService;


    public static boolean beginWork(long timeout, ExecutorService executorService,
                                    List<WorkerWrapper> workerWrappers) throws ExecutionException,
            InterruptedException {
        if (workerWrappers == null || workerWrappers.size() == 0) {
            return false;
        }
        Async.executorService = executorService;

        Map<String, WorkerWrapper> forParamUseWrappers = new ConcurrentHashMap<>();
        CompletableFuture[] futures = new CompletableFuture[workerWrappers.size()];
        for (int i = 0; i < workerWrappers.size(); i++) {
            WorkerWrapper workerWrapper = workerWrappers.get(i);
            futures[i] = CompletableFuture.runAsync(() ->
                    workerWrapper.work(executorService, timeout, forParamUseWrappers), executorService);
        }

        try {
            CompletableFuture.allOf(futures).get(timeout, TimeUnit.MILLISECONDS);
            return true;
        } catch (TimeoutException e) {
            Set<WorkerWrapper> set = new HashSet<>();
            totalWorkers(workerWrappers, set);
            for (WorkerWrapper workerWrapper : set) {
                workerWrapper.stopNow();

            }
            return false;
        }
    }

    private static void totalWorkers(List<WorkerWrapper> workerWrappers, Set<WorkerWrapper> set) {
        set.addAll(workerWrappers);

        for (WorkerWrapper workerWrapper : workerWrappers) {
            if (workerWrapper.getNextWrappers() != null) {
                continue;
            }
            List<WorkerWrapper> wrappers = workerWrapper.getNextWrappers();
            totalWorkers(wrappers, set);
        }


    }

}




