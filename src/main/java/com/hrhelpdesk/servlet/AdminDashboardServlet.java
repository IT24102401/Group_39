package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.DepartmentDAO;
import com.hrhelpdesk.dao.RoleDAO;
import com.hrhelpdesk.service.UserService;
import com.hrhelpdesk.model.*;
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
import java.util.ArrayList;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final List<String> JOB_TITLES = Arrays.asList("Executive Board", "Manager", "Secretary", "Team Leader", "Staff", "Intern");
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }
        try {
            List<User> activeUsers = userService.getActiveUsers();
            List<User> removedUsers = userService.getRemovedUsers();
            for (User removedUser : removedUsers) {
                if (removedUser.getDeletedBy() != 0) {
                    User deleter = userService.getUserById(removedUser.getDeletedBy());
                    removedUser.setDeletedByUsername(deleter != null ? deleter.getUsername() : "Unknown");
                } else {
                    removedUser.setDeletedByUsername("Unknown");
                }
            }
            RoleDAO roleDAO = new RoleDAO();
            DepartmentDAO deptDAO = new DepartmentDAO();
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
        if (currentUser == null || currentUser.getRoleId() != 4) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }
        try {
            if ("search".equals(action)) {
                String employeeId = request.getParameter("employee_id");
                User searchedUser = userService.getUserByEmployeeId(employeeId);
                if (searchedUser != null && !searchedUser.isDeleted()) {
                    request.setAttribute("searchedUser", searchedUser);
                } else {
                    request.setAttribute("errorMessage", "No active user found with Employee ID: " + employeeId);
                }
                reloadDashboardData(request);
                request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
            } else if ("remove".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("user_id"));
                String reason = request.getParameter("reason");
                userService.removeUser(userId, reason, currentUser.getUserId());
                response.sendRedirect("adminDashboard");
            } else if ("update".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("user_id"));
                User updatedUser = userService.getUserById(userId);
                if (updatedUser != null && !updatedUser.isDeleted()) {
                    updatedUser.setFirstName(request.getParameter("first_name"));
                    updatedUser.setLastName(request.getParameter("last_name"));
                    updatedUser.setEmail(request.getParameter("email"));
                    String deptIdStr = request.getParameter("dept_id");
                    updatedUser.setDeptId(deptIdStr != null && !deptIdStr.isEmpty() ? Integer.parseInt(deptIdStr) : null);
                    updatedUser.setJobTitle(request.getParameter("job_title"));
                    String phoneNumbersInput = request.getParameter("phone_numbers");
                    updatedUser.setPhoneNumbers(phoneNumbersInput != null && !phoneNumbersInput.isEmpty() ?
                            Arrays.asList(phoneNumbersInput.split("\\s*,\\s*")) : new ArrayList<>());
                    updatedUser.setAddress(request.getParameter("address"));
                    updatedUser.setRoleId(Integer.parseInt(request.getParameter("role_id")));
                    userService.updateUser(updatedUser);
                    response.sendRedirect("adminDashboard");
                } else {
                    request.setAttribute("errorMessage", "User not found or already deleted.");
                    reloadDashboardData(request);
                    request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
                }
            } else {
                int roleId = Integer.parseInt(request.getParameter("role_id"));
                User newUser = createUserInstance(roleId);
                newUser.setUsername(request.getParameter("username"));
                newUser.setEmail(request.getParameter("email"));
                newUser.setFirstName(request.getParameter("first_name"));
                newUser.setLastName(request.getParameter("last_name"));
                String deptIdStr = request.getParameter("dept_id");
                newUser.setDeptId(deptIdStr != null && !deptIdStr.isEmpty() ? Integer.parseInt(deptIdStr) : null);
                newUser.setJobTitle(request.getParameter("job_title"));
                String phoneNumbersInput = request.getParameter("phone_numbers");
                newUser.setPhoneNumbers(phoneNumbersInput != null && !phoneNumbersInput.isEmpty() ?
                        Arrays.asList(phoneNumbersInput.split("\\s*,\\s*")) : new ArrayList<>());
                newUser.setAddress(request.getParameter("address"));
                newUser.setRoleId(roleId);
                String plainPassword = request.getParameter("password");
                userService.addUser(newUser, plainPassword);
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
                reloadDashboardData(request);
            } catch (SQLException ex) {
                ex.printStackTrace();
                request.setAttribute("errorMessage", "Error reloading data: " + ex.getMessage());
            }
            request.getRequestDispatcher("jsp/adminDashboard.jsp").forward(request, response);
        }
    }

    private void reloadDashboardData(HttpServletRequest request) throws SQLException {
        List<User> activeUsers = userService.getActiveUsers();
        List<User> removedUsers = userService.getRemovedUsers();
        RoleDAO roleDAO = new RoleDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();
        List<Role> roles = roleDAO.getAllRoles();
        List<Department> departments = deptDAO.getAllDepartments();
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("removedUsers", removedUsers);
        request.setAttribute("roles", roles);
        request.setAttribute("departments", departments);
        request.setAttribute("jobTitles", JOB_TITLES);
    }

    private User createUserInstance(int roleId) {
        switch (roleId) {
            case 1: return new Employee();
            case 2: return new HRStaff();
            case 3: return new HRManager();
            case 4: return new Admin();
            case 5: return new CompanyManagement();
            default: throw new IllegalArgumentException("Unknown role ID: " + roleId);
        }
    }
}

