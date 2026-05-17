package com.javafx.csit228capstone.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QueueTicket {
    private int queueId;
    private String queueNumber;
    private String department;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private LocalDateTime createdAt;
    private String status;
    private String time;
    private LocalDate date;
    private String purpose;
    private String staff;

    public QueueTicket(int queueId, String queueNumber, String department, String firstName, String middleInitial, String lastName, LocalDateTime createdAt, String status, String time, LocalDate date, String purpose, String staff) {
        this.queueId = queueId;
        this.queueNumber = queueNumber;
        this.department = department;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.status = status;
        this.time = time;
        this.date = date;
        this.purpose = purpose;
        this.staff = staff;
    }

    public QueueTicket(int queueId, String queueNumber, String department,
                       String firstName, String middleInitial, String lastName,
                       LocalDateTime createdAt, String status, String purpose, String staff) {
        this.queueId = queueId;
        this.queueNumber = queueNumber;
        this.department = department;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.status = status;
        this.purpose = purpose;
        setStaff(staff);
    }

    public QueueTicket() {}

    public int getQueueId() { return queueId; }
    public String getQueueNumber() { return queueNumber; }
    public String getDepartment() { return department; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setTime(String time) { this.time = time;}
    public void setDate(LocalDate date) { this.date = date;}
    public String getTime() { return time; }
    public LocalDate getDate() { return date; }
    public String getStaff() { return staff; }
    public void setQueueId(int queueId) { this.queueId = queueId; }
    public void setQueueNumber(String queueNumber) { this.queueNumber = queueNumber; }
    public void setDepartment(String department) { this.department = department; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleInitial(String middleInitial) {this.middleInitial = middleInitial; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) {this.status = status; }
    public void setStaff(String staff) {
        if(!staff.isBlank()) this.staff = staff;
        else staff = "Not yet assigned";
    }

    public String getFullName() {
        if (middleInitial == null || middleInitial.isBlank() || middleInitial.equalsIgnoreCase("N/A")) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleInitial + ". " + lastName;
    }

}
