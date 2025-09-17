package com.hrhelpdesk.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private int userId;
    private String employeeId;
    private String username;
    private String passwordHash; // Stores BCrypt-hashed password
    private String email;
    private String firstName;
    private String lastName;
    private Integer deptId; // Nullable, as Management role has no department
    private String deptName; // Transient, for display
    private Department department; // Composition: User has a Department
    private String jobTitle;
    private String phoneNumbers;
    private String address;
    private int roleId;
    private Role role; // Composition: User has a Role
    private String roleName; // Transient, for display
    private boolean deleted;
    private Timestamp createdAt; // Tracks user creation timestamp
    private Timestamp deletedAt;
    private String deletionReason;
    private int deletedBy;
    private String deletedByUsername; // Transient, for display in removed users

    // Composition: User has Tickets (one-to-many)
    private List<Ticket> submittedTickets = new ArrayList<>();
    private List<Ticket> assignedTickets = new ArrayList<>();

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(String phoneNumbers) { this.phoneNumbers = phoneNumbers; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
    public String getDeletionReason() { return deletionReason; }
    public void setDeletionReason(String deletionReason) { this.deletionReason = deletionReason; }
    public int getDeletedBy() { return deletedBy; }
    public void setDeletedBy(int deletedBy) { this.deletedBy = deletedBy; }
    public String getDeletedByUsername() { return deletedByUsername; }
    public void setDeletedByUsername(String deletedByUsername) { this.deletedByUsername = deletedByUsername; }

    // Tickets
    public List<Ticket> getSubmittedTickets() { return submittedTickets; }
    public void setSubmittedTickets(List<Ticket> submittedTickets) { this.submittedTickets = submittedTickets; }
    public void addSubmittedTicket(Ticket ticket) { this.submittedTickets.add(ticket); }
    public List<Ticket> getAssignedTickets() { return assignedTickets; }
    public void setAssignedTickets(List<Ticket> assignedTickets) { this.assignedTickets = assignedTickets; }
    public void addAssignedTicket(Ticket ticket) { this.assignedTickets.add(ticket); }

    // Abstract role-specific behaviors (used in service layer for authorization)
    public abstract boolean canSubmitTicket();
    public abstract boolean canViewAllTickets();
}