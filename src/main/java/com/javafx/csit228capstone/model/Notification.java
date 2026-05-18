package com.javafx.csit228capstone.model;

import java.time.LocalDateTime;

public class Notification {
    private int notifId;
    private int userId;
    private String title;
    private String message;
    private String type;       // JOINED, CANCELLED, ALMOST_TURN
    private boolean isRead;
    private LocalDateTime createdAt;

    public Notification(int notifId, int userId, String title, String message,
                        String type, boolean isRead, LocalDateTime createdAt) {
        this.notifId   = notifId;
        this.userId    = userId;
        this.title     = title;
        this.message   = message;
        this.type      = type;
        this.isRead    = isRead;
        this.createdAt = createdAt;
    }

    public int getNotifId()          { return notifId; }
    public int getUserId()           { return userId; }
    public String getTitle()         { return title; }
    public String getMessage()       { return message; }
    public String getType()          { return type; }
    public boolean isRead()          { return isRead; }
    public void setRead(boolean r)   { this.isRead = r; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}