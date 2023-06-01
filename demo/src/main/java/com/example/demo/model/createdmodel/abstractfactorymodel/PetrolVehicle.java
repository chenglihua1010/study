package com.example.demo.model.createdmodel.abstractfactorymodel;

import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public class PetrolVehicle implements Vehicle {
    public PetrolVehicle() {
        info();
    }

    @Override
    public void info() {
        System.out.println("油车");
    }
}
