package com.example.demo.model.createdmodel.singletonmodel;

public enum SingletonEnum {

    INSTANCE;

    private Person person;

    SingletonEnum() {

        person = new Person();
    }

    public Person getInstance() {
        return person;
    }

    class Person {

    }
}
