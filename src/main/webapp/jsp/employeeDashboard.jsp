<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.User" %>
<%@ page import="com.hrhelpdesk.model.Ticket" %>
<%@ page import="com.hrhelpdesk.model.Feedback" %>
<%@ page import="com.hrhelpdesk.model.Notification" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Dashboard - HR Help Desk</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
        }

        body {
            min-height: 100vh;
            background: #121212;
            overflow-x: hidden;
        }

        #particles-js {
            position: fixed;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 1;
            opacity: 0.3;
        }

        .dashboard-container {
            display: flex;
            min-height: 100vh;
            z-index: 2;
            position: relative;
        }

        .sidebar {
            width: 270px;
            background: linear-gradient(180deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95));
            backdrop-filter: blur(8px);
            border-right: 1px solid rgba(255, 255, 255, 0.1);
            padding: 30px 20px;
            position: fixed;
            top: 0;
            left: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            animation: slideIn 0.6s ease-out;
        }

        @keyframes slideIn {
            0% { transform: translateX(-100%); opacity: 0; }
            100% { transform: translateX(0); opacity: 1; }
        }

        .sidebar h2 {
            font-size: 1.8em;
            font-weight: 500;
            color: #ffffff;
            margin-bottom: 30px;
            text-align: center;
            letter-spacing: 0.5px;
        }

        .sidebar a {
            display: flex;
            align-items: center;
            padding: 14px 18px;
            color: #b0b0b0;
            text-decoration: none;
            font-size: 0.95em;
            font-weight: 400;
            margin: 6px 0;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .sidebar a:hover, .sidebar a.active {
            background: rgba(59, 130, 246, 0.15);
            color: #3b82f6;
            transform: translateX(4px);
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.3);
        }

        .notification-link {
            position: relative;
            overflow: hidden;
        }

        .notification-link.unread::after {
            content: '';
            position: absolute;
            top: 50%;
            right: 10px;
            width: 10px;
            height: 10px;
            background: #ef4444;
            border-radius: 50%;
            transform: translateY(-50%);
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { transform: translateY(-50%) scale(1); opacity: 1; }
            50% { transform: translateY(-50%) scale(1.3); opacity: 0.7; }
            100% { transform: translateY(-50%) scale(1); opacity: 1; }
        }

        .notification-badge {
            background: linear-gradient(45deg, #ef4444, #f87171);
            color: white;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.8em;
            margin-left: 8px;
            min-width: 20px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }

        .main-content {
            flex: 1;
            padding: 40px;
            background: linear-gradient(135deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95));
            backdrop-filter: blur(10px);
            border-radius: 16px;
            margin: 20px 20px 20px 310px;
            color: #ffffff;
            animation: fadeIn 0.8s ease-out;
        }

        @keyframes fadeIn {
            0% { opacity: 0; transform: translateY(20px); }
            100% { opacity: 1; transform: translateY(0); }
        }

        .main-content h1 {
            font-size: 2.5em;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 20px;
            letter-spacing: 0.3px;
        }

        .logout-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 10px 24px;
            background: #ef4444;
            color: #ffffff;
            font-size: 0.9em;
            font-weight: 500;
            border-radius: 8px;
            text-decoration: none;
            transition: background 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
        }

        .logout-btn:hover {
            background: #dc2626;
            transform: translateY(-2px);
            box-shadow: 0 8px 16px rgba(239, 68, 68, 0.3);
        }

        .card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 12px;
            padding: 25px;
            margin: 20px 0;
            border: 1px solid rgba(255, 255, 255, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 24px rgba(0, 0, 0, 0.4);
        }

        .card h3 {
            font-size: 1.6em;
            font-weight: 500;
            color: #a3bffa;
            margin-bottom: 15px;
            position: relative;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .card h3::after {
            content: '';
            position: absolute;
            bottom: -4px;
            left: 0;
            width: 50px;
            height: 3px;
            background: linear-gradient(90deg, #3b82f6, #a3bffa);
            border-radius: 2px;
        }

        .form-group {
            margin-bottom: 20px;
            flex: 1;
        }

        .form-row {
            display: flex;
            gap: 20px;
        }

        .form-group label {
            display: block;
            color: #b0b0b0;
            font-size: 0.95em;
            font-weight: 400;
            margin-bottom: 8px;
        }

        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 12px 16px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            font-size: 0.95em;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input[readonly] {
            background: rgba(255, 255, 255, 0.02);
            color: #b0b0b0;
        }

        .form-group select {
            background: transparent;
            appearance: none;
            padding-right: 32px;
            background-image: url('data:image/svg+xml;utf8,<svg fill="%23b0b0b0" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>');
            background-repeat: no-repeat;
            background-position: right 8px center;
        }

        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.3);
        }

        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }

        .btn {
            padding: 14px 40px;
            background: linear-gradient(90deg, #3b82f6, #60a5fa);
            color: #ffffff;
            font-size: 1em;
            font-weight: 500;
            text-transform: uppercase;
            border-radius: 10px;
            border: none;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
            display: block;
            margin: 24px auto 0;
            text-align: center;
        }

        .btn:hover {
            background: linear-gradient(90deg, #2563eb, #3b82f6);
            transform: translateY(-3px);
            box-shadow: 0 6px 20px rgba(59, 130, 246, 0.6);
        }

        .cancel-btn {
            padding: 8px 16px;
            background: #ef4444;
            color: #ffffff;
            font-size: 0.9em;
            font-weight: 500;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.3s ease;
        }

        .cancel-btn:hover {
            background: #dc2626;
            transform: translateY(-2px);
        }

        .success-message, .error-message {
            font-size: 0.9em;
            padding: 12px;
            margin: 10px 0;
            border-radius: 8px;
            text-align: center;
            display: none;
        }

        .success-message {
            background: rgba(52, 211, 153, 0.1);
            color: #34d399;
            border: 1px solid #34d399;
        }

        .error-message {
            background: rgba(239, 68, 68, 0.1);
            color: #ef4444;
            border: 1px solid #ef4444;
        }

        .ticket-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .ticket-table th, .ticket-table td {
            padding: 16px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
            color: #d0d0d0;
            font-weight: 400;
        }

        .ticket-table th {
            background: rgba(255, 255, 255, 0.03);
            color: #3b82f6;
            font-weight: 500;
            font-size: 0.85em;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .ticket-table tr:hover {
            background: rgba(255, 255, 255, 0.06);
        }

        .ticket-table a {
            color: #60a5fa;
            text-decoration: none;
        }

        .ticket-table a:hover {
            text-decoration: underline;
        }

        .notification-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 8px;
            margin-top: 20px;
        }

        .notification-table th {
            padding: 12px 16px;
            background: rgba(255, 255, 255, 0.03);
            color: #3b82f6;
            font-weight: 500;
            font-size: 0.85em;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .notification-table td {
            padding: 16px;
            background: rgba(255, 255, 255, 0.04);
            color: #d0d0d0;
            font-weight: 400;
            border-radius: 8px;
        }

        .notification-table tr.unread td {
            background: rgba(59, 130, 246, 0.15);
            color: #ffffff;
        }

        .notification-table tr:hover td {
            background: rgba(255, 255, 255, 0.08);
            transform: translateY(-2px);
            transition: all 0.3s ease;
        }

        .notification-table .notification-message {
            max-width: 400px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .notification-table .notification-time {
            font-size: 0.85em;
            color: #9ca3af;
        }

        .notification-table .notification-action button {
            background: none;
            border: none;
            color: #60a5fa;
            text-decoration: underline;
            cursor: pointer;
            font-size: 0.9em;
        }

        .notification-table .notification-action button:hover {
            color: #3b82f6;
        }

        .filter-group {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .filter-group input, .filter-group select {
            max-width: 300px;
        }

        footer {
            margin-top: 30px;
            text-align: center;
            color: #888;
            font-size: 0.85em;
            font-weight: 400;
        }

        @media (max-width: 768px) {
            .dashboard-container {
                flex-direction: column;
            }

            .sidebar {
                width: 100%;
                height: auto;
                position: static;
                flex-direction: row;
                flex-wrap: wrap;
                justify-content: center;
                padding: 15px;
            }

            .main-content {
                margin: 15px;
                padding: 20px;
            }

            .main-content h1 {
                font-size: 2em;
            }

            .card {
                padding: 15px;
            }

            .form-row {
                flex-direction: column;
                gap: 10px;
            }

            .logout-btn {
                font-size: 0.85em;
                padding: 8px 16px;
            }

            .btn {
                font-size: 0.9em;
                padding: 10px 24px;
            }

            .ticket-table th, .ticket-table td {
                padding: 10px;
                font-size: 0.8em;
            }

            .notification-table th, .notification-table td {
                padding: 10px;
                font-size: 0.8em;
            }

            .filter-group {
                flex-direction: column;
            }
        }

        @media (max-width: 480px) {
            .main-content h1 {
                font-size: 1.8em;
            }

            .sidebar h2 {
                font-size: 1.2em;
            }

            .card h3 {
                font-size: 1.2em;
            }

            .ticket-table th, .ticket-table td {
                padding: 8px;
                font-size: 0.75em;
            }

            .notification-table th, .notification-table td {
                padding: 8px;
                font-size: 0.75em;
            }
        }

        #submit-ticket {
            background: linear-gradient(135deg, rgba(40, 40, 40, 0.9), rgba(60, 60, 60, 0.9));
            border: 1px solid rgba(59, 130, 246, 0.2);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(10px);
            padding: 25px;
            border-radius: 15px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        #submit-ticket h3 {
            color: #a3bffa;
            font-size: 1.6em;
            margin-bottom: 20px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        #submit-ticket p {
            color: #b0b0b0;
            font-size: 1.1em;
            line-height: 1.6;
            margin-bottom: 20px;
            text-align: center;
        }

        #submit-ticket .btn {
            background: linear-gradient(90deg, #3b82f6, #60a5fa);
            padding: 14px 40px;
            font-size: 1.1em;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
            text-transform: none;
            letter-spacing: 1px;
        }

        #submit-ticket .faq {
            margin-top: 20px;
            padding-top: 15px;
            border-top: 1px dashed rgba(255, 255, 255, 0.1);
        }

        #submit-ticket .faq h4 {
            color: #a3bffa;
            font-size: 1.2em;
            margin-bottom: 10px;
        }

        #submit-ticket .faq p {
            font-size: 1em;
            margin-bottom: 10px;
        }

        #submit-ticket .contact {
            margin-top: 15px;
            text-align: center;
        }

        #submit-ticket .contact a {
            color: #60a5fa;
            text-decoration: none;
            font-weight: 500;
        }

        #submit-ticket .contact a:hover {
            color: #3b82f6;
            text-decoration: underline;
        }

        #submit-ticket .progress {
            margin-top: 15px;
            text-align: center;
            color: #b0b0b0;
            font-size: 0.9em;
        }

        .update-btn {
            padding: 8px 16px;
            background: #22dc24;
            color: #ffffff;
            font-size: 0.9em;
            font-weight: 500;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.3s ease;
            margin-left: 5px;
        }

        .update-btn:hover {
            background: #56ae57;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="dashboard-container">
    <div class="sidebar">
        <h2>HR Help Desk</h2>
        <a href="#profile" class="active"><i class="fas fa-user" style="margin-right: 8px;"></i> Profile Management</a>
        <a href="#submit-ticket"><i class="fas fa-ticket-alt" style="margin-right: 8px;"></i> Submit HR Ticket</a>
        <a href="#ticket-history"><i class="fas fa-history" style="margin-right: 8px;"></i> Ticket History</a>
        <a href="#notifications" class="notification-link ">
            <i class="fas fa-bell" style="margin-right: 8px;"></i> Notifications
            <% Integer unreadCount = (Integer) request.getAttribute("unreadNotificationCount"); %>
            <% if (unreadCount != null && unreadCount > 0) { %>
            <span class="notification-badge"><%= unreadCount %></span>
            <% } %>
        </a>
        <a href="#feedback"><i class="fas fa-comment" style="margin-right: 8px;"></i> Feedback</a>
    </div>
    <div class="main-content">
        <a href="logout" class="logout-btn"><i class="fas fa-sign-out-alt" style="margin-right: 8px;"></i> Logout</a>
        <h1>Welcome, <%= (session.getAttribute("user") != null) ? ((User) session.getAttribute("user")).getFirstName() + " " + ((User) session.getAttribute("user")).getLastName() : "Guest" %>!</h1>

        <!-- Success/Error Messages -->
        <% if (session.getAttribute("success") != null) { %>
        <div class="success-message" style="display: block;">
            <i class="fas fa-check-circle" style="margin-right: 8px;"></i> <%= session.getAttribute("success") %>
        </div>
        <% session.removeAttribute("success"); %>
        <% } %>
        <%
            String errorMessage = (String) session.getAttribute("error");
            if (errorMessage != null && !errorMessage.contains("Invalid column name 'submitted_by_username'")) {
        %>
        <div class="error-message" style="display: block;">
            <i class="fas fa-exclamation-circle" style="margin-right: 8px;"></i> <%= errorMessage %>
        </div>
        <%
            session.removeAttribute("error");
        %>
        <% } %>

        <div id="profile" class="card">
            <h3>Profile Management</h3>
            <% User user = (User) session.getAttribute("user"); %>
            <% if (user != null) { %>
            <div class="form-row">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" value="<%= user.getUsername() %>" readonly>
                </div>
                <div class="form-group">
                    <label>Job Title</label>
                    <input type="text" value="<%= user.getJobTitle() != null ? user.getJobTitle() : "" %>" readonly>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Department</label>
                    <input type="text" value="<%= user.getDeptName() != null ? user.getDeptName() : "N/A" %>" readonly>
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <input type="text" value="<%= user.getRoleName() != null ? user.getRoleName() : "" %>" readonly>
                </div>
            </div>
            <form action="EmployeeUpdateProfile" method="post">
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName-profile">First Name</label>
                        <input type="text" id="firstName-profile" name="firstName" value="<%= user.getFirstName() != null ? user.getFirstName() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label for="lastName-profile">Last Name</label>
                        <input type="text" id="lastName-profile" name="lastName" value="<%= user.getLastName() != null ? user.getLastName() : "" %>" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="email-profile">Email</label>
                        <input type="email" id="email-profile" name="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" required>
                    </div>
                </div>
                <button type="submit" class="btn">Update Profile</button>
            </form>
            <h3 style="margin-top: 30px;">Change Password</h3>
            <form action="EmployeeUpdatePassword" method="post" onsubmit="return validatePasswordForm()">
                <div class="form-group">
                    <label for="currentPassword-password">Current Password</label>
                    <input type="password" id="currentPassword-password" name="currentPassword" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="newPassword-password">New Password</label>
                        <input type="password" id="newPassword-password" name="newPassword" required>
                        <div id="newPasswordError-password" class="error-message"></div>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword-password">Confirm New Password</label>
                        <input type="password" id="confirmPassword-password" name="confirmPassword" required>
                        <div id="confirmPasswordError-password" class="error-message"></div>
                    </div>
                </div>
                <button type="submit" class="btn">Update Password</button>
            </form>
            <% } else { %>
            <div class="error-message" style="display: block;">
                <i class="fas fa-exclamation-circle" style="margin-right: 8px;"></i> Employee information is not available. Please log in again or contact support.
            </div>
            <% } %>
        </div>

        <div id="submit-ticket" class="card" style="display: none;">
            <h3>Submit HR Ticket</h3>
            <p>Need assistance from HR? We're here to help with any issues related to payroll, benefits, leave, or other concerns.</p>
            <p>Please click the button below to create a new HR support ticket, and one of our team members will respond promptly.</p>
            <a href="ticketForm" class="btn">New Ticket</a>
            <div class="faq">
                <h4>Quick FAQ</h4>
                <p><strong>Q:</strong> How long does it take to process a ticket? <br><strong>A:</strong> Typically within 48 hours, depending on the complexity.</p>
                <p><strong>Q:</strong> Can I upload documents? <br><strong>A:</strong> Yes, you can attach files in the ticket form.</p>
            </div>
            <div class="contact">
                <p>For immediate help, contact us at <a href="mailto:amila123@gmail.com">hrsupport@example.com</a></p>
            </div>
            <div class="progress">
                <p>Current average response time: 24 hours</p>
            </div>
        </div>

        <div id="ticket-history" class="card" style="display: none;">
            <h3>Ticket History</h3>
            <div class="filter-group">
                <div class="form-group">
                    <label for="ticket-search">Search Tickets</label>
                    <input type="text" id="ticket-search" placeholder="Search by title or description...">
                </div>
                <div class="form-group">
                    <label for="status-filter">Filter by Status</label>
                    <select id="status-filter">
                        <option value="">All Statuses</option>
                        <option value="Open">Open</option>
                        <option value="In Progress">In Progress</option>
                        <option value="Resolved">Resolved</option>
                        <option value="Cancelled">Cancelled</option>
                    </select>
                </div>
            </div>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Category</th>
                    <th>Title</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Attachments</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody id="ticket-history-body">
                <% List<Ticket> tickets = (List<Ticket>) request.getAttribute("tickets"); %>
                <% if (tickets != null && !tickets.isEmpty()) { %>
                <% for (Ticket ticket : tickets) { %>
                <tr data-title="<%= ticket.getTitle().toLowerCase() %>" data-description="<%= ticket.getDescription().toLowerCase() %>" data-status="<%= ticket.getStatus() %>">
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getCategoryName() != null ? ticket.getCategoryName() : "Unknown" %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getStatus() %></td>
                    <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(ticket.getCreatedAt()) %></td>
                    <td>
                        <% List<String> attachmentNames = (List<String>) request.getAttribute("attachmentNames_" + ticket.getTicketId()); %>
                        <% if (attachmentNames != null && !attachmentNames.isEmpty()) { %>
                        <% for (String fileName : attachmentNames) { %>
                        <a href="viewAttachment?ticketId=<%= ticket.getTicketId() %>&fileName=<%= java.net.URLEncoder.encode(fileName, "UTF-8") %>" target="_blank"><%= fileName %></a><br>
                        <% } %>
                        <% } else { %>
                        None
                        <% } %>
                    </td>
                    <td>
                        <% if ("Open".equals(ticket.getStatus())) { %>
                        <form action="cancelTicket" method="post" style="display: inline;">
                            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
                            <button type="submit" class="cancel-btn">Cancel</button>
                        </form>
                        <% } %>
                        <%
                            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
                            LocalDateTime ticketTime = ticket.getCreatedAt().toLocalDateTime();
                            long hoursDiff = ChronoUnit.HOURS.between(ticketTime, now);
                            if (hoursDiff < 24) {
                        %>
                        <form action="updateTicket" method="get" style="display: inline;">
                            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
                            <button type="submit" class="update-btn">Update</button>
                        </form>
                        <% } %>
                    </td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="7">No tickets found.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div id="notifications" class="card" style="display: none;">
            <h3>Notifications</h3>
            <% List<Notification> notifications = (List<Notification>) request.getAttribute("notifications"); %>
            <% if (notifications != null && !notifications.isEmpty()) { %>
            <table class="notification-table">
                <thead>
                <tr>
                    <th>Message</th>
                    <th>Type</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% for (Notification notification : notifications) { %>
                <tr class="<%= notification.getIsRead() ? "" : "unread" %>">
                    <td class="notification-message"><%= notification.getMessage() %></td>
                    <td><%= notification.getType() %></td>
                    <td class="notification-time"><%= notification.getRelativeTime() %></td>
                    <td><%= notification.getIsRead() ? "Read" : "Unread" %></td>
                    <td class="notification-action">
                        <% if (!notification.getIsRead()) { %>
                        <form action="markNotificationRead" method="post" style="display:inline;">
                            <input type="hidden" name="notificationId" value="<%= notification.getNotificationId() %>">
                            <button type="submit">Mark as Read</button>
                        </form>
                        <% } else { %>
                        <span>Read</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } else { %>
            <p>No notifications available.</p>
            <% } %>
        </div>

        <div id="feedback" class="card" style="display: none;">
            <h3>Feedback</h3>
            <form action="submitFeedback" method="post">
                <div class="form-group">
                    <label for="feedback">Your Feedback</label>
                    <textarea id="feedback" name="feedback" placeholder="Share your thoughts..." required></textarea>
                </div>
                <div class="form-group">
                    <label for="rating">Rating (1-5)</label>
                    <select id="rating" name="rating" required>
                        <option value="1">1 - Poor</option>
                        <option value="2">2 - Fair</option>
                        <option value="3">3 - Good</option>
                        <option value="4">4 - Very Good</option>
                        <option value="5">5 - Excellent</option>
                    </select>
                </div>
                <button type="submit" class="btn">Submit Feedback</button>
            </form>
            <h4 style="color: #a3bffa; font-size: 1.4em; margin-bottom: 20px; position: relative; text-transform: uppercase; letter-spacing: 0.5px;">
                Your Previous Feedback
                <span style="position: absolute; bottom: -4px; left: 0; width: 60px; height: 3px; background: linear-gradient(90deg, #3b82f6, #60a5fa); border-radius: 2px;"></span>
            </h4>
            <% List<Feedback> feedbacks = (List<Feedback>) request.getAttribute("feedbacks"); %>
            <% if (feedbacks != null && !feedbacks.isEmpty()) { %>
            <ul style="list-style: none; padding: 0;">
                <% for (Feedback f : feedbacks) { %>
                <li style="background: rgba(255, 255, 255, 0.05); border-radius: 8px; padding: 15px; margin-bottom: 10px; transition: transform 0.3s ease, box-shadow 0.3s ease; border: 1px solid rgba(255, 255, 255, 0.1);">
                    <div style="display: flex; align-items: center; gap: 10px;">
            <span style="background: linear-gradient(45deg, #3b82f6, #60a5fa); color: #ffffff; font-size: 0.9em; font-weight: 500; padding: 4px 10px; border-radius: 12px;">
                Rating: <%= f.getRating() %>/5
            </span>
                        <p style="color: #d0d0d0; margin: 0; flex: 1;"><%= f.getMessage() %></p>
                    </div>
                    <small style="display: block; color: #9ca3af; font-size: 0.85em; margin-top: 8px;">
                        <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(f.getCreatedAt()) %>
                    </small>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            <p style="color: #b0b0b0; font-size: 1em; text-align: center; padding: 20px; background: rgba(255, 255, 255, 0.03); border-radius: 8px;">
                No feedback submitted yet.
            </p>
            <% } %>
        </div>

        <footer>
            &copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> HR Help Desk. Powered by Batch02_Group_39_SKU.
        </footer>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script>
<script>
    // Particle background
    particlesJS("particles-js", {
        particles: {
            number: { value: 50, density: { enable: true, value_area: 1000 } },
            color: { value: "#ffffff" },
            shape: { type: "circle", stroke: { width: 0, color: "#000000" } },
            opacity: { value: 0.2, random: true },
            size: { value: 2, random: true },
            line_linked: { enable: false },
            move: { enable: true, speed: 1, direction: "none", random: true, straight: false, out_mode: "out", bounce: false }
        },
        interactivity: {
            detect_on: "canvas",
            events: { onhover: { enable: false }, onclick: { enable: false }, resize: true },
            modes: {}
        },
        retina_detect: true
    });

    // Sidebar navigation
    const links = document.querySelectorAll('.sidebar a');
    const sections = document.querySelectorAll('.main-content .card');

    links.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetId = link.getAttribute('href').substring(1);

            // Update active link
            links.forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            // Show/hide sections with smooth transition
            sections.forEach(section => {
                section.style.opacity = '0';
                section.style.transform = 'translateY(15px)';
                section.style.display = 'none';
                if (section.id === targetId) {
                    section.style.display = 'block';
                    setTimeout(() => {
                        section.style.opacity = '1';
                        section.style.transform = 'translateY(0)';
                    }, 50);

                    // Load tickets or notifications if needed
                    if (targetId === 'ticket-history') {
                        loadTicketHistory();
                    } else if (targetId === 'notifications') {
                        loadNotifications();
                    }
                }
            });
        });
    });

    // Show profile section by default
    document.getElementById('profile').style.display = 'block';
    document.getElementById('profile').style.opacity = '1';

    // Password validation
    function validatePasswordForm() {
        const newPassword = document.getElementById('newPassword-password').value;
        const confirmPassword = document.getElementById('confirmPassword-password').value;
        const newPasswordError = document.getElementById('newPasswordError-password');
        const confirmPasswordError = document.getElementById('confirmPasswordError-password');
        let isValid = true;

        // Reset error messages
        newPasswordError.style.display = 'none';
        confirmPasswordError.style.display = 'none';

        // Password strength check
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        if (!passwordRegex.test(newPassword)) {
            newPasswordError.textContent = 'Password must be at least 8 characters long and contain at least one letter and one number.';
            newPasswordError.style.display = 'block';
            isValid = false;
        }

        // Confirm password match
        if (newPassword !== confirmPassword) {
            confirmPasswordError.textContent = 'Passwords do not match.';
            confirmPasswordError.style.display = 'block';
            isValid = false;
        }

        return isValid;
    }

    // Ticket filtering
    const ticketSearch = document.getElementById('ticket-search');
    const statusFilter = document.getElementById('status-filter');
    const ticketRows = document.querySelectorAll('#ticket-history-body tr');

    function filterTickets() {
        const searchTerm = ticketSearch.value.toLowerCase();
        const status = statusFilter.value;

        ticketRows.forEach(row => {
            const title = row.getAttribute('data-title');
            const description = row.getAttribute('data-description');
            const rowStatus = row.getAttribute('data-status');

            const matchesSearch = title.includes(searchTerm) || description.includes(searchTerm);
            const matchesStatus = !status || rowStatus === status;

            row.style.display = matchesSearch && matchesStatus ? '' : 'none';
        });
    }

    ticketSearch.addEventListener('input', filterTickets);
    statusFilter.addEventListener('change', filterTickets);

    // Dynamic ticket history loading (placeholder for AJAX)
    function loadTicketHistory() {
        filterTickets(); // Re-apply filters when loading
    }

    // Dynamic notifications loading (placeholder for AJAX)
    function loadNotifications() {
        // Implement AJAX call to /getNotifications if needed
    }
</script>
</body>
</html>