package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketCategoryDAO;
import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.dao.UserDAO;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.TicketCategory;
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

@WebServlet("/ticketForm")
public class TicketFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 1) {  // Assuming roleId 1 is Employee
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            TicketCategoryDAO categoryDAO = new TicketCategoryDAO();
            List<TicketCategory> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);

            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getActiveUsers();
            request.setAttribute("users", users);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load form data.");
        }

        request.getRequestDispatcher("jsp/ticket.jsp").forward(request, response);
    }
}