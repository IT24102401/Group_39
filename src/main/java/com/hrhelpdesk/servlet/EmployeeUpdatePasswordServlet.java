package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.UserDAO;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/EmployeeUpdatePassword")
public class EmployeeUpdatePasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            UserDAO userDAO = new UserDAO();
            User dbUser = userDAO.validateUser(user.getUsername(), currentPassword);
            if (dbUser == null) {
                request.setAttribute("error", "Current password is incorrect");
                request.getRequestDispatcher("jsp/EmployeeDashboard.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "New passwords do not match");
                request.getRequestDispatcher("jsp/EmployeeDashboard.jsp").forward(request, response);
                return;
            }

            userDAO.updatePassword(user.getUserId(), newPassword);
            request.setAttribute("success", "Password updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update password");
        }

        request.getRequestDispatcher("jsp/employeeDashboard.jsp").forward(request, response);
    }
}