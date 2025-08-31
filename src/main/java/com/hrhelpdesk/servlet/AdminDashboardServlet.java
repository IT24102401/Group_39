package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.DepartmentDAO;
import com.hrhelpdesk.dao.RoleDAO;
import com.hrhelpdesk.dao.UserDAO;
import com.hrhelpdesk.model.Department;
import com.hrhelpdesk.model.Role;
import com.hrhelpdesk.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final List<String> JOB_TITLES = Arrays.asList("Executive Board", "Manager", "Secretary", "Team Leader", "Staff", "Intern");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {  // Check if admin
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            RoleDAO roleDAO = new RoleDAO();
            DepartmentDAO deptDAO = new DepartmentDAO();
            List<User> activeUsers = userDAO.getActiveUsers();
            List<User> removedUsers = userDAO.getRemovedUsers();
            for (User removedUser : removedUsers) {
                if (removedUser.getDeletedBy() != 0) {
                    User deleter = userDAO.getUserById(removedUser.getDeletedBy());
                    removedUser.setDeletedByUsername(deleter != null ? deleter.getUsername() : "Unknown");
                } else {
                    removedUser.setDeletedByUsername("Unknown");
                }
            }
            List<Role> roles = roleDAO.getAllRoles();
            List<Department> departments = deptDAO.getAllDepartments();
            request.setAttribute("activeUsers", activeUsers);
            request.setAttribute("removedUsers", removedUsers);
            request.setAttribute("roles", roles);
            request.setAttribute("departments", departments);
            request.setAttribute("jobTitles", JOB_TITLES);
            request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error occurred. Please try again.");
            request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO(); // Declare userDAO here to ensure it's accessible in catch block

        try {
            if ("search".equals(action)) {
                String employeeId = request.getParameter("employee_id");
                User searchedUser = userDAO.getUserByEmployeeId(employeeId);
                if (searchedUser != null && !searchedUser.isDeleted()) {
                    request.setAttribute("searchedUser", searchedUser);
                } else {
                    request.setAttribute("errorMessage", "No active user found with Employee ID: " + employeeId);
                }
                // Reload active users, removed users, roles, and departments
                RoleDAO roleDAO = new RoleDAO();
                DepartmentDAO deptDAO = new DepartmentDAO();
                List<User> activeUsers = userDAO.getActiveUsers();
                List<User> removedUsers = userDAO.getRemovedUsers();
                for (User removedUser : removedUsers) {
                    if (removedUser.getDeletedBy() != 0) {
                        User deleter = userDAO.getUserById(removedUser.getDeletedBy());
                        removedUser.setDeletedByUsername(deleter != null ? deleter.getUsername() : "Unknown");
                    } else {
                        removedUser.setDeletedByUsername("Unknown");
                    }
                }
                List<Role> roles = roleDAO.getAllRoles();
                List<Department> departments = deptDAO.getAllDepartments();
                request.setAttribute("activeUsers", activeUsers);
                request.setAttribute("removedUsers", removedUsers);
                request.setAttribute("roles", roles);
                request.setAttribute("departments", departments);
                request.setAttribute("jobTitles", JOB_TITLES);
                request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
            } else if ("remove".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("user_id"));
                String reason = request.getParameter("reason");
                userDAO.removeUser(userId, reason, currentUser.getUserId());
                response.sendRedirect("adminDashboard");
            } else if ("update".equals(action)) {
                // Handle update user
                int userId = Integer.parseInt(request.getParameter("user_id"));
                User updatedUser = userDAO.getUserById(userId);
                if (updatedUser != null && !updatedUser.isDeleted()) {
                    updatedUser.setFirstName(request.getParameter("first_name"));
                    updatedUser.setLastName(request.getParameter("last_name"));
                    updatedUser.setEmail(request.getParameter("email"));
                    String deptIdStr = request.getParameter("dept_id");
                    if (deptIdStr != null && !deptIdStr.isEmpty()) {
                        updatedUser.setDeptId(Integer.parseInt(deptIdStr));
                    } else {
                        updatedUser.setDeptId(null);
                    }
                    updatedUser.setJobTitle(request.getParameter("job_title"));
                    updatedUser.setPhoneNumbers(request.getParameter("phone_numbers"));
                    updatedUser.setAddress(request.getParameter("address"));
                    updatedUser.setRoleId(Integer.parseInt(request.getParameter("role_id")));
                    userDAO.updateUser(updatedUser);
                    response.sendRedirect("adminDashboard");
                } else {
                    request.setAttribute("errorMessage", "User not found or already deleted.");
                    reloadDashboardData(request, userDAO);
                    request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
                }
            } else {
                // Handle add user
                User newUser = new User();
                newUser.setUsername(request.getParameter("username"));
                newUser.setEmail(request.getParameter("email"));
                newUser.setFirstName(request.getParameter("first_name"));
                newUser.setLastName(request.getParameter("last_name"));
                String deptIdStr = request.getParameter("dept_id");
                if (deptIdStr != null && !deptIdStr.isEmpty()) {
                    newUser.setDeptId(Integer.parseInt(deptIdStr));
                }
                newUser.setJobTitle(request.getParameter("job_title"));
                newUser.setPhoneNumbers(request.getParameter("phone_numbers"));
                newUser.setAddress(request.getParameter("address"));
                newUser.setRoleId(Integer.parseInt(request.getParameter("role_id")));
                String plainPassword = request.getParameter("password");
                userDAO.addUser(newUser, plainPassword);
                response.sendRedirect("adminDashboard");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage().contains("Department already has a Manager") ?
                    "Cannot update/add user: Department already has a Manager." :
                    e.getMessage().contains("Management role cannot be assigned") ?
                            "Cannot update/add user: Management role cannot be assigned to a department." :
                            "Database error occurred. Please try again.";
            request.setAttribute("errorMessage", errorMessage);
            try {
                reloadDashboardData(request, userDAO);
            } catch (SQLException ex) {
                ex.printStackTrace();
                request.setAttribute("errorMessage", "Error reloading data: " + ex.getMessage());
            }
            request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
        }
    }

    private void reloadDashboardData(HttpServletRequest request, UserDAO userDAO) throws SQLException {
        RoleDAO roleDAO = new RoleDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();
        List<User> activeUsers = userDAO.getActiveUsers();
        List<User> removedUsers = userDAO.getRemovedUsers();
        List<Role> roles = roleDAO.getAllRoles();
        List<Department> departments = deptDAO.getAllDepartments();
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("removedUsers", removedUsers);
        request.setAttribute("roles", roles);
        request.setAttribute("departments", departments);
        request.setAttribute("jobTitles", JOB_TITLES);
    }
}