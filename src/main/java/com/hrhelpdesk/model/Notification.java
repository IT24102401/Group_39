package com.hrhelpdesk.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private String type;
    private boolean isRead;
    private Timestamp createdAt;

    // Default constructor
    public Notification() {}

    // Getters and Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean getIsRead() { return isRead; }
    public void setIsRead(boolean isRead) { this.isRead = isRead; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Helper method for relative time
    public String getRelativeTime() {
        if (createdAt == null) return "";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime created = createdAt.toLocalDateTime();
        long hours = java.time.temporal.ChronoUnit.HOURS.between(created, now);
        if (hours < 1) {
            return "Just now";
        } else if (hours < 24) {
            return hours + "h ago";
        } else {
            return java.time.temporal.ChronoUnit.DAYS.between(created, now) + "d ago";
        }
    }
}
