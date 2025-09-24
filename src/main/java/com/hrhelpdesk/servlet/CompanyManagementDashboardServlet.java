// Updated CompanyManagementDashboardServlet.java - Added report handling
package com.hrhelpdesk.servlet;

import com.hrhelpdesk.dao.CompanyManagementUserDAO;
import com.hrhelpdesk.dao.ReportDAO;
import com.hrhelpdesk.model.Feedback;
import com.hrhelpdesk.model.Ticket;
import com.hrhelpdesk.model.Report;
import com.hrhelpdesk.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@WebServlet("/companyManagementDashboard")
public class CompanyManagementDashboardServlet extends HttpServlet {
    private ReportDAO reportDao = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 5) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("generateReport".equals(action)) {
            handleGenerateReport(request, response, user.getUserId());
            return;
        } else if ("download".equals(request.getParameter("download"))) {
            handleDownload(request, response);
            return;
        }

        try {
            CompanyManagementUserDAO dao = new CompanyManagementUserDAO();
            // Existing fetches...
            List<User> users = dao.getAllUsers();
            request.setAttribute("users", users);

            Map<String, List<Ticket>> ticketsByCategory = dao.getTicketsByCategory();
            request.setAttribute("ticketsByCategory", ticketsByCategory);

            List<Feedback> feedbackList = dao.getAllFeedback();
            request.setAttribute("feedbackList", feedbackList);

            Map<String, Double> performanceMetrics = dao.getPerformanceMetrics();
            request.setAttribute("performanceMetrics", performanceMetrics);

            // New: Fetch all reports
            List<Report> reports = fetchAllReports(); // Implement this method or use reportDao.getAllReports() and convert to List<Report>
            request.setAttribute("reports", reports);

        } catch (SQLException e) {
            session.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            response.sendRedirect("error.jsp");
            return;
        }

        request.getRequestDispatcher("/jsp/companyManagementDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Reuse GET for simplicity
    }

    private void handleGenerateReport(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        String reportType = request.getParameter("reportType");
        Date from = Date.valueOf(request.getParameter("fromDate"));
        Date to = Date.valueOf(request.getParameter("toDate"));
        String parameters = request.getParameter("parameters"); // e.g., dept for leave report
        Integer deptId = null;
        if ("Leave".equals(reportType) && parameters != null && !parameters.isEmpty()) {
            deptId = Integer.valueOf(parameters);
        }

        try {
            int reportId;
            if ("Ticket".equals(reportType)) {
                reportId = reportDao.generateTicketReport(from, to, userId, parameters);
            } else if ("Leave".equals(reportType)) {
                reportId = reportDao.generateLeaveReport(from, to, userId, parameters, deptId);
            } else {
                response.sendRedirect("companyManagementDashboard?error=Invalid report type");
                return;
            }
            response.sendRedirect("companyManagementDashboard?success=Report generated with ID: " + reportId);
        } catch (SQLException e) {
            response.sendRedirect("companyManagementDashboard?error=Failed to generate report: " + e.getMessage());
        }
    }

    private void handleDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String downloadType = request.getParameter("download");
        String format = request.getParameter("format");
        response.setContentType("text/csv"); // Default to CSV
        if ("pdf".equals(format)) {
            response.setContentType("application/pdf");
            // TODO: Use iText or similar library for PDF generation
            // For now, fallback to CSV
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadType + "." + format + "\"");

        PrintWriter writer = response.getWriter();
        try {
            CompanyManagementUserDAO dao = new CompanyManagementUserDAO();
            if ("users".equals(downloadType)) {
                List<User> users = dao.getAllUsers();
                writer.println("Employee ID,Username,Name,Email,Role,Department,Status");
                for (User u : users) {
                    writer.println(u.getEmployeeId() + "," + u.getUsername() + "," +
                            (u.getFirstName() + " " + u.getLastName()) + "," + u.getEmail() + "," +
                            u.getRoleName() + "," + (u.getDeptName() != null ? u.getDeptName() : "") + "," +
                            (u.isDeleted() ? "Deleted" : "Active"));
                }
            } else if ("tickets".equals(downloadType)) {
                Map<String, List<Ticket>> ticketsByCategory = dao.getTicketsByCategory();
                writer.println("Category,Ticket ID,Title,Status,Priority,Submitted By,Assigned To,Created At");
                for (Map.Entry<String, List<Ticket>> entry : ticketsByCategory.entrySet()) {
                    for (Ticket t : entry.getValue()) {
                        writer.println(entry.getKey() + "," + t.getTicketId() + "," + t.getTitle() + "," +
                                t.getStatus() + "," + t.getPriority() + "," + t.getSubmittedByUsername() + "," +
                                (t.getAssignedToUsername() != null ? t.getAssignedToUsername() : "") + "," +
                                t.getCreatedAt());
                    }
                }
            } else if ("feedback".equals(downloadType)) {
                // Similar for feedback
                List<Feedback> feedbackList = dao.getAllFeedback();
                writer.println("Feedback ID,Message,Rating,Submitted By,Created At");
                for (Feedback f : feedbackList) {
                    writer.println(f.getFeedbackId() + "," + f.getMessage() + "," + f.getRating() + "," +
                            f.getSubmittedByUsername() + "," + f.getCreatedAt());
                }
            } else if ("report".equals(downloadType)) {
                int reportId = Integer.parseInt(request.getParameter("reportId"));
                // Fetch report details using reportDao and output as CSV
                // Example for TicketReport
                Map<String, Object> details = reportDao.getTicketReportDetails(reportId);
                Report report = getReportById(reportId); // Implement or use DAO
                writer.println("Report Type: " + report.getReportType());
                writer.println("Period: " + report.getPeriodFrom() + " to " + report.getPeriodTo());
                writer.println("Open Count: " + details.get("openCount"));
                writer.println("Closed Count: " + details.get("closedCount"));
                writer.println("Avg Resolution Time (days): " + details.get("avgResolutionTime"));
            }
        } catch (Exception e) {
            writer.println("Error generating download: " + e.getMessage());
        }
        writer.flush();
    }

    // Placeholder for fetching reports - adjust to use ReportDAO
    private List<Report> fetchAllReports() throws SQLException {
        // Use reportDao.getAllReports() and map to Report objects
        return new ArrayList<>(); // Implement mapping
    }

    private Report getReportById(int reportId) throws SQLException {
        // Implement using DAO
        return null;
    }
}