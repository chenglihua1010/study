package com.example.demo.model.createdmodel.abstractfactorymodel;

public class test {

    public static void main(String[] args) {
        AbstractFactory factory = new ElecFactory();
        factory.buyFitting();

        AbstractFactory factory1 = new PetrolFactory();
        factory1.buyVehicle();

    }

}
