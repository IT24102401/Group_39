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
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@WebServlet("/hrViewAttachment")
public class HRViewAttachmentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 3 || user.getRoleId() != 2 ) {
            response.sendRedirect("/jsp/unauthorized.jsp");
            return;
        }

        try {
            int ticketId = Integer.parseInt(request.getParameter("ticketId"));
            String fileName = request.getParameter("fileName");

            TicketDAO ticketDAO = new TicketDAO();
            Blob blob = ticketDAO.getAttachmentData(ticketId, fileName);
            if (blob == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Attachment not found.");
                return;
            }

            // Set content type based on file extension
            String contentType = getServletContext().getMimeType(fileName);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

            // Write blob data to response
            try (InputStream inputStream = blob.getBinaryStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving attachment.");
        }
    }
}