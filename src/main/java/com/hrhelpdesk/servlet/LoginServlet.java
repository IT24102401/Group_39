package com.hrhelpdesk.servlet;

import com.hrhelpdesk.service.UserService;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.validateUser(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                int roleId = user.getRoleId();
                if (roleId == 4) { // Admin
                    response.sendRedirect(request.getContextPath() + "/adminDashboard");
                } else if (roleId == 3) { // HR Manager
                    response.sendRedirect(request.getContextPath() + "/hrManagerDashboard");
                } else if (roleId == 1) { // Employee
                    response.sendRedirect(request.getContextPath() + "/employeeDashboard");
                } else if (roleId == 2) { // HR Staff
                    response.sendRedirect(request.getContextPath() + "/hrStaffDashboard");
                } else if (roleId == 5) { // Management
                    response.sendRedirect(request.getContextPath() + "/companyManagementDashboard");
                } else {
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
                }
            } else {
                request.setAttribute("error", "Invalid credentials");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/error.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}