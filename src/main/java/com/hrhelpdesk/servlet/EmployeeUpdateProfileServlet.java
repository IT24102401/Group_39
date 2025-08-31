package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.UserDAO;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/EmployeeUpdateProfile")
public class EmployeeUpdateProfileServlet extends HttpServlet {
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

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user);
            session.setAttribute("user", user); // Update session with new details
            request.setAttribute("success", "Profile updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage().contains("Management role cannot be assigned") ?
                    "Cannot update profile: Management role cannot be assigned to a department." :
                    "Failed to update profile: " + e.getMessage();
            request.setAttribute("error", errorMessage);
        }

        request.getRequestDispatcher("/jsp/employeeDashboard.jsp").forward(request, response);
    }
}