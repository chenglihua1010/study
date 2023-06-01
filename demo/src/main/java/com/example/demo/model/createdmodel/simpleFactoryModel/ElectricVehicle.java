package com.example.demo.model.createdmodel.simpleFactoryModel;

public class ElectricVehicle implements Vehicle {
    public ElectricVehicle() {
        info();
    }

    @Override
    public void info() {
        System.out.println("电车");
    }
}
