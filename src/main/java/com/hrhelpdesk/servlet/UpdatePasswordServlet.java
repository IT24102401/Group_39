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

@WebServlet("/EmployeeUpdatePassword")
public class UpdatePasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "New passwords do not match or are invalid.");
            response.sendRedirect(getRedirectURL(user));
            return;
        }

        if (!isValidPassword(newPassword)) {
            session.setAttribute("error", "Password must be at least 8 characters long and contain at least one letter and one number.");
            response.sendRedirect(getRedirectURL(user));
            return;
        }

        try {
            User dbUser = userService.validateUser(user.getUsername(), currentPassword);
            if (dbUser == null) {
                session.setAttribute("error", "Current password is incorrect.");
                response.sendRedirect(getRedirectURL(user));
                return;
            }

            userService.updatePassword(user.getUserId(), newPassword);
            session.setAttribute("success", "Password updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            session.setAttribute("error", "Failed to update password due to a server error.");
        }

        response.sendRedirect(getRedirectURL(user));
    }

    private String getRedirectURL(User user) {
        return user != null && user.getRoleId() == 3 ? "hrManagerDashboard" : "employeeDashboard";
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
}