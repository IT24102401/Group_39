package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/markNotificationRead")
public class MarkNotificationReadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int notificationId = Integer.parseInt(request.getParameter("notificationId"));
        NotificationDAO notificationDAO = new NotificationDAO();
        try {
            notificationDAO.markNotificationAsRead(notificationId);
            response.sendRedirect(request.getContextPath() + "/employeeDashboard");
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Failed to mark notification as read.");
            response.sendRedirect(request.getContextPath() + "/employeeDashboard");
        }
    }
}