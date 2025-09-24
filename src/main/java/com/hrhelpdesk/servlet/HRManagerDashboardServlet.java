package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.HRManagerUserDAO;
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

@WebServlet("/hrManagerDashboard")
public class HRManagerDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 3) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        TicketDAO ticketDAO = new TicketDAO();
        HRManagerUserDAO userDAO = new HRManagerUserDAO();

        try {
            // Fetch HR staff (role_id=2)
            List<User> staff = userDAO.getAllHRStaff();
            request.setAttribute("staff", staff);

            // Fetch open, in-progress, and resolved tickets
            List<Ticket> openTickets = ticketDAO.getAllOpenTickets();
            List<Ticket> inProgressTickets = ticketDAO.getAllInProgressTickets();
            List<Ticket> resolvedTickets = ticketDAO.getAllResolvedTickets();
            request.setAttribute("openTickets", openTickets);
            request.setAttribute("inProgressTickets", inProgressTickets);
            request.setAttribute("resolvedTickets", resolvedTickets);

            // Fetch unassigned open tickets count
            int unassignedTicketsCount = ticketDAO.getUnassignedOpenTicketsCount();
            request.setAttribute("unassignedTicketsCount", unassignedTicketsCount);

            // Fetch attachment names for each ticket
            for (Ticket ticket : openTickets) {
                List<String> attachmentNames = ticketDAO.getAttachmentNames(ticket.getTicketId());
                request.setAttribute("attachmentNames_" + ticket.getTicketId(), attachmentNames);
            }
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

        request.getRequestDispatcher("/jsp/hrManagerDashboard.jsp").forward(request, response);
    }
}
