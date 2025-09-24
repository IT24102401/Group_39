// Report.java - Model class for base report
package com.hrhelpdesk.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Report {
    private int reportId;
    private String reportType;
    private Date periodFrom;
    private Date periodTo;
    private String parameters;
    private Timestamp generatedAt;
    private int generatedBy;
    private String generatedByUsername; // Transient

    // Getters and Setters
    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public Date getPeriodFrom() { return periodFrom; }
    public void setPeriodFrom(Date periodFrom) { this.periodFrom = periodFrom; }
    public Date getPeriodTo() { return periodTo; }
    public void setPeriodTo(Date periodTo) { this.periodTo = periodTo; }
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    public Timestamp getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Timestamp generatedAt) { this.generatedAt = generatedAt; }
    public int getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(int generatedBy) { this.generatedBy = generatedBy; }
    public String getGeneratedByUsername() { return generatedByUsername; }
    public void setGeneratedByUsername(String generatedByUsername) { this.generatedByUsername = generatedByUsername; }
}