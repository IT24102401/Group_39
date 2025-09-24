package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

@WebServlet("/downloadAttachment")
public class DownloadAttachmentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int ticketId = Integer.parseInt(request.getParameter("ticketId"));
            String fileName = request.getParameter("fileName");
            TicketDAO ticketDAO = new TicketDAO();
            Blob blob = ticketDAO.getAttachmentData(ticketId, fileName);
            if (blob != null) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                try (InputStream inputStream = blob.getBinaryStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}