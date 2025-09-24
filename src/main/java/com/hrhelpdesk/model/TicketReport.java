
package com.hrhelpdesk.model;

public class TicketReport extends Report {
    private int openCount;
    private int closedCount;
    private double avgResolutionTime;

    // Getters and Setters
    public int getOpenCount() { return openCount; }
    public void setOpenCount(int openCount) { this.openCount = openCount; }
    public int getClosedCount() { return closedCount; }
    public void setClosedCount(int closedCount) { this.closedCount = closedCount; }
    public double getAvgResolutionTime() { return avgResolutionTime; }
    public void setAvgResolutionTime(double avgResolutionTime) { this.avgResolutionTime = avgResolutionTime; }
}