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

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public QueueTicket() {}

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
