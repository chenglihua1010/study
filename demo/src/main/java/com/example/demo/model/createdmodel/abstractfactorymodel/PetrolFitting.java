package com.example.demo.model.createdmodel.abstractfactorymodel;

public class PetrolFitting implements Fitting {
    public PetrolFitting() {
        info();
    }

    @Override
    public void info() {
        System.out.println("油车配件");
    }
}
