// New HRStaffUpdateProfileServlet.java
package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.HRStaffUserDAO;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/HRStaffUpdateProfile")
public class HRStaffUpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        HRStaffUserDAO userDAO = new HRStaffUserDAO();
        try {
            userDAO.updateUserProfile(user);
            session.setAttribute("success", "Profile updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        response.sendRedirect("hrStaffDashboard");
    }
}