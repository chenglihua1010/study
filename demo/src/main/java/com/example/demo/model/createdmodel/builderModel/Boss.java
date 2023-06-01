package com.example.demo.model.createdmodel.builderModel;

public class Boss {


    private AbstractBuilder abstractBuilder;

    public Boss(AbstractBuilder abstractBuilder) {
        this.abstractBuilder = abstractBuilder;
    }

    public Computer build() {
        abstractBuilder.build1();
        abstractBuilder.build2();
        abstractBuilder.build3();
        abstractBuilder.build4();
        abstractBuilder.build5();
        abstractBuilder.build6();
        abstractBuilder.build7();
        return abstractBuilder.getComputer();
    }

}
