package com.example.demo.model.createdmodel.abstractfactorymodel;

import com.example.demo.model.createdmodel.simpleFactoryModel.ElectricVehicle;
import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public class ElecFactory implements AbstractFactory {


    @Override
    public Vehicle buyVehicle() {
        return new ElectricVehicle();
    }

    @Override
    public Fitting buyFitting() {
        return new ElecFitting();
    }
}
