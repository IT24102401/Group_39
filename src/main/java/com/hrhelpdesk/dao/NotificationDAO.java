package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void createNotification(Notification notif) throws SQLException {
        String sql = "INSERT INTO NOTIFICATION (user_id, message, type, is_read, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notif.getUserId());
            ps.setString(2, notif.getMessage());
            ps.setString(3, notif.getType());
            ps.setBoolean(4, notif.getIsRead());
            ps.setTimestamp(5, notif.getCreatedAt());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert notification.");
            }
        }
    }


    public List<Notification> getNotificationsByUser(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATION WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notif = new Notification();
                    notif.setNotificationId(rs.getInt("notification_id"));
                    notif.setUserId(rs.getInt("user_id"));
                    notif.setMessage(rs.getString("message"));
                    notif.setType(rs.getString("type"));
                    notif.setIsRead(rs.getBoolean("is_read"));
                    notif.setCreatedAt(rs.getTimestamp("created_at"));
                    notifications.add(notif);
                }
            }
        }
        return notifications;
    }


    public int getUnreadNotificationCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NOTIFICATION WHERE user_id = ? AND is_read = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Optional: Method to mark a notification as read
    public void markNotificationAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE NOTIFICATION SET is_read = 1 WHERE notification_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to mark notification as read.");
            }
        }
    }
}