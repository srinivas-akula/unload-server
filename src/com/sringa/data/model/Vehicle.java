package com.sringa.data.model;

import org.traccar.model.ExtendedModel;

public class Vehicle extends ExtendedModel {

    private String number;
    private String model;
    private int tonnage;
    private String axle;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTonnage() {
        return tonnage;
    }

    public void setTonnage(int tonnage) {
        this.tonnage = tonnage;
    }

    public String getAxle() {
        return axle;
    }

    public void setAxle(String axle) {
        this.axle = axle;
    }

}
