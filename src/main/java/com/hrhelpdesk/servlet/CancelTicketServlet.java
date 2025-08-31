package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cancelTicket")
public class CancelTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            int ticketId = Integer.parseInt(request.getParameter("ticketId"));
            TicketDAO ticketDAO = new TicketDAO();

            // Verify ticket belongs to user and is Open
            if (ticketDAO.isTicketOwnedByUser(ticketId, user.getUserId()) && ticketDAO.isTicketOpen(ticketId)) {
                ticketDAO.cancelTicket(ticketId);
                session.setAttribute("success", "Ticket #" + ticketId + " cancelled successfully.");
            } else {
                session.setAttribute("error", "Cannot cancel ticket: Invalid ticket or not authorized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to cancel ticket: " + e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid ticket ID.");
        }

        response.sendRedirect("employeeDashboard#ticket-history");
    }
}