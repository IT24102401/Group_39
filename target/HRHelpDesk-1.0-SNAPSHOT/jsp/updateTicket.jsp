<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.Ticket" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Ticket - HR Help Desk</title>
    <style>
        body {
            min-height: 100vh;
            background: #121212;
            color: #ffffff;
            font-family: 'Inter', sans-serif;
            padding: 40px;
        }
        .card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 12px;
            padding: 25px;
            margin: 20px 0;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            color: #b0b0b0;
            margin-bottom: 8px;
        }
        .form-group input, .form-group textarea, .form-group select {
            width: 100%;
            padding: 12px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
        }
        .btn {
            padding: 12px 24px;
            background: #3b82f6;
            color: #ffffff;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }
        .back-link {
            display: inline-block;
            margin-top: 10px;
            color: #60a5fa;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="card">
    <h2>Update Ticket #<%= ((Ticket) request.getAttribute("ticket")).getTicketId() %></h2>
    <form action="updateTicket" method="post" enctype="multipart/form-data">
        <input type="hidden" name="ticketId" value="<%= ((Ticket) request.getAttribute("ticket")).getTicketId() %>">
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" id="title" name="title" value="<%= ((Ticket) request.getAttribute("ticket")).getTitle() %>" required>
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" required><%= ((Ticket) request.getAttribute("ticket")).getDescription() %></textarea>
        </div>
        <div class="form-group">
            <label for="priority">Priority</label>
            <select id="priority" name="priority" required>
                <option value="Low" <%= "Low".equals(((Ticket) request.getAttribute("ticket")).getPriority()) ? "selected" : "" %>>Low</option>
                <option value="Medium" <%= "Medium".equals(((Ticket) request.getAttribute("ticket")).getPriority()) ? "selected" : "" %>>Medium</option>
                <option value="High" <%= "High".equals(((Ticket) request.getAttribute("ticket")).getPriority()) ? "selected" : "" %>>High</option>
            </select>
        </div>
        <div class="form-group">
            <label for="attachment">Add Attachment (Optional)</label>
            <input type="file" id="attachment" name="attachment" accept=".pdf,.doc,.jpg,.png">
        </div>
        <button type="submit" class="btn">Update Ticket</button>
    </form>
    <a href="employeeDashboard#ticket-history" class="back-link">Back to Dashboard</a>
</div>
</body>
</html>