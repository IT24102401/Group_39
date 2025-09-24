package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.dao.NotificationDAO;
import com.hrhelpdesk.model.Notification;
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
import java.sql.Timestamp;

@WebServlet("/updateTicketStatus")
public class UpdateTicketStatusServlet extends HttpServlet {
    private static final String[] VALID_STATUSES = {"Open", "In Progress", "Wait Listed", "Resolved"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 3) {
            session.setAttribute("error", "Unauthorized access.");
            response.sendRedirect("/jsp/unauthorized.jsp");
            return;
        }

        try {
            int ticketId = Integer.parseInt(request.getParameter("ticketId"));
            String status = request.getParameter("status");

            // Validate status
            boolean isValidStatus = false;
            for (String validStatus : VALID_STATUSES) {
                if (validStatus.equals(status)) {
                    isValidStatus = true;
                    break;
                }
            }
            if (!isValidStatus) {
                session.setAttribute("error", "Invalid status selected.");
                response.sendRedirect("ticketDetail?ticketId=" + ticketId);
                return;
            }

            TicketDAO ticketDAO = new TicketDAO();
            // Verify ticket exists
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null) {
                session.setAttribute("error", "Ticket not found.");
                response.sendRedirect("ticketDetail?ticketId=" + ticketId);
                return;
            }

            ticketDAO.updateTicketStatus(ticketId, status);

            // Create notification if status is In Progress, Wait Listed, or Resolved
            if (!status.equals("Open")) {
                NotificationDAO notificationDAO = new NotificationDAO();
                Notification notif = new Notification();
                notif.setUserId(ticket.getSubmittedBy());
                notif.setMessage("Your ticket #" + ticketId + " - " + ticket.getTitle() + " has been updated to " + status + ".");
                notif.setType("ticket_status_update");
                notif.setIsRead(false);
                notif.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                try {
                    notificationDAO.createNotification(notif);
                } catch (SQLException e) {
                    System.err.println("Failed to create notification for ticket #" + ticketId + ": " + e.getMessage());
                    session.setAttribute("error", "Ticket status updated, but notification creation failed.");
                }
            }

            session.setAttribute("success", "Ticket status updated to " + status + ".");
            response.sendRedirect("ticketDetail?ticketId=" + ticketId);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid ticket ID.");
            response.sendRedirect("ticketDetail?ticketId=" + request.getParameter("ticketId"));
        } catch (SQLException e) {
            System.err.println("SQLException in UpdateTicketStatusServlet: " + e.getMessage());
            session.setAttribute("error", "Failed to update ticket status: " + e.getMessage());
            response.sendRedirect("ticketDetail?ticketId=" + request.getParameter("ticketId"));
        }
    }
}