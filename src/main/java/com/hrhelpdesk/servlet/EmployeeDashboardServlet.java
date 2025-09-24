package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.NotificationDAO;
import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.dao.FeedbackDAO;
import com.hrhelpdesk.model.User;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.Feedback;
import com.hrhelpdesk.model.Notification;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/employeeDashboard")
public class EmployeeDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRoleId() != 1) {
            session.setAttribute("error", "Unauthorized access.");
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        TicketDAO ticketDAO = new TicketDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        try {
            // Log user ID for debugging
            System.out.println("Fetching data for user ID: " + user.getUserId());

            // Fetch tickets for the user
            List<Ticket> tickets = ticketDAO.getTicketsByUser(user.getUserId());
            request.setAttribute("tickets", tickets);

            // Fetch attachment names for each ticket
            for (Ticket ticket : tickets) {
                List<String> attachmentNames = ticketDAO.getAttachmentNames(ticket.getTicketId());
                request.setAttribute("attachmentNames_" + ticket.getTicketId(), attachmentNames);
            }

            // Fetch feedback for the user
            List<Feedback> feedbacks = feedbackDAO.getFeedbackByUserId(user.getUserId());
            request.setAttribute("feedbacks", feedbacks);

            // Fetch notifications for the user
            List<Notification> notifications = notificationDAO.getNotificationsByUser(user.getUserId());
            request.setAttribute("notifications", notifications);

            // Fetch unread notification count
            int unreadCount = notificationDAO.getUnreadNotificationCount(user.getUserId());
            request.setAttribute("unreadNotificationCount", unreadCount);

        } catch (SQLException e) {
            System.err.println("SQLException in EmployeeDashboardServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/employeeDashboard.jsp").forward(request, response);
    }
}