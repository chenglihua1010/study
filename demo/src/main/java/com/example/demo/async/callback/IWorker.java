package com.example.demo.async.callback;

import com.example.demo.async.wrapper.WorkerWrapper;

import java.util.Map;

@FunctionalInterface
public interface IWorker<T, V> {

    V action(T object, Map<String, WorkerWrapper> allWrappers);


    default V defaultValue() {
        return null;
    }

}
