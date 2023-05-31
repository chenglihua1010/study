package com.example.demo.async.worker;

public class WorkResult<V> {
    private V result;
    private ResultState resultState;

    private Exception ex;

    public WorkResult(V result, ResultState resultState, Exception ex) {
        this.resultState = resultState;
        this.ex = ex;
        this.result = result;
    }

    public WorkResult(V result, ResultState resultState) {
        this(result, resultState, null);
    }

    public static <V> WorkResult<V> defaultResult() {
        return new WorkResult<>(null, ResultState.DEFAULT);
    }

    @Override
    public String toString() {
        return "WorkerResult{" +
                "resultState=" + resultState +
                ", ex=" + ex +
                ", result=" + result +
                '}';
    }

    public ResultState getResultState() {
        return resultState;
    }

    public void setResultState(ResultState resultState) {
        this.resultState = resultState;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public V getResult() {
        return result;
    }

    public void setResult(V result) {
        this.result = result;
    }
}
