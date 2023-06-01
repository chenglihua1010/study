package com.example.demo.model.createdmodel.abstractfactorymodel;

import com.example.demo.model.createdmodel.simpleFactoryModel.Vehicle;

public interface AbstractFactory {

    Vehicle buyVehicle();

    Fitting buyFitting();


}
