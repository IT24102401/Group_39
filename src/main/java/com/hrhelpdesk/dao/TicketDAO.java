package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Ticket;
import java.io.InputStream;
import java.time.LocalDateTime;
import com.hrhelpdesk.dao.NotificationDAO;

import com.hrhelpdesk.model.TicketResponse;
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

    public void insertPromotionTransferTicket(int ticketId, String requestType, String reason, Integer targetDeptId) throws SQLException {
        String sql = "INSERT INTO PROMOTION_TRANSFER_TICKET (ticket_id, request_type, reason, target_dept_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, requestType);
            ps.setString(3, reason);
            if (targetDeptId != null) {
                ps.setInt(4, targetDeptId);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
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
        String sql = "SELECT t.*, tc.category_name, u.username AS submitted_by_username " +
                "FROM TICKET t " +
                "JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u ON t.submitted_by = u.user_id " +
                "WHERE t.submitted_by = ? ORDER BY t.created_at DESC";
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
                    ticket.setSubmittedByUsername(rs.getString("submitted_by_username"));
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

    private Ticket extractTicket(ResultSet rs) throws SQLException {
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
        ticket.setSubmittedByUsername(rs.getString("submitted_by_username"));
        ticket.setAssignedToUsername(rs.getString("assigned_to_username"));
        return ticket;
    }



    public List<Ticket> getAllOpenTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.status = 'Open'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        }
        return tickets;
    }


    public List<Ticket> getAllInProgressTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.status = 'In Progress'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        }
        return tickets;
    }

    public Ticket getTicketById(int ticketId) throws SQLException {
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractTicket(rs);
                }
            }
        }
        return null;
    }

    public void assignTicket(int ticketId, int assignedTo) throws SQLException {
        String sql = "UPDATE TICKET SET assigned_to = ?, status = 'In Progress', updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ? AND status = 'Open'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignedTo);
            ps.setInt(2, ticketId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Failed to assign ticket.");
            }
        }
    }

    public void updateTicketStatus(int ticketId, String status) throws SQLException {
        String closedAt = (status.equals("Resolved") || status.equals("Closed")) ? ", closed_at = CURRENT_TIMESTAMP" : "";
        String sql = "UPDATE TICKET SET status = ?, updated_at = CURRENT_TIMESTAMP" + closedAt + " WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, ticketId);
            ps.executeUpdate();
        }
    }


    private void createNotificationForResponse(int ticketId, int respondedBy, String message) throws SQLException {
        // First, get the ticket submitter
        String getSubmitterSql = "SELECT submitted_by FROM TICKET WHERE ticket_id = ?";
        int submitterId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getSubmitterSql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    submitterId = rs.getInt("submitted_by");
                }
            }
        }
        if (submitterId == -1) return;

        // Check if submitter is an employee (role_id=1)
        String getRoleSql = "SELECT role_id FROM [USER] WHERE user_id = ?";
        int roleId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getRoleSql)) {
            ps.setInt(1, submitterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    roleId = rs.getInt("role_id");
                }
            }
        }
        if (roleId != 1) return; // Only notify employees

        // Insert notification (truncate message if too long)
        String truncatedMessage = message.length() > 100 ? message.substring(0, 100) + "..." : message;
        String notificationSql = "INSERT INTO NOTIFICATION (user_id, message, type, is_read) VALUES (?, ?, 'TICKET_RESPONSE', 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(notificationSql)) {
            ps.setInt(1, submitterId);
            ps.setString(2, "New response on Ticket #" + ticketId + ": " + truncatedMessage);
            ps.executeUpdate();
        }
    }

    public void addTicketResponse(int ticketId, String message, int respondedBy) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "INSERT INTO TICKET_RESPONSE (ticket_id, message, responded_by) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, ticketId);
                ps.setString(2, message);
                ps.setInt(3, respondedBy);
                ps.executeUpdate();
            }
            String updateSql = "UPDATE TICKET SET updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, ticketId);
                ps.executeUpdate();
            }
            // Create notification for the submitter
            createNotificationForResponse(ticketId, respondedBy, message);
        } finally {
            conn.close();
        }
    }

    public List<TicketResponse> getTicketResponses(int ticketId) throws SQLException {
        List<TicketResponse> responses = new ArrayList<>();
        String sql = "SELECT tr.*, u.username AS responded_by_username " +
                "FROM TICKET_RESPONSE tr JOIN [USER] u ON tr.responded_by = u.user_id " +
                "WHERE tr.ticket_id = ? ORDER BY tr.created_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TicketResponse resp = new TicketResponse();
                    resp.setResponseId(rs.getInt("response_id"));
                    resp.setTicketId(rs.getInt("ticket_id"));
                    resp.setMessage(rs.getString("message"));
                    resp.setCreatedAt(rs.getTimestamp("created_at"));
                    resp.setRespondedBy(rs.getInt("responded_by"));
                    resp.setRespondedByUsername(rs.getString("responded_by_username"));
                    responses.add(resp);
                }
            }
        }
        return responses;
    }

    public List<TicketResponse> getResponsesBySubmitter(String submittedByUsername) throws SQLException {
        List<TicketResponse> responses = new ArrayList<>();
        String sql = "SELECT tr.* FROM TICKET_RESPONSE tr JOIN TICKET t ON tr.ticket_id = t.ticket_id WHERE t.submitted_by_username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, submittedByUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TicketResponse response = new TicketResponse();
                    response.setTicketId(rs.getInt("ticket_id"));
                    response.setMessage(rs.getString("message"));
                    response.setRespondedByUsername(rs.getString("responded_by_username"));
                    response.setCreatedAt(rs.getTimestamp("created_at"));
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    public List<Ticket> getTicketsByAssignedTo(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.assigned_to = ? AND t.status NOT IN ('Resolved', 'Closed', 'Cancelled')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(extractTicket(rs));
                }
            }
        }
        return tickets;
    }

    public void escalateTicket(int ticketId, int managerId, String note, int staffId) throws SQLException {
        assignTicket(ticketId, managerId);
        addTicketResponse(ticketId, note, staffId);
    }

    public void updateTicket(Ticket ticket) throws SQLException {
        String sql = "UPDATE TICKET SET title = ?, description = ?, priority = ?, updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ? AND submitted_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getDescription());
            ps.setString(3, ticket.getPriority());
            ps.setInt(4, ticket.getTicketId());
            ps.setInt(5, ticket.getSubmittedBy());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Ticket not found or does not belong to user.");
            }
        }
    }

    public void addAttachmentToTicket(int ticketId, String fileName, InputStream fileData) throws SQLException {
        String sql = "INSERT INTO TICKET_ATTACHMENT (ticket_id, file_name, file_data) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setString(2, fileName);
            ps.setBlob(3, fileData);
            ps.executeUpdate();
        }
    }

    public boolean isWithin24Hours(int ticketId) throws SQLException {
        String sql = "SELECT created_at FROM TICKET WHERE ticket_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    LocalDateTime ticketTime = createdAt.toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();
                    return now.isBefore(ticketTime.plusHours(24));
                }
            }
        }
        return false;
    }

    public List<Ticket> getAllResolvedTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT t.ticket_id, t.title, t.description, t.status, t.priority, t.created_at, t.updated_at, t.closed_at, " +
                "t.submitted_by, t.assigned_to, t.category_id, c.category_name, " +
                "u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t " +
                "LEFT JOIN TICKET_CATEGORY c ON t.category_id = c.category_id " +
                "LEFT JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.status = 'Resolved'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
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
                ticket.setAssignedTo(rs.getInt("assigned_to"));
                ticket.setCategoryId(rs.getInt("category_id"));
                ticket.setCategoryName(rs.getString("category_name"));
                ticket.setSubmittedByUsername(rs.getString("submitted_by_username"));
                ticket.setAssignedToUsername(rs.getString("assigned_to_username"));
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public int getUnassignedOpenTicketsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM TICKET WHERE status = 'Open' AND assigned_to IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Ticket> getResolvedTicketsByAssignedTo(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.assigned_to = ? AND t.status = 'Resolved'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(extractTicket(rs));
                }
            }
        }
        return tickets;
    }

    public List<Ticket> getAssignedTickets(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id " +
                "WHERE t.assigned_to = ? AND t.status = 'In Progress'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(extractTicket(rs));
                }
            }
        }
        return tickets;
    }


}
