package com.hrhelpdesk.model;


public class TicketCategory {
    private int categoryId;
    private String categoryName;
    private String isaTableName;

    // Getters and Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getIsaTableName() { return isaTableName; }
    public void setIsaTableName(String isaTableName) { this.isaTableName = isaTableName; }
}