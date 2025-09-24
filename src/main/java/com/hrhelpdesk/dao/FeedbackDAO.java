package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Feedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    public void submitFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO FEEDBACK (message, rating, user_id, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, feedback.getMessage());
            ps.setInt(2, feedback.getRating());
            ps.setInt(3, feedback.getUserId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    feedback.setFeedbackId(rs.getInt(1));
                }
            }
        }
    }

    public List<Feedback> getFeedbackByUserId(int userId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM FEEDBACK WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setFeedbackId(rs.getInt("feedback_id"));
                    feedback.setMessage(rs.getString("message"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setUserId(rs.getInt("user_id"));
                    feedback.setCreatedAt(rs.getTimestamp("created_at"));
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }
}