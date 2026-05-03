package com.javafx.csit228capstone.model;

import java.time.LocalDate;

public class QueueHistory {
    private int queueNumber;
    private String service;
    private String status;
    private LocalDate date;
    private String department;
    private User patient;
    private String staff;

    public QueueHistory(int queueNumber, String service, String status, LocalDate date, String department, User patient, String staff) {
        this.queueNumber = queueNumber;
        this.service = service;
        this.status = status;
        this.date = date;
        this.department = department;
        this.patient = patient;
        this.staff = staff;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public String getService() {
        return service;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDepartment() {
        return department;
    }

    public User getPatient() {
        return patient;
    }

    public String getStaff() {
        return staff;
    }
}
