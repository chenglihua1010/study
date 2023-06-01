package com.example.demo.model.createdmodel.simpleFactoryModel;

/**
 * 简单工厂模式：
 */
public class SimpleVehicleFactory {
    public Vehicle VehicleFactory(int type) {
        Vehicle vehicle = null;
        if (type == 1) {
            vehicle = new ElectricVehicle();
        } else if (type == 2) {
            vehicle = new PowerVehicle();
        }
        return vehicle;

    }
}
