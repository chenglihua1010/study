package com.example.demo.model.createdmodel.prototypempdel;

public class PrototypeModel implements Cloneable {

    public PrototypeModel() {
        System.out.println("具体原型创建成功");
    }

    protected Object clone() throws CloneNotSupportedException {


        System.out.println("具体原型克隆成功");
        return super.clone();
    }
}
