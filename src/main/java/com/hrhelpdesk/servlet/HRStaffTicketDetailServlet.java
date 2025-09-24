package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.TicketResponse;
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

@WebServlet("/hrStaffTicketDetail")
public class HRStaffTicketDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        TicketDAO ticketDAO = new TicketDAO();
        try {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null || ticket.getAssignedTo() != user.getUserId()) {
                session.setAttribute("error", "Ticket not found or not assigned to you.");
                response.sendRedirect("hrStaffDashboard");
                return;
            }
            request.setAttribute("ticket", ticket);
            List<String> attachmentNames = ticketDAO.getAttachmentNames(ticketId);
            request.setAttribute("attachmentNames", attachmentNames);
            List<TicketResponse> responses = ticketDAO.getTicketResponses(ticketId);
            request.setAttribute("responses", responses);
        } catch (SQLException e) {
            session.setAttribute("error", "Failed to load ticket details: " + e.getMessage());
            response.sendRedirect("hrStaffDashboard");
            return;
        }

        request.getRequestDispatcher("/jsp/hrStaffTicketDetail.jsp").forward(request, response);
    }
}
