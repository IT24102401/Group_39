package com.hrhelpdesk.servlet;

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

@WebServlet("/EmployeeUpdateProfile")
public class UpdateProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String userIdParam = request.getParameter("userId");

        if (!isValidInput(firstName, lastName, email)) {
            session.setAttribute("error", "Invalid input: First name, last name, and a valid email are required.");
            response.sendRedirect(getRedirectURL(user));
            return;
        }

        User updateUser = user; // Default to the logged-in user
        int userId = user.getUserId();
        if (user.getRoleId() == 3 && userIdParam != null && !userIdParam.isEmpty()) {
            try {
                userId = Integer.parseInt(userIdParam);
                // Fetch the target user, handling SQLException
                updateUser = userService.getUserById(userId);
                if (updateUser == null || updateUser.isDeleted()) {
                    session.setAttribute("error", "User not found or already deleted.");
                    response.sendRedirect(getRedirectURL(user));
                    return;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid user ID for update.");
                response.sendRedirect(getRedirectURL(user));
                return;
            } catch (SQLException e) {
                session.setAttribute("error", "Database error while fetching user: " + e.getMessage());
                response.sendRedirect(getRedirectURL(user));
                return;
            }
        }

        updateUser.setFirstName(firstName);
        updateUser.setLastName(lastName);
        updateUser.setEmail(email);

        try {
            userService.updateUserProfile(updateUser);
            if (userId == user.getUserId()) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                session.setAttribute("user", user);
            }
            session.setAttribute("success", "Profile updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            String errorMessage = e.getMessage().contains("Management role cannot be assigned") ?
                    "Cannot update profile: Management role cannot be assigned to a department." :
                    "Failed to update profile due to a server error.";
            session.setAttribute("error", errorMessage);
        }

        response.sendRedirect(getRedirectURL(user));
    }

    private String getRedirectURL(User user) {
        return user != null && user.getRoleId() == 3 ? "hrManagerDashboard" : "employeeDashboard";
    }

    private boolean isValidInput(String firstName, String lastName, String email) {
        return firstName != null && !firstName.trim().isEmpty() && lastName != null && !lastName.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}