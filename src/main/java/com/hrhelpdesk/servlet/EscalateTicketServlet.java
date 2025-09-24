// New EscalateTicketServlet.java
package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.HRStaffUserDAO;
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

@WebServlet("/escalateTicket")
public class EscalateTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String note = request.getParameter("note");

        TicketDAO ticketDAO = new TicketDAO();
        HRStaffUserDAO userDAO = new HRStaffUserDAO();
        try {
            int managerId = userDAO.getHRManagerId();
            ticketDAO.escalateTicket(ticketId, managerId, note, user.getUserId());
            session.setAttribute("success", "Ticket escalated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to escalate ticket: " + e.getMessage());
        }

        response.sendRedirect("ticketDetail?ticketId=" + ticketId);
    }
}