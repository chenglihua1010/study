package com.example.demo.model.createdmodel.singletonmodel;

/**
 *单例模式医用场景：多线程的线程池；数据库连接池
 */
public class Test {

    public static void main(String[] args) {
        LazySingleton lazySingleton = LazySingleton.getInstance();
        LazySingleton lazySingleton1 = LazySingleton.getInstance();


        System.out.println(lazySingleton == lazySingleton1);


        HungrySingleton hungrySingleton = HungrySingleton.getInstance();
        HungrySingleton hungrySingleton1 = HungrySingleton.getInstance();
        System.out.println(hungrySingleton == hungrySingleton1);

        SingletonEnum.Person person = SingletonEnum.INSTANCE.getInstance();
        SingletonEnum.Person person1 = SingletonEnum.INSTANCE.getInstance();

        System.out.println(person == person1);



    }

}
