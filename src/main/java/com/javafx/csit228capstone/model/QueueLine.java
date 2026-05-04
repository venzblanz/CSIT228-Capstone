package com.javafx.csit228capstone.model;

public class QueueLine {
    private int queueId;
    private int formId;
    private int userId;
    private String department;
    private String queueNumber;
    private String status;
    private String createdAt;

    public QueueLine(int queueId, int formId, int userId, String department, String queueNumber, String status, String createdAt) {
        this.queueId = queueId;
        this.formId = formId;
        this.userId = userId;
        this.department = department;
        this.queueNumber = queueNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getQueueId() { return queueId; }
    public int getFormId() { return formId; }
    public int getUserId() { return userId; }
    public String getDepartment() { return department; }
    public String getQueueNumber() { return queueNumber; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
