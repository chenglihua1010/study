package com.example.demo.model.createdmodel.factoryMethodModel;

import com.example.demo.model.createdmodel.simpleFactoryModel.ElectricVehicle;
import com.example.demo.model.createdmodel.simpleFactoryModel.PowerVehicle;
import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public class NewCompany implements VehicleCenterCompany {
    @Override
    public Vehicle buyVehicle(int type) {
        Vehicle vehicle = null;
        if (type == 1) {
            vehicle = new PowerVehicle();
        } else if (type == 2) {
            vehicle = new ElectricVehicle();
        }

        return vehicle;
    }
}
