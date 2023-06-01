package com.example.demo.model.createdmodel.builderModel;

public class test {
    public static void main(String[] args) {

        ComputerInstallA computerInstallA = new ComputerInstallA();
        Boss boss = new Boss(computerInstallA);
        Computer computer = boss.build();
        computer.show();



    }
}
