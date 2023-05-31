package com.example.demo.async.callback;

import com.example.demo.async.worker.WorkResult;

@FunctionalInterface
public interface ICallback<T, V> {

    default void begin(){}


    void result(boolean success, T param, WorkResult<V> result);
}
