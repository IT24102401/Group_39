package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.model.User;
import com.hrhelpdesk.model.Ticket;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 1) {  // Check if employee
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            TicketDAO ticketDAO = new TicketDAO();
            List<Ticket> tickets = ticketDAO.getTicketsByUser(user.getUserId());
            request.setAttribute("tickets", tickets);

            // Fetch attachment names for each ticket
            for (Ticket ticket : tickets) {
                List<String> attachmentNames = ticketDAO.getAttachmentNames(ticket.getTicketId());
                request.setAttribute("attachmentNames_" + ticket.getTicketId(), attachmentNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to load ticket history: " + e.getMessage());
        }

        request.getRequestDispatcher("jsp/employeeDashboard.jsp").forward(request, response);
    }
}