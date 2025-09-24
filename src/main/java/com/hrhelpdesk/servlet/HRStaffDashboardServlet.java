package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
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

@WebServlet("/hrStaffDashboard")
public class HRStaffDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        TicketDAO ticketDAO = new TicketDAO();

        try {
            // Fetch in-progress and resolved tickets assigned to this staff
            List<Ticket> inProgressTickets = ticketDAO.getAssignedTickets(user.getUserId());
            List<Ticket> resolvedTickets = ticketDAO.getResolvedTicketsByAssignedTo(user.getUserId());
            request.setAttribute("inProgressTickets", inProgressTickets);
            request.setAttribute("resolvedTickets", resolvedTickets);

            // Fetch attachment names for each ticket
            for (Ticket ticket : inProgressTickets) {
                List<String> attachmentNames = ticketDAO.getAttachmentNames(ticket.getTicketId());
                request.setAttribute("attachmentNames_" + ticket.getTicketId(), attachmentNames);
            }
            for (Ticket ticket : resolvedTickets) {
                List<String> attachmentNames = ticketDAO.getAttachmentNames(ticket.getTicketId());
                request.setAttribute("attachmentNames_" + ticket.getTicketId(), attachmentNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/hrStaffDashboard.jsp").forward(request, response);
    }
}