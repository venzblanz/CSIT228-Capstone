package com.javafx.csit228capstone.model;

public class Service {
    private String name;
    private String serviceType;

    public Service(String name, String serviceType) {
        this.name = name;
        this.serviceType = serviceType;
    }

    public String getName() { return name; }
    public String getServiceType() { return serviceType; }
    public void setName(String name) { this.name = name; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
}