package com.example.demo.model.createdmodel.singletonmodel;

/**
 * 类一旦加载就创建一个单例，保证在调用getInstance 方法之前单例已经存在；
 * 创建好一个静态的对象供系统使用，以后不再改变，所以是县城安全的；
 * 无形中占用很多内存，没有延迟加载；
 *
 */
public class HungrySingleton {
    private static HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {
        System.out.println("hungry model ,only one create");
    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
