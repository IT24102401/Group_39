package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserDAO extends BaseUserDAO {
    public void addUser(User user, String plainPassword) throws SQLException {
        if ("Manager".equalsIgnoreCase(user.getJobTitle()) && user.getDeptId() != null && hasManagerInDepartment(user.getDeptId())) {
            throw new SQLException("Department already has a Manager.");
        }
        if (user.getRoleId() == 5 && user.getDeptId() != null) {
            throw new SQLException("Users with Management role cannot be assigned to a department.");
        }
        String sql = "INSERT INTO [USER] (employee_id, username, password_hash, email, first_name, last_name, dept_id, job_title, address, role_id, created_at, is_deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmployeeId() != null ? user.getEmployeeId() : generateEmployeeId());
            ps.setString(2, user.getUsername());
            ps.setString(3, BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getFirstName());
            ps.setString(6, user.getLastName());
            if (user.getDeptId() != null) ps.setInt(7, user.getDeptId()); else ps.setNull(7, Types.INTEGER);
            ps.setString(8, user.getJobTitle());
            ps.setString(9, user.getAddress());
            ps.setInt(10, user.getRoleId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) user.setUserId(rs.getInt(1));
            }
            // Insert phone numbers
            insertPhoneNumbers(user.getUserId(), user.getPhoneNumbers());
        }
    }

    public void updateUser(User user) throws SQLException {
        if ("Manager".equalsIgnoreCase(user.getJobTitle()) && user.getDeptId() != null && hasManagerInDepartment(user.getDeptId(), user.getUserId())) {
            throw new SQLException("Department already has a Manager.");
        }
        if (user.getRoleId() == 5 && user.getDeptId() != null) {
            throw new SQLException("Users with Management role cannot be assigned to a department.");
        }
        String sql = "UPDATE [USER] SET first_name = ?, last_name = ?, email = ?, dept_id = ?, job_title = ?, " +
                "address = ?, role_id = ? WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            if (user.getDeptId() != null) ps.setInt(4, user.getDeptId()); else ps.setNull(4, Types.INTEGER);
            ps.setString(5, user.getJobTitle());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getRoleId());
            ps.setInt(8, user.getUserId());
            if (ps.executeUpdate() == 0) throw new SQLException("User not found or already deleted.");
            // Update phone numbers: Delete existing and insert new
            deletePhoneNumbers(user.getUserId());
            insertPhoneNumbers(user.getUserId(), user.getPhoneNumbers());
        }
    }

    private void insertPhoneNumbers(int userId, List<String> phoneNumbers) throws SQLException {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) return;
        String sql = "INSERT INTO USER_PHONE_NUMBERS (user_id, phone_number) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String phoneNumber : phoneNumbers) {
                if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                    ps.setInt(1, userId);
                    ps.setString(2, phoneNumber.trim());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void deletePhoneNumbers(int userId) throws SQLException {
        String sql = "DELETE FROM USER_PHONE_NUMBERS WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void saveUser(User user, String plainPassword) throws SQLException {
        addUser(user, plainPassword);
    }
    public void removeUser(int userId, String reason, int deletedBy) throws SQLException {
        String sql = "UPDATE [USER] SET is_deleted = 1, deleted_at = GETDATE(), deletion_reason = ?, deleted_by = ? " +
                "WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setInt(2, deletedBy);
            ps.setInt(3, userId);
            if (ps.executeUpdate() == 0) throw new SQLException("User not found or already deleted.");
        }
    }

    public List<User> getActiveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(extractUser(rs));
        }
        return users;
    }

    public List<User> getRemovedUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.is_deleted = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(extractUser(rs));
        }
        return users;
    }

    public List<User> getUsersByRole(int roleId) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.role_id = ? AND (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) users.add(extractUser(rs));
            }
        }
        return users;
    }
}