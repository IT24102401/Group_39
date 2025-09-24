package com.hrhelpdesk.model;

public class LeaveReport extends Report {
    private int totalLeaves;
    private Integer deptId;
    private String deptName; // Transient

    // Getters and Setters
    public int getTotalLeaves() { return totalLeaves; }
    public void setTotalLeaves(int totalLeaves) { this.totalLeaves = totalLeaves; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
}