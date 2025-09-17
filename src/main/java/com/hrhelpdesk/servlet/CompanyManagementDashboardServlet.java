package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.CompanyManagementUserDAO;
import com.hrhelpdesk.model.Feedback;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/companyManagementDashboard")
public class CompanyManagementDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 5) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            CompanyManagementUserDAO dao = new CompanyManagementUserDAO();
            // Fetch all users
            List<User> users = dao.getAllUsers();
            request.setAttribute("users", users);

            // Fetch tickets by category
            Map<String, List<Ticket>> ticketsByCategory = dao.getTicketsByCategory();
            request.setAttribute("ticketsByCategory", ticketsByCategory);

            // Fetch feedback
            List<Feedback> feedbackList = dao.getAllFeedback();
            request.setAttribute("feedbackList", feedbackList);

            // Fetch performance metrics
            Map<String, Double> performanceMetrics = dao.getPerformanceMetrics();
            request.setAttribute("performanceMetrics", performanceMetrics);

        } catch (SQLException e) {
            session.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            response.sendRedirect("error.jsp");
            return;
        }

        request.getRequestDispatcher("/jsp/companyManagementDashboard.jsp").forward(request, response);
    }
}
