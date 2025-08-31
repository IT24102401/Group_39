package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import com.hrhelpdesk.dao.TicketCategoryDAO;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@MultipartConfig
@WebServlet("/submitTicket")
public class SubmitTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String priority = request.getParameter("priority");
            int categoryId = Integer.parseInt(request.getParameter("category"));

            if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || priority == null) {
                throw new IllegalArgumentException("Required fields are missing.");
            }

            Ticket ticket = new Ticket();
            ticket.setTitle(title);
            ticket.setDescription(description);
            ticket.setPriority(priority);
            ticket.setSubmittedBy(user.getUserId());
            ticket.setCategoryId(categoryId);

            TicketDAO ticketDAO = new TicketDAO();
            int ticketId = ticketDAO.createGeneralTicket(ticket);

            TicketCategoryDAO categoryDAO = new TicketCategoryDAO();
            String isa = categoryDAO.getIsaById(categoryId);

            if (isa != null) {
                switch (isa) {
                    case "LEAVE_TICKET":
                        String leaveType = request.getParameter("leave_type");
                        String startDate = request.getParameter("start_date");
                        String endDate = request.getParameter("end_date");
                        if (leaveType != null && startDate != null && endDate != null) {
                            ticketDAO.insertLeaveTicket(ticketId, leaveType, startDate, endDate);
                        }
                        break;
                    case "SALARY_TICKET":
                        String monthYear = request.getParameter("month_year");
                        String issueDetails = request.getParameter("issue_details");
                        if (monthYear != null && issueDetails != null) {
                            ticketDAO.insertSalaryTicket(ticketId, monthYear, issueDetails);
                        }
                        break;
                    case "COMPLAINT_TICKET":
                        String againstUserIdStr = request.getParameter("against_user_id");
                        String severity = request.getParameter("severity");
                        if (againstUserIdStr != null && severity != null) {
                            int againstUserId = Integer.parseInt(againstUserIdStr);
                            ticketDAO.insertComplaintTicket(ticketId, againstUserId, severity);
                        }
                        break;
                    case "SERVICE_LETTER_TICKET":
                        String letterType = request.getParameter("letter_type");
                        String recipientName = request.getParameter("recipient_name");
                        if (letterType != null && recipientName != null) {
                            ticketDAO.insertServiceLetterTicket(ticketId, letterType, recipientName);
                        }
                        break;
                    case "ID_CARD_TICKET":
                        String cardIssueType = request.getParameter("card_issue_type");
                        String details = request.getParameter("details");
                        if (cardIssueType != null && details != null) {
                            ticketDAO.insertIdCardTicket(ticketId, cardIssueType, details);
                        }
                        break;
                    case "PROMOTION_TRANSFER_TICKET":
                        String requestType = request.getParameter("request_type");
                        String reason = request.getParameter("reason");
                        if (requestType != null && reason != null) {
                            ticketDAO.insertPromotionTransferTicket(ticketId, requestType, reason);
                        }
                        break;
                    default:
                        // No additional fields
                }
            }

            // Handle attachments
            Collection<Part> attachmentParts = request.getParts();
            for (Part part : attachmentParts) {
                if ("attachments".equals(part.getName()) && part.getSize() > 0) {
                    String fileName = part.getSubmittedFileName();
                    InputStream fileData = part.getInputStream();
                    ticketDAO.insertAttachment(ticketId, fileName, fileData);
                }
            }

            session.setAttribute("success", "Ticket created successfully with ID: " + ticketId);
            response.sendRedirect("employeeDashboard");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to submit ticket: " + e.getMessage());
            response.sendRedirect("ticketForm");
        }
    }
}