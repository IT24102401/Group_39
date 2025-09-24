package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Report;  // Assuming a Report model class exists or needs to be created
import com.hrhelpdesk.model.TicketReport;  // Assuming specialized models if needed
import com.hrhelpdesk.model.LeaveReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {

    // Generate Ticket Report and store summary in database
    public int generateTicketReport(Date from, Date to, int generatedBy, String parameters) throws SQLException {
        int reportId = insertReport("Ticket", from, to, parameters, generatedBy);

        String openCountSql = "SELECT COUNT(*) FROM TICKET WHERE created_at BETWEEN ? AND ? AND status = 'Open'";
        String closedCountSql = "SELECT COUNT(*) FROM TICKET WHERE closed_at BETWEEN ? AND ? AND status IN ('Resolved', 'Closed')";
        String avgSql = "SELECT AVG(DATEDIFF(HOUR, created_at, closed_at) / 24.0) FROM TICKET WHERE closed_at BETWEEN ? AND ? AND status IN ('Resolved', 'Closed')";

        try (Connection conn = DBConnection.getConnection()) {
            int openCount = executeCountQuery(conn, openCountSql, from, to);
            int closedCount = executeCountQuery(conn, closedCountSql, from, to);
            double avgTime = executeDoubleQuery(conn, avgSql, from, to);

            String insertTicketReport = "INSERT INTO TICKET_REPORT (report_id, open_count, closed_count, avg_resolution_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertTicketReport)) {
                ps.setInt(1, reportId);
                ps.setInt(2, openCount);
                ps.setInt(3, closedCount);
                ps.setDouble(4, avgTime);
                ps.executeUpdate();
            }
        }
        return reportId;
    }

    // Generate Leave Report and store summary in database (overall, dept_id null)
    public int generateLeaveReport(Date from, Date to, int generatedBy, String parameters, Integer deptId) throws SQLException {
        int reportId = insertReport("Leave", from, to, parameters, generatedBy);

        String sql = "SELECT SUM(DATEDIFF(DAY, l.start_date, l.end_date) + 1) AS total_leaves " +
                "FROM LEAVE_TICKET l JOIN TICKET t ON l.ticket_id = t.ticket_id " +
                "JOIN [USER] u ON t.submitted_by = u.user_id " +
                "WHERE t.closed_at BETWEEN ? AND ? AND t.status IN ('Resolved', 'Closed')";
        if (deptId != null) {
            sql += " AND u.dept_id = ?";
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, from);
            ps.setDate(2, to);
            if (deptId != null) {
                ps.setInt(3, deptId);
            }
            ResultSet rs = ps.executeQuery();
            int totalLeaves = 0;
            if (rs.next()) {
                totalLeaves = rs.getInt("total_leaves");
            }

            String insertLeaveReport = "INSERT INTO LEAVE_REPORT (report_id, total_leaves, dept_id) VALUES (?, ?, ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertLeaveReport)) {
                psInsert.setInt(1, reportId);
                psInsert.setInt(2, totalLeaves);
                if (deptId != null) {
                    psInsert.setInt(3, deptId);
                } else {
                    psInsert.setNull(3, Types.INTEGER);
                }
                psInsert.executeUpdate();
            }
        }
        return reportId;
    }

    // Helper to insert base report and return generated ID
    private int insertReport(String type, Date from, Date to, String parameters, int generatedBy) throws SQLException {
        String sql = "INSERT INTO REPORT (report_type, period_from, period_to, parameters, generated_by) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, type);
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setString(4, parameters);
            ps.setInt(5, generatedBy);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to generate report ID.");
    }

    // Fetch all reports
    public List<Map<String, Object>> getAllReports() throws SQLException {
        List<Map<String, Object>> reports = new ArrayList<>();
        String sql = "SELECT r.*, u.username AS generated_by_username " +
                "FROM REPORT r JOIN [USER] u ON r.generated_by = u.user_id " +
                "ORDER BY r.generated_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> report = new HashMap<>();
                report.put("reportId", rs.getInt("report_id"));
                report.put("reportType", rs.getString("report_type"));
                report.put("periodFrom", rs.getDate("period_from"));
                report.put("periodTo", rs.getDate("period_to"));
                report.put("parameters", rs.getString("parameters"));
                report.put("generatedAt", rs.getTimestamp("generated_at"));
                report.put("generatedBy", rs.getInt("generated_by"));
                report.put("generatedByUsername", rs.getString("generated_by_username"));
                reports.add(report);
            }
        }
        return reports;
    }

    // Get details for a Ticket Report
    public Map<String, Object> getTicketReportDetails(int reportId) throws SQLException {
        Map<String, Object> details = new HashMap<>();
        String sql = "SELECT * FROM TICKET_REPORT WHERE report_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    details.put("openCount", rs.getInt("open_count"));
                    details.put("closedCount", rs.getInt("closed_count"));
                    details.put("avgResolutionTime", rs.getDouble("avg_resolution_time"));
                }
            }
        }
        return details;
    }

    // Get details for a Leave Report
    public Map<String, Object> getLeaveReportDetails(int reportId) throws SQLException {
        Map<String, Object> details = new HashMap<>();
        String sql = "SELECT * FROM LEAVE_REPORT WHERE report_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    details.put("totalLeaves", rs.getInt("total_leaves"));
                    details.put("deptId", rs.getObject("dept_id"));
                }
            }
        }
        return details;
    }

    // Helper to execute count queries
    private int executeCountQuery(Connection conn, String sql, Date from, Date to) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, from);
            ps.setDate(2, to);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Helper to execute double queries (e.g., AVG)
    private double executeDoubleQuery(Connection conn, String sql, Date from, Date to) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, from);
            ps.setDate(2, to);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }
}