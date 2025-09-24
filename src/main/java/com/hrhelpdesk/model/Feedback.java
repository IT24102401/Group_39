package com.hrhelpdesk.model;

import java.sql.Timestamp;

public class Feedback {
    private int feedbackId;
    private String message;
    private int rating; // 1-5
    private int userId;
    private String submittedByUsername; // Added field
    private Timestamp createdAt;

    // Getters and Setters
    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getSubmittedByUsername() { return submittedByUsername; }
    public void setSubmittedByUsername(String submittedByUsername) { this.submittedByUsername = submittedByUsername; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
