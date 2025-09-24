// Updated BaseUserDAO.java with getHRManagerId method
package com.hrhelpdesk.dao;

import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseUserDAO {
    protected User extractUser(ResultSet rs) throws SQLException {
        int roleId = rs.getInt("role_id");
        User user;
        switch (roleId) {
            case 1: user = new Employee(); break;
            case 2: user = new HRStaff(); break;
            case 3: user = new HRManager(); break;
            case 4: user = new Admin(); break;
            case 5: user = new CompanyManagement(); break;
            default: throw new IllegalArgumentException("Unknown role ID: " + roleId);
        }
        user.setUserId(rs.getInt("user_id"));
        user.setEmployeeId(rs.getString("employee_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setDeptId(rs.getObject("dept_id") != null ? rs.getInt("dept_id") : null);
        user.setJobTitle(rs.getString("job_title"));
        user.setAddress(rs.getString("address"));
        user.setRoleId(roleId);
        user.setRoleName(rs.getString("role_name"));
        user.setDeleted(rs.getBoolean("is_deleted"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setDeletedAt(rs.getTimestamp("deleted_at"));
        user.setDeletionReason(rs.getString("deletion_reason"));
        user.setDeletedBy(rs.getInt("deleted_by"));
        user.setDeletedByUsername(rs.getString("deleted_by_username") != null ? rs.getString("deleted_by_username") : "Unknown");
        user.setDeptName(rs.getString("dept_name"));
        Role roleObj = new Role();
        roleObj.setRoleId(roleId);
        roleObj.setRoleName(rs.getString("role_name"));
        user.setRole(roleObj);
        if (user.getDeptId() != null) {
            Department deptObj = new Department();
            deptObj.setDeptId(user.getDeptId());
            deptObj.setDeptName(rs.getString("dept_name"));
            user.setDepartment(deptObj);
        }
        // Fetch phone numbers
        user.setPhoneNumbers(getPhoneNumbers(user.getUserId()));
        return user;
    }

    protected List<String> getPhoneNumbers(int userId) throws SQLException {
        List<String> phoneNumbers = new ArrayList<>();
        String sql = "SELECT phone_number FROM USER_PHONE_NUMBERS WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    phoneNumbers.add(rs.getString("phone_number"));
                }
            }
        }
        return phoneNumbers;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractUser(rs);
            }
        }
        return null;
    }

    public User getUserByEmployeeId(String employeeId) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.employee_id = ? AND (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractUser(rs);
            }
        }
        return null;
    }

    public User validateUser(String username, String password) throws SQLException {
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.username = ? AND (u.is_deleted = 0 OR u.is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (BCrypt.checkpw(password, storedHash)) return extractUser(rs);
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
            if (rs.next()) maxId = rs.getInt("max_id");
            String newId = "EM" + String.format("%04d", maxId + 1);
            String checkSql = "SELECT COUNT(*) FROM [USER] WHERE employee_id = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, newId);
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next() && checkRs.getInt(1) > 0) return generateEmployeeId();
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
                if (rs.next()) return rs.getInt(1) > 0;
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
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void updatePassword(int userId, String newPlainPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(newPlainPassword, BCrypt.gensalt());
        String sql = "UPDATE [USER] SET password_hash = ? WHERE user_id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            if (ps.executeUpdate() == 0) throw new SQLException("User not found or already deleted.");
        }
    }

    public List<User> getAllHRStaff() throws SQLException {
        List<User> staff = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, d.dept_name, u2.username AS deleted_by_username " +
                "FROM [USER] u JOIN ROLE r ON u.role_id = r.role_id " +
                "LEFT JOIN DEPARTMENT d ON u.dept_id = d.dept_id " +
                "LEFT JOIN [USER] u2 ON u.deleted_by = u2.user_id " +
                "WHERE u.role_id = 2 AND u.is_deleted = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                staff.add(extractUser(rs));
            }
        }
        return staff;
    }

    public int getHRManagerId() throws SQLException {
        String sql = "SELECT user_id FROM [USER] WHERE role_id = 3 AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        throw new SQLException("No HR Manager found.");
    }

    public abstract void saveUser(User user, String plainPassword) throws SQLException;
}