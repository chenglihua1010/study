package com.example.demo.model.createdmodel.builderModel;

public class Computer {
    private String monitor;
    private String processor;
    private String graphics;
    private String ram;
    private String hadDisk;
    private String powerSupply;
    private String motherBoard;

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getHadDisk() {
        return hadDisk;
    }

    public void setHadDisk(String hadDisk) {
        this.hadDisk = hadDisk;
    }

    public String getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(String powerSupply) {
        this.powerSupply = powerSupply;
    }

    public String getMotherBoard() {
        return motherBoard;
    }

    public void setMotherBoard(String motherBoard) {
        this.motherBoard = motherBoard;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "monitor='" + monitor + '\'' +
                ", processor='" + processor + '\'' +
                ", graphics='" + graphics + '\'' +
                ", ram='" + ram + '\'' +
                ", hadDisk='" + hadDisk + '\'' +
                ", powerSupply='" + powerSupply + '\'' +
                ", motherBoard='" + motherBoard + '\'' +
                '}';
    }

    public void show() {
        System.out.println(toString());
    }
}
