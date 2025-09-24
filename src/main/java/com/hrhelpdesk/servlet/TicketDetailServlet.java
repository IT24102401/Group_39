package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.HRManagerUserDAO;
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

@WebServlet("/ticketDetail")
public class TicketDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/jsp/unauthorized.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        TicketDAO ticketDAO = new TicketDAO();
        HRManagerUserDAO userDAO = new HRManagerUserDAO();

        try {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null) {
                session.setAttribute("error", "Ticket not found.");
                if (user.getRoleId() == 3) {
                    response.sendRedirect("hrManagerDashboard");
                } else if (user.getRoleId() == 2) {
                    response.sendRedirect("hrStaffDashboard");
                }
                return;
            }

            // Permission check
            if (user.getRoleId() != 3 && (user.getRoleId() != 2 || ticket.getAssignedTo() != user.getUserId())) {
                response.sendRedirect("/jsp/unauthorized.jsp");
                return;
            }

            request.setAttribute("ticket", ticket);
            User submitter = userDAO.getUserById(ticket.getSubmittedBy());
            request.setAttribute("submitter", submitter);
            List<TicketResponse> responses = ticketDAO.getTicketResponses(ticketId);
            request.setAttribute("responses", responses);
            List<String> attachmentNames = ticketDAO.getAttachmentNames(ticketId);
            request.setAttribute("attachmentNames", attachmentNames);
            List<User> staff = userDAO.getAllHRStaff();
            request.setAttribute("staff", staff);

            // Forward to appropriate JSP based on role
            if (user.getRoleId() == 3) {
                request.getRequestDispatcher("/jsp/ticketDetail.jsp").forward(request, response);
            } else if (user.getRoleId() == 2) {
                request.getRequestDispatcher("/jsp/ticketDetailStaff.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to load ticket details: " + e.getMessage());
            if (user.getRoleId() == 3) {
                response.sendRedirect("hrManagerDashboard");
            } else if (user.getRoleId() == 2) {
                response.sendRedirect("hrStaffDashboard");
            }
        }
    }
}