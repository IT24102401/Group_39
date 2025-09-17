package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Feedback;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyManagementUserDAO extends BaseUserDAO {

    // Fetch all users (active and removed)
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        }
        return users;
    }

    // Fetch tickets grouped by category
    public Map<String, List<Ticket>> getTicketsByCategory() throws SQLException {
        Map<String, List<Ticket>> ticketsByCategory = new HashMap<>();
        String sql = "SELECT t.*, tc.category_name, u1.username AS submitted_by_username, u2.username AS assigned_to_username " +
                "FROM TICKET t " +
                "JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "JOIN [USER] u1 ON t.submitted_by = u1.user_id " +
                "LEFT JOIN [USER] u2 ON t.assigned_to = u2.user_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setTitle(rs.getString("title"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setPriority(rs.getString("priority"));
                ticket.setSubmittedBy(rs.getInt("submitted_by"));
                ticket.setSubmittedByUsername(rs.getString("submitted_by_username"));
                ticket.setAssignedTo(rs.getInt("assigned_to"));
                ticket.setAssignedToUsername(rs.getString("assigned_to_username"));
                ticket.setCategoryId(rs.getInt("category_id"));
                ticket.setCategoryName(rs.getString("category_name"));
                ticket.setCreatedAt(rs.getTimestamp("created_at"));
                ticket.setUpdatedAt(rs.getTimestamp("updated_at"));
                ticket.setClosedAt(rs.getTimestamp("closed_at"));
                String category = rs.getString("category_name");
                ticketsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(ticket);
            }
        }
        return ticketsByCategory;
    }

    // Fetch all feedback
    public List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, u.username AS submitted_by_username " +
                "FROM FEEDBACK f " +
                "JOIN [USER] u ON f.user_id = u.user_id " +
                "ORDER BY f.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(rs.getInt("feedback_id"));
                feedback.setMessage(rs.getString("message"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setUserId(rs.getInt("user_id"));
                feedback.setSubmittedByUsername(rs.getString("submitted_by_username"));
                feedback.setCreatedAt(rs.getTimestamp("created_at"));
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    // Fetch performance data (average resolution time by category)
    public Map<String, Double> getPerformanceMetrics() throws SQLException {
        Map<String, Double> metrics = new HashMap<>();
        String sql = "SELECT tc.category_name, AVG(DATEDIFF(HOUR, t.created_at, t.closed_at) / 24.0) AS avg_resolution_days " +
                "FROM TICKET t " +
                "JOIN TICKET_CATEGORY tc ON t.category_id = tc.category_id " +
                "WHERE t.status IN ('Resolved', 'Closed') AND t.closed_at IS NOT NULL " +
                "GROUP BY tc.category_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String category = rs.getString("category_name");
                double avgDays = rs.getDouble("avg_resolution_days");
                metrics.put(category, avgDays);
            }
        }
        return metrics;
    }

    @Override
    public void saveUser(User user, String plainPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        String sql = "INSERT INTO [USER] (username, password_hash, email, first_name, last_name, job_title, dept_id, role_id, employee_id, phone_numbers, address, created_at, is_deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getJobTitle());
            ps.setObject(7, user.getDeptId(), Types.INTEGER);
            ps.setInt(8, user.getRoleId());
            ps.setString(9, user.getEmployeeId());
            ps.setString(10, user.getPhoneNumbers());
            ps.setString(11, user.getAddress());
            ps.executeUpdate();
        }
    }
}
