package com.example.demo.model.createdmodel.simpleFactoryModel;

public class PowerVehicle implements Vehicle {
    public PowerVehicle() {
        info();
    }

    @Override
    public void info() {
        System.out.println("Power Vehicle");
    }
}
