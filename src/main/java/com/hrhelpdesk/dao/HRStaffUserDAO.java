package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HRStaffUserDAO extends BaseUserDAO {
    public void updateUserProfile(User user) throws SQLException {
        String sql = "UPDATE [USER] SET first_name = ?, last_name = ?, email = ? " +
                "WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getUserId());
            if (ps.executeUpdate() == 0) throw new SQLException("User not found or already deleted.");
        }
    }

    @Override
    public void saveUser(User user, String plainPassword) throws SQLException {
        updateUserProfile(user); // For HR Staff, save = profile update
    }
}