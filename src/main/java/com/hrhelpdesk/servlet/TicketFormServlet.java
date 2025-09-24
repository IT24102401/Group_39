package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketCategoryDAO;
import com.hrhelpdesk.dao.DepartmentDAO;
import com.hrhelpdesk.service.UserService;
import com.hrhelpdesk.model.Department;
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
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            TicketCategoryDAO categoryDAO = new TicketCategoryDAO();
            List<TicketCategory> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);

            UserService userService = new UserService();
            List<User> users = userService.getActiveUsers(); // For against_user_id in complaints
            request.setAttribute("users", users);

            DepartmentDAO deptDAO = new DepartmentDAO();
            List<Department> departments = deptDAO.getAllDepartments();
            request.setAttribute("departments", departments);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load form data.");
        }

        request.getRequestDispatcher("jsp/ticket.jsp").forward(request, response);
    }
}