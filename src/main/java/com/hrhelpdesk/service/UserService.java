package com.hrhelpdesk.service;

import com.hrhelpdesk.dao.AdminUserDAO;
import com.hrhelpdesk.dao.BaseUserDAO;
import com.hrhelpdesk.dao.EmployeeUserDAO;
import com.hrhelpdesk.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private AdminUserDAO adminDAO = new AdminUserDAO();
    private EmployeeUserDAO empDAO = new EmployeeUserDAO();

    public BaseUserDAO getDAOForUser(User user) {
        return switch (user.getRoleId()) {
            case 1 -> empDAO; // Employee (main user)
            case 2, 3, 4 -> adminDAO; // HR Staff, Manager, Admin
            case 5 -> adminDAO; // Company Management (view-only)
            default -> throw new IllegalArgumentException("Unknown role");
        };
    }

    public User getUserById(int userId) throws SQLException {
        return adminDAO.getUserById(userId); // Common
    }

    public User validateUser(String username, String password) throws SQLException {
        return adminDAO.validateUser(username, password); // Common
    }

    public void addUser(User user, String plainPassword) throws SQLException {
        adminDAO.addUser(user, plainPassword);
    }

    public void updateUser(User user) throws SQLException {
        adminDAO.updateUser(user);
    }

    public void removeUser(int userId, String reason, int deletedBy) throws SQLException {
        adminDAO.removeUser(userId, reason, deletedBy);
    }

    public void updateUserProfile(User user) throws SQLException {
        empDAO.updateUserProfile(user);
    }

    public void updatePassword(int userId, String newPlainPassword) throws SQLException {
        adminDAO.updatePassword(userId, newPlainPassword);
    }

    public List<User> getActiveUsers() throws SQLException {
        return adminDAO.getActiveUsers();
    }

    public List<User> getRemovedUsers() throws SQLException {
        return adminDAO.getRemovedUsers();
    }

    public List<User> getUsersByRole(int roleId) throws SQLException {
        return adminDAO.getUsersByRole(roleId);
    }

    public User getUserByEmployeeId(String employeeId) throws SQLException {
        return adminDAO.getUserByEmployeeId(employeeId);
    }
}