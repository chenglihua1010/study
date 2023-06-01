package com.example.demo.model.createdmodel.simpleFactoryModel;

public class test {
    public Vehicle createVehicle(int type) {
        SimpleVehicleFactory
                simpleVehicleFactory = new SimpleVehicleFactory();
        Vehicle ve = simpleVehicleFactory.VehicleFactory(type);
//        ve.info();
        return ve;
    }


    public static void main(String[] args) {
        new test().createVehicle(1);
    }


}
