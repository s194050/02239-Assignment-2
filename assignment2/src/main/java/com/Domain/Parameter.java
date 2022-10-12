package com.Domain;

public class Parameter {
    
    private String name;
    private int value;

    public Parameter(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getParameterName() {
        return name;
    }

    public int getParameterValue() {
        return value;
    }

    public void setParameterName(String name) {
        this.name = name;
    }

    public void setParameterValue(int value) {
        this.value = value;
    }
}
