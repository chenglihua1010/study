package com.example.demo.model.createdmodel.singletonmodel;


/**
 * 第一次调用getInstance 时，创建对象；
 * <p>
 * 线程不安全；双重判断加锁
 */
public class LazySingleton {

    private static LazySingleton instance = null;


    private LazySingleton() {
        System.out.println("lazy model create");
    }

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }


}
