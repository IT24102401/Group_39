package com.hrhelpdesk.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private int ticketId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp closedAt;
    private int submittedBy;
    private User submittedByUser; // Bidirectional
    private Integer assignedTo;
    private User assignedToUser; // Bidirectional
    private int categoryId;
    private TicketCategory category;
    private String categoryName;
    private String submittedByUsername;
    private String assignedToUsername;
    private String submittedByEmail;
    private List<String> submittedByPhone; // Changed to List<String>
    private String deptName;

    // Constructors
    public Ticket() {
        this.submittedByPhone = new ArrayList<>(); // Initialize the list
    }

    public Ticket(int ticketId, String title, String description, String status, String priority, Timestamp createdAt,
                  int submittedBy, Integer assignedTo, int categoryId, String categoryName, String submittedByUsername,
                  String assignedToUsername, String submittedByEmail, List<String> submittedByPhone, String deptName) {
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.submittedBy = submittedBy;
        this.assignedTo = assignedTo;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.submittedByUsername = submittedByUsername;
        this.assignedToUsername = assignedToUsername;
        this.submittedByEmail = submittedByEmail;
        this.submittedByPhone = (submittedByPhone != null) ? new ArrayList<>(submittedByPhone) : new ArrayList<>();
        this.deptName = deptName;
    }

    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getClosedAt() { return closedAt; }
    public void setClosedAt(Timestamp closedAt) { this.closedAt = closedAt; }

    public int getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(int submittedBy) { this.submittedBy = submittedBy; }

    public User getSubmittedByUser() { return submittedByUser; }
    public void setSubmittedByUser(User submittedByUser) {
        this.submittedByUser = submittedByUser;
        if (submittedByUser != null) {
            submittedByUser.addSubmittedTicket(this);
            // Update email, phone, and deptName from User if not already set
            if (this.submittedByEmail == null) this.submittedByEmail = submittedByUser.getEmail();
            if (this.submittedByPhone == null || this.submittedByPhone.isEmpty()) {
                this.submittedByPhone = new ArrayList<>(submittedByUser.getPhoneNumbers());
            }
            if (this.deptName == null && submittedByUser.getDepartment() != null) {
                this.deptName = submittedByUser.getDepartment().getDeptName();
            }
        }
    }

    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

    public User getAssignedToUser() { return assignedToUser; }
    public void setAssignedToUser(User assignedToUser) {
        this.assignedToUser = assignedToUser;
        if (assignedToUser != null) assignedToUser.addAssignedTicket(this);
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public TicketCategory getCategory() { return category; }
    public void setCategory(TicketCategory category) { this.category = category; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubmittedByUsername() { return submittedByUsername; }
    public void setSubmittedByUsername(String submittedByUsername) { this.submittedByUsername = submittedByUsername; }

    public String getAssignedToUsername() { return assignedToUsername; }
    public void setAssignedToUsername(String assignedToUsername) { this.assignedToUsername = assignedToUsername; }

    public String getSubmittedByEmail() { return submittedByEmail; }    // Added getter
    public void setSubmittedByEmail(String submittedByEmail) { this.submittedByEmail = submittedByEmail; }    // Added setter

    public List<String> getSubmittedByPhone() { return submittedByPhone; }    // Updated getter
    public void setSubmittedByPhone(List<String> submittedByPhone) {
        this.submittedByPhone = (submittedByPhone != null) ? new ArrayList<>(submittedByPhone) : new ArrayList<>();
    }    // Updated setter

    public String getDeptName() { return deptName; }    // Added getter
    public void setDeptName(String deptName) { this.deptName = deptName; }    // Added setter

    // Helper method to manage bidirectional relationship (assumed in User class)
    public void addSubmittedTicket(Ticket ticket) {
        if (this.submittedByUser != null) {
            List<Ticket> submittedTickets = this.submittedByUser.getSubmittedTickets();
            if (submittedTickets == null) submittedTickets = new ArrayList<>();
            if (!submittedTickets.contains(ticket)) submittedTickets.add(ticket);
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", closedAt=" + closedAt +
                ", submittedBy=" + submittedBy +
                ", submittedByUser=" + (submittedByUser != null ? submittedByUser.getUsername() : "null") +
                ", assignedTo=" + assignedTo +
                ", assignedToUser=" + (assignedToUser != null ? assignedToUser.getUsername() : "null") +
                ", categoryId=" + categoryId +
                ", category=" + (category != null ? category.getCategoryName() : "null") +
                ", categoryName='" + categoryName + '\'' +
                ", submittedByUsername='" + submittedByUsername + '\'' +
                ", assignedToUsername='" + assignedToUsername + '\'' +
                ", submittedByEmail='" + submittedByEmail + '\'' +
                ", submittedByPhone=" + submittedByPhone + // Updated to handle List<String>
                ", deptName='" + deptName + '\'' +
                '}';
    }
}