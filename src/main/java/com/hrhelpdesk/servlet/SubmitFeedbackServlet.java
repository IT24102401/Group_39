package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.FeedbackDAO;
import com.hrhelpdesk.model.Feedback;
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

@WebServlet("/submitFeedback")
public class SubmitFeedbackServlet extends HttpServlet {
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("loginServlet");
            return;
        }

        try {
            List<Feedback> feedbacks = feedbackDAO.getFeedbackByUserId(user.getUserId());
            request.setAttribute("feedbacks", feedbacks);
            request.getRequestDispatcher("/jsp/employeeDashboard.jsp").forward(request, response); // Or a dedicated feedback JSP
        } catch (SQLException e) {
            session.setAttribute("error", "Failed to load feedback: " + e.getMessage());
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

        String message = request.getParameter("feedback");
        int rating = Integer.parseInt(request.getParameter("rating"));

        if (message == null || message.trim().isEmpty() || rating < 1 || rating > 5) {
            session.setAttribute("error", "Invalid feedback or rating.");
            response.sendRedirect("employeeDashboard");
            return;
        }

        try {
            Feedback feedback = new Feedback();
            feedback.setMessage(message);
            feedback.setRating(rating);
            feedback.setUserId(user.getUserId());
            feedbackDAO.submitFeedback(feedback);
            session.setAttribute("success", "Feedback submitted successfully.");
        } catch (SQLException e) {
            session.setAttribute("error", "Failed to submit feedback: " + e.getMessage());
        }

        response.sendRedirect("employeeDashboard#feedback");
    }
}