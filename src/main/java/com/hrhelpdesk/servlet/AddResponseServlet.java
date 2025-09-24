// Updated AddResponseServlet.java to support HR Staff
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

@WebServlet("/addResponse")
public class AddResponseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRoleId() != 3 && user.getRoleId() != 2)) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String message = request.getParameter("message");

        TicketDAO ticketDAO = new TicketDAO();
        try {
            ticketDAO.addTicketResponse(ticketId, message, user.getUserId());
            session.setAttribute("success", "Response added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to add response: " + e.getMessage());
        }

        response.sendRedirect("ticketDetail?ticketId=" + ticketId);
    }
}