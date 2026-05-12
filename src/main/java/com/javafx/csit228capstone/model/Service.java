package com.javafx.csit228capstone.model;

public class Service {
    private int serviceId;
    private String name;
    private String serviceType;
    private boolean recurring; // NEW

    public Service(int serviceId, String name, String serviceType, boolean recurring) {
        this.serviceId = serviceId;
        this.name = name;
        this.serviceType = serviceType;
        this.recurring = recurring;
    }

    public Service(String name, String serviceType) {
        this(-1, name, serviceType, true);
    }

    public Service(String name, String serviceType, boolean recurring) {
        this(-1, name, serviceType, recurring);
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public String getChipColor() {
        return switch (serviceType) {
            case "Women's Health"           -> "pink";
            case "Specialized Fields"       -> "green";
            case "Diagnostics & Laboratory" -> "purple";
            default                         -> "blue";
        };
    }
}