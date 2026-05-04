package com.javafx.csit228capstone.model;

import java.time.LocalDateTime;

public class QueueTicket {
    private int queueId;
    private String queueNumber;
    private String department;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private LocalDateTime createdAt;

    public QueueTicket(int queueId, String queueNumber, String department,
                       String firstName, String middleInitial, String lastName,
                       LocalDateTime createdAt) {
        this.queueId = queueId;
        this.queueNumber = queueNumber;
        this.department = department;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public int getQueueId() {
        return queueId;
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public String getDepartment() {
        return department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getFullName() {
        if (middleInitial == null || middleInitial.isBlank() || middleInitial.equalsIgnoreCase("N/A")) {
            return firstName + " " + lastName;
        }

        return firstName + " " + middleInitial + ". " + lastName;
    }
}
