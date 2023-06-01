package com.example.demo.model.createdmodel.abstractfactorymodel;

import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public class PetrolFactory implements AbstractFactory {
    @Override
    public Vehicle buyVehicle() {
        return new PetrolVehicle();
    }

    @Override
    public Fitting buyFitting() {
        return new PetrolFitting();
    }
}
