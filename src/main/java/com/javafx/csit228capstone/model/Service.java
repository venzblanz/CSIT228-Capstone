package com.javafx.csit228capstone.model;

public class Service {
    private int serviceId;
    private String name;
    private String serviceType;

    public Service(int serviceId, String name, String serviceType) {
        this.serviceId = serviceId;
        this.name = name;
        this.serviceType = serviceType;
    }

    public Service(String name, String serviceType) {
        this.serviceId = -1;
        this.name = name;
        this.serviceType = serviceType;
    }

    public int getServiceId()      { return serviceId; }
    public String getName()        { return name; }
    public String getServiceType() { return serviceType; }

    public String getChipColor() {
        return switch (serviceType) {
            case "Women's Health"           -> "pink";
            case "Specialized Fields"       -> "green";
            case "Diagnostics & Laboratory" -> "purple";
            default                         -> "blue";
        };
    }
}