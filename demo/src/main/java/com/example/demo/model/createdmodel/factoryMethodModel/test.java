package com.example.demo.model.createdmodel.factoryMethodModel;

import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public class test {
    public static void main(String[] args) {
        VehicleCenterCompany vehicleCenterCompany = new NewCompany();
        Vehicle vehicle = vehicleCenterCompany.buyVehicle(1);
        VehicleCenterCompany vehicleCenterCompany1 = new OldCompany();
        Vehicle vehicle1 = vehicleCenterCompany1.buyVehicle(1);
    }
}
