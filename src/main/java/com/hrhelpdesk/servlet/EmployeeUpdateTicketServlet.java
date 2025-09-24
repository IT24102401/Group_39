package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@MultipartConfig
@WebServlet("/updateTicket")
public class EmployeeUpdateTicketServlet extends HttpServlet {
    private final TicketDAO ticketDAO = new TicketDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        try {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null || ticket.getSubmittedBy() != user.getUserId()) {
                session.setAttribute("error", "Ticket not found or unauthorized.");
                response.sendRedirect("employeeDashboard");
                return;
            }
            if (!ticketDAO.isWithin24Hours(ticketId)) {
                session.setAttribute("error", "Cannot update ticket: 24-hour limit exceeded.");
                response.sendRedirect("employeeDashboard");
                return;
            }
            request.setAttribute("ticket", ticket);
            request.getRequestDispatcher("/jsp/updateTicket.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
            response.sendRedirect("employeeDashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");

        try {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null || ticket.getSubmittedBy() != user.getUserId()) {
                session.setAttribute("error", "Ticket not found or unauthorized.");
                response.sendRedirect("employeeDashboard");
                return;
            }

            if (!ticketDAO.isWithin24Hours(ticketId)) {
                session.setAttribute("error", "Cannot update ticket: 24-hour limit exceeded.");
                response.sendRedirect("employeeDashboard");
                return;
            }

            ticket.setTitle(title);
            ticket.setDescription(description);
            ticket.setPriority(priority);
            ticketDAO.updateTicket(ticket);

            // Handle additional attachments
            Part attachmentPart = request.getPart("attachment");
            if (attachmentPart != null && attachmentPart.getSize() > 0) {
                String fileName = attachmentPart.getSubmittedFileName();
                InputStream fileData = attachmentPart.getInputStream();
                ticketDAO.addAttachmentToTicket(ticketId, fileName, fileData);
            }

            session.setAttribute("success", "Ticket updated successfully.");
            response.sendRedirect("employeeDashboard");
        } catch (SQLException e) {
            session.setAttribute("error", "Failed to update ticket: " + e.getMessage());
            response.sendRedirect("employeeDashboard");
        }
    }
}