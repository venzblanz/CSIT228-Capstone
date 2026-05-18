package com.javafx.csit228capstone.model;

public class Service {
    private int serviceId;
    private String name;
    private String serviceType;
    private boolean recurring;

    private Service(int serviceId, String name, String serviceType, boolean recurring) {
        this.serviceId = serviceId;
        this.name = name;
        this.serviceType = serviceType;
        this.recurring = recurring;
    }

    public static Service fromDatabase(int serviceId, String name, String serviceType, boolean recurring) {
        return new Service(serviceId, name, serviceType, recurring);
    }

    public static Service createNew(String name, String serviceType, boolean recurring) {
        return new Service(-1, name, serviceType, recurring);
    }

    public static Service createNewWithId(int serviceId, String name, String serviceType, boolean recurring) {
        return new Service(serviceId, name, serviceType, recurring);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setServiceType(String type) {
        this.serviceType = type;
    }

    public String getChipColor() {
        switch (serviceType) {
            case "Women's Health":
                return "pink";
            case "Specialized Fields":
                return "green";
            case "Diagnostics & Laboratory":
                return "purple";
            default:
                return "blue";
        }
    }
}