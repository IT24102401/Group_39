<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.Ticket" %>
<%@ page import="com.hrhelpdesk.model.User" %>
<%@ page import="com.hrhelpdesk.model.TicketResponse" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Details - HR Help Desk</title>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: #121212;
            color: #ffffff;
            padding: 20px;
        }
        .main-content {
            max-width: 800px;
            margin: 0 auto;
            background: linear-gradient(135deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95));
            padding: 30px;
            border-radius: 12px;
        }
        h1, h3 {
            color: #a3bffa;
        }
        p, li {
            color: #d0d0d0;
        }
        .btn {
            padding: 10px 20px;
            background: linear-gradient(90deg, #3b82f6, #60a5fa);
            color: #ffffff;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background: linear-gradient(90deg, #2563eb, #3b82f6);
        }
        textarea, select {
            width: 100%;
            padding: 10px;
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            margin-bottom: 10px;
        }
        .success-message, .error-message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 8px;
        }
        .success-message {
            background: rgba(52, 211, 153, 0.1);
            color: #34d399;
        }
        .error-message {
            background: rgba(239, 68, 68, 0.1);
            color: #ef4444;
        }
        .attachment-link {
            color: #60a5fa;
            text-decoration: none;
        }
        .attachment-link:hover {
            text-decoration: underline;
        }
        .back-btn {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="main-content">
    <a href="hrManagerDashboard" class="btn back-btn">Back to Dashboard</a>

    <% Ticket ticket = (Ticket) request.getAttribute("ticket"); %>
    <% User submitter = (User) request.getAttribute("submitter"); %>
    <% if (session.getAttribute("success") != null) { %>
    <div class="success-message"><%= session.getAttribute("success") %></div>
    <% session.removeAttribute("success"); %>
    <% } %>
    <% if (session.getAttribute("error") != null) { %>
    <div class="error-message"><%= session.getAttribute("error") %></div>
    <% session.removeAttribute("error"); %>
    <% } %>

    <h1>Ticket #<%= ticket.getTicketId() %> - <%= ticket.getTitle() %></h1>
    <p><strong>Description:</strong> <%= ticket.getDescription() %></p>
    <p><strong>Status:</strong> <%= ticket.getStatus() %></p>
    <p><strong>Priority:</strong> <%= ticket.getPriority() %></p>
    <p><strong>Category:</strong> <%= ticket.getCategoryName() %></p>
    <p><strong>Created At:</strong> <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(ticket.getCreatedAt()) %></p>

    <h3>Submitter Contact Info</h3>
    <p><strong>Name:</strong> <%= submitter.getFirstName() + " " + submitter.getLastName() %></p>
    <p><strong>Email:</strong> <%= submitter.getEmail() %></p>
    <p><strong>Phone:</strong> <%= submitter.getPhoneNumbers() != null ? submitter.getPhoneNumbers() : "N/A" %></p>
    <p><strong>Department:</strong> <%= submitter.getDeptName() != null ? submitter.getDeptName() : "N/A" %></p>

    <h3>Attachments</h3>
    <% List<String> attachmentNames = (List<String>) request.getAttribute("attachmentNames"); %>
    <% if (attachmentNames != null && !attachmentNames.isEmpty()) { %>
    <ul>
        <% for (String fileName : attachmentNames) { %>
        <li><a href="hrViewAttachment?ticketId=<%= ticket.getTicketId() %>&fileName=<%= java.net.URLEncoder.encode(fileName, "UTF-8") %>" class="attachment-link" target="_blank"><%= fileName %></a></li>
        <% } %>
    </ul>
    <% } else { %>
    <p>No attachments.</p>
    <% } %>

    <h3>Responses</h3>
    <% List<TicketResponse> responses = (List<TicketResponse>) request.getAttribute("responses"); %>
    <% if (responses != null && !responses.isEmpty()) { %>
    <ul>
        <% for (TicketResponse resp : responses) { %>
        <li><strong><%= resp.getRespondedByUsername() %>:</strong> <%= resp.getMessage() %> (<%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(resp.getCreatedAt()) %>)</li>
        <% } %>
    </ul>
    <% } else { %>
    <p>No responses yet.</p>
    <% } %>

    <h3>Update Ticket Status</h3>
    <form action="updateTicketStatus" method="post">
        <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
        <select name="status" required>
            <option value="In Progress" <%= "In Progress".equals(ticket.getStatus()) ? "selected" : "" %>>In Progress</option>
            <option value="Wait Listed" <%= "Wait Listed".equals(ticket.getStatus()) ? "selected" : "" %>>Wait Listed</option>
            <option value="Resolved" <%= "Resolved".equals(ticket.getStatus()) ? "selected" : "" %>>Resolved</option>
        </select>
        <button type="submit" class="btn">Update Status</button>
    </form>

    <% if ("Open".equals(ticket.getStatus())) { %>
    <h3>Assign Ticket</h3>
    <form action="assignTicket" method="post">
        <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
        <select name="assignedTo">
            <% List<User> staff = (List<User>) request.getAttribute("staff"); %>
            <% for (User s : staff) { %>
            <option value="<%= s.getUserId() %>"><%= s.getFirstName() + " " + s.getLastName() %></option>
            <% } %>
        </select>
        <button type="submit" class="btn">Assign to Staff</button>
    </form>
    <% if ("High".equals(ticket.getPriority())) { %>
    <form action="assignTicket" method="post">
        <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
        <input type="hidden" name="assignedTo" value="<%= ((User) session.getAttribute("user")).getUserId() %>">
        <button type="submit" class="btn">Handle Myself</button>
    </form>
    <% } %>
    <% } %>

    <h3>Add Response</h3>
    <form action="addResponse" method="post">
        <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
        <textarea name="message" required></textarea>
        <button type="submit" class="btn">Send Response</button>
    </form>
</div>
</body>
</html>