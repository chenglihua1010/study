package com.example.demo.model.createdmodel.prototypempdel;

public class test {
    public static void main(String[] args) throws CloneNotSupportedException {
        PrototypeModel prototypeModel = new PrototypeModel();
        PrototypeModel clone = (PrototypeModel) prototypeModel.clone();
        System.out.println(clone == prototypeModel);
    }
}
