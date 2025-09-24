package com.hrhelpdesk.model;

import java.sql.Timestamp;

public class TicketResponse {
    private int responseId;
    private int ticketId;
    private String message;
    private Timestamp createdAt;
    private int respondedBy;
    private String respondedByUsername;


    public int getResponseId() { return responseId; }
    public void setResponseId(int responseId) { this.responseId = responseId; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getRespondedBy() { return respondedBy; }
    public void setRespondedBy(int respondedBy) { this.respondedBy = respondedBy; }

    public String getRespondedByUsername() { return respondedByUsername; }
    public void setRespondedByUsername(String respondedByUsername) { this.respondedByUsername = respondedByUsername; }



    public String getRelativeTime() {

        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(createdAt);
    }
}