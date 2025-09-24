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

@WebServlet("/assignTicket")
public class AssignTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 3) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        int assignedTo = Integer.parseInt(request.getParameter("assignedTo"));

        TicketDAO ticketDAO = new TicketDAO();
        try {
            ticketDAO.assignTicket(ticketId, assignedTo);
            session.setAttribute("success", "Ticket assigned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to assign ticket: " + e.getMessage());
        }

        response.sendRedirect("hrManagerDashboard");
    }
}
