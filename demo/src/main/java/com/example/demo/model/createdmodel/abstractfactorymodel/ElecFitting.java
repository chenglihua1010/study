package com.example.demo.model.createdmodel.abstractfactorymodel;

public class ElecFitting implements Fitting {
    public ElecFitting() {
        info();
    }
    @Override
    public void info() {
        System.out.println("电动配件");
    }
}
