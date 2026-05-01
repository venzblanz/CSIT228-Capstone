package com.javafx.csit228capstone.model;

import java.time.LocalDate;

public class QueueHistory {
    private int queueNumber;
    private String service;
    private String status;
    private LocalDate date;
    private User patient;

    public QueueHistory(int queueNumber, String service, String status, LocalDate date, User patient) {
        this.queueNumber = queueNumber;
        this.service = service;
        this.status = status;
        this.date = date;
        this.patient = patient;
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

    public User getPatient() {
        return patient;
    }
}
