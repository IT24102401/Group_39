package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Added import to resolve 'Statement'
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User getUserByEmployeeId(String employeeId) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.employee_id = ? AND (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    public User validateUser(String username, String password) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.username = ? AND (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return extractUser(rs);
                    }
                }
            }
        }
        return null;
    }

    public String generateEmployeeId() throws SQLException {
        String sql = "SELECT MAX(user_id) AS max_id FROM [USER]";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt("max_id");
            }
            String newId = "EM" + String.format("%04d", maxId + 1);
            String checkSql = "SELECT COUNT(*) FROM [USER] WHERE employee_id = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, newId);
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        return generateEmployeeId(); // Recursive call to ensure unique ID
                    }
                }
            }
            return newId;
        }
    }

    public boolean hasManagerInDepartment(Integer deptId) throws SQLException {
        if (deptId == null) return false;
        String sql = "SELECT COUNT(*) FROM [USER] WHERE dept_id = ? AND job_title = 'Manager' AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean hasManagerInDepartment(Integer deptId, int excludeUserId) throws SQLException {
        if (deptId == null) return false;
        String sql = "SELECT COUNT(*) FROM [USER] WHERE dept_id = ? AND job_title = 'Manager' AND user_id != ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void addUser(User user, String plainPassword) throws SQLException {
        // Check if job_title is Manager and department already has a Manager
        if ("Manager".equalsIgnoreCase(user.getJobTitle()) && user.getDeptId() != null) {
            if (hasManagerInDepartment(user.getDeptId())) {
                throw new SQLException("Department already has a Manager.");
            }
        }
        // For Management role (role_id = 5), ensure dept_id is null
        if (user.getRoleId() == 5 && user.getDeptId() != null) {
            throw new SQLException("Users with Management role cannot be assigned to a department.");
        }

        String sql = "INSERT INTO [USER] (employee_id, username, password_hash, email, first_name, last_name, dept_id, job_title, phone_numbers, address, role_id, created_at, is_deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmployeeId() != null ? user.getEmployeeId() : generateEmployeeId());
            ps.setString(2, user.getUsername());
            ps.setString(3, BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getFirstName());
            ps.setString(6, user.getLastName());
            if (user.getDeptId() != null) {
                ps.setInt(7, user.getDeptId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setString(8, user.getJobTitle());
            ps.setString(9, user.getPhoneNumbers());
            ps.setString(10, user.getAddress());
            ps.setInt(11, user.getRoleId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
        }
    }

    public List<User> getActiveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.is_deleted = 0 OR u.is_deleted IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        }
        return users;
    }

    public List<User> getRemovedUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.is_deleted = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        }
        return users;
    }

    public void removeUser(int userId, String reason, int deletedBy) throws SQLException {
        String sql = "UPDATE [USER] SET is_deleted = 1, deleted_at = GETDATE(), deletion_reason = ?, deleted_by = ? " +
                "WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setInt(2, deletedBy);
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("User not found or already deleted.");
            }
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u " +
                "JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    public void updateUser(User user) throws SQLException {
        // Check if job_title is Manager and department already has a Manager (excluding this user)
        if ("Manager".equalsIgnoreCase(user.getJobTitle()) && user.getDeptId() != null) {
            if (hasManagerInDepartment(user.getDeptId(), user.getUserId())) {
                throw new SQLException("Department already has a Manager.");
            }
        }
        // For Management role (role_id = 5), ensure dept_id is null
        if (user.getRoleId() == 5 && user.getDeptId() != null) {
            throw new SQLException("Users with Management role cannot be assigned to a department.");
        }

        String sql = "UPDATE [USER] SET first_name = ?, last_name = ?, email = ?, dept_id = ?, job_title = ?, " +
                "phone_numbers = ?, address = ?, role_id = ? " +
                "WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            if (user.getDeptId() != null) {
                ps.setInt(4, user.getDeptId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, user.getJobTitle());
            ps.setString(6, user.getPhoneNumbers());
            ps.setString(7, user.getAddress());
            ps.setInt(8, user.getRoleId());
            ps.setInt(9, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("User not found or already deleted.");
            }
        }
    }

    public void updateUserProfile(User user) throws SQLException {
        // Only update fields editable by employees
        String sql = "UPDATE [USER] SET first_name = ?, last_name = ?, email = ? " +
                "WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("User not found or already deleted.");
            }
        }
    }

    public void updatePassword(int userId, String newPlainPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(newPlainPassword, BCrypt.gensalt());
        String sql = "UPDATE [USER] SET password_hash = ? WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("User not found or already deleted.");
            }
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmployeeId(rs.getString("employee_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setDeptId(rs.getObject("dept_id") != null ? rs.getInt("dept_id") : null);
        user.setDeptName(rs.getString("dept_name"));
        user.setJobTitle(rs.getString("job_title"));
        user.setPhoneNumbers(rs.getString("phone_numbers"));
        user.setAddress(rs.getString("address"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setDeleted(rs.getBoolean("is_deleted"));
        user.setDeletedAt(rs.getTimestamp("deleted_at"));
        user.setDeletionReason(rs.getString("deletion_reason"));
        user.setDeletedBy(rs.getInt("deleted_by"));
        user.setDeletedByUsername(rs.getString("deleted_by_username") != null ? rs.getString("deleted_by_username") : "Unknown");
        return user;
    }
}