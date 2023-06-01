package com.example.demo.model.createdmodel.builderModel;

public abstract class AbstractBuilder {
    protected Computer computer = new Computer();

    public Computer getComputer() {
        return computer;
    }

    public abstract void build1();

    public abstract void build2();

    public abstract void build3();

    public abstract void build4();

    public abstract void build5();

    public abstract void build6();

    public abstract void build7();
}
