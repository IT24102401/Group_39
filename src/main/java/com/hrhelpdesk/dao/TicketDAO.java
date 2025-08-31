package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Ticket;
import java.io.InputStream;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class TicketDAO {

    public int createGeneralTicket(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO TICKET (title, description, status, priority, submitted_by, category_id) " +
                "VALUES (?, ?, 'Open', ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getDescription());
            ps.setString(3, ticket.getPriority());
            ps.setInt(4, ticket.getSubmittedBy());
            ps.setInt(5, ticket.getCategoryId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create ticket.");
    }

    public void insertLeaveTicket(int ticketId, String leaveType, String startDateStr, String endDateStr) throws SQLException {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        String sql = "INSERT INTO LEAVE_TICKET (ticket_id, leave_type, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, leaveType);
            ps.setDate(3, Date.valueOf(startDate));
            ps.setDate(4, Date.valueOf(endDate));
            ps.executeUpdate();
        }
    }

    public void insertSalaryTicket(int ticketId, String monthYear, String issueDetails) throws SQLException {
        String sql = "INSERT INTO SALARY_TICKET (ticket_id, month_year, issue_details) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, monthYear);
            ps.setString(3, issueDetails);
            ps.executeUpdate();
        }
    }

    public void insertComplaintTicket(int ticketId, int againstUserId, String severity) throws SQLException {
        String sql = "INSERT INTO COMPLAINT_TICKET (ticket_id, against_user_id, severity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setInt(2, againstUserId);
            ps.setString(3, severity);
            ps.executeUpdate();
        }
    }

    public void insertServiceLetterTicket(int ticketId, String letterType, String recipientName) throws SQLException {
        String sql = "INSERT INTO SERVICE_LETTER_TICKET (ticket_id, letter_type, recipient_name) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, letterType);
            ps.setString(3, recipientName);
            ps.executeUpdate();
        }
    }

    public void insertIdCardTicket(int ticketId, String cardIssueType, String details) throws SQLException {
        String sql = "INSERT INTO ID_CARD_TICKET (ticket_id, card_issue_type, details) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, cardIssueType);
            ps.setString(3, details);
            ps.executeUpdate();
        }
    }

    public void insertPromotionTransferTicket(int ticketId, String requestType, String reason) throws SQLException {
        String sql = "INSERT INTO PROMOTION_TRANSFER_TICKET (ticket_id, request_type, reason) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, requestType);
            ps.setString(3, reason);
            ps.executeUpdate();
        }
    }

    public void insertAttachment(int ticketId, String fileName, InputStream fileData) throws SQLException {
        String sql = "INSERT INTO TICKET_ATTACHMENT (ticket_id, file_name, file_data) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, fileName);
            ps.setBlob(3, fileData);
            ps.executeUpdate();
        }
    }

    public List<String> getAttachmentNames(int ticketId) throws SQLException {
        List<String> attachmentNames = new ArrayList<>();
        String sql = "SELECT file_name FROM TICKET_ATTACHMENT WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    attachmentNames.add(rs.getString("file_name"));
                }
            }
        }
        return attachmentNames;
    }

    public Blob getAttachmentData(int ticketId, String fileName) throws SQLException {
        String sql = "SELECT file_data FROM TICKET_ATTACHMENT WHERE ticket_id = ? AND file_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, fileName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBlob("file_data");
                }
            }
        }
        return null;
    }

    public List<Ticket> getTicketsByUser(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id WHERE t.submitted_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketId(rs.getInt("ticket_id"));
                    ticket.setTitle(rs.getString("title"));
                    ticket.setDescription(rs.getString("description"));
                    ticket.setStatus(rs.getString("status"));
                    ticket.setPriority(rs.getString("priority"));
                    ticket.setCreatedAt(rs.getTimestamp("created_at"));
                    ticket.setUpdatedAt(rs.getTimestamp("updated_at"));
                    ticket.setClosedAt(rs.getTimestamp("closed_at"));
                    ticket.setSubmittedBy(rs.getInt("submitted_by"));
                    ticket.setAssignedTo(rs.getObject("assigned_to") != null ? rs.getInt("assigned_to") : null);
                    ticket.setCategoryId(rs.getInt("category_id"));
                    ticket.setCategoryName(rs.getString("category_name"));
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }

    public boolean isTicketOwnedByUser(int ticketId, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TICKET WHERE ticket_id = ? AND submitted_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isTicketOpen(int ticketId) throws SQLException {
        String sql = "SELECT status FROM TICKET WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Open".equals(rs.getString("status"));
                }
            }
        }
        return false;
    }

    public void cancelTicket(int ticketId) throws SQLException {
        String sql = "UPDATE TICKET SET status = 'Cancelled', closed_at = CURRENT_TIMESTAMP WHERE ticket_id = ? AND status = 'Open'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to cancel ticket: Ticket not found or not open.");
            }
        }
    }
}