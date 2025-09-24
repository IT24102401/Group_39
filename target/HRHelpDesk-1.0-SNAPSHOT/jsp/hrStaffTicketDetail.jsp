<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.Ticket" %>
<%@ page import="com.hrhelpdesk.model.TicketResponse" %>
<%@ page import="com.hrhelpdesk.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Staff Ticket Details - HR Help Desk</title>
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
            position: relative;
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
        .main-content {
            padding: 40px;
            background: linear-gradient(135deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95));
            backdrop-filter: blur(10px);
            border-radius: 16px;
            margin: 20px;
            color: #ffffff;
            position: relative;
            z-index: 2;
        }
        .main-content h1 {
            font-size: 2.5em;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 20px;
        }
        .back-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 10px 24px;
            background: #6b7280;
            color: #ffffff;
            font-size: 0.9em;
            font-weight: 500;
            border-radius: 8px;
            text-decoration: none;
            transition: background 0.3s ease, transform 0.3s ease;
        }
        .back-btn:hover {
            background: #4b5563;
            transform: translateY(-2px);
        }
        .card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 12px;
            padding: 25px;
            margin: 20px 0;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        .card h3 {
            font-size: 1.6em;
            font-weight: 500;
            color: #a3bffa;
            margin-bottom: 15px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .card p {
            font-size: 1em;
            color: #d0d0d0;
            margin-bottom: 10px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            color: #b0b0b0;
            font-size: 0.95em;
            font-weight: 400;
            margin-bottom: 8px;
        }
        .form-group textarea, .form-group select {
            width: 100%;
            padding: 12px 16px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            font-size: 0.95em;
        }
        .form-group textarea:focus, .form-group select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.3);
        }
        .btn {
            padding: 8px 16px;
            background: linear-gradient(90deg, #3b82f6, #60a5fa);
            color: #ffffff;
            font-size: 0.85em;
            font-weight: 500;
            text-transform: uppercase;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            margin: 5px;
        }
        .btn:hover {
            background: linear-gradient(90deg, #2563eb, #3b82f6);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(59, 130, 246, 0.6);
        }
        .success-message, .error-message {
            font-size: 0.9em;
            padding: 12px;
            margin: 10px 0;
            border-radius: 8px;
            text-align: center;
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
        .response-list {
            margin-top: 20px;
        }
        .response-item {
            background: rgba(255, 255, 255, 0.03);
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="main-content">
    <a href="hrStaffDashboard" class="back-btn">Back to Dashboard</a>
    <h1>HR Staff Ticket Details</h1>

    <% if (session.getAttribute("success") != null) { %>
    <div class="success-message" style="display: block;">
        <%= session.getAttribute("success") %>
    </div>
    <% session.removeAttribute("success"); %>
    <% } %>
    <% if (session.getAttribute("error") != null) { %>
    <div class="error-message" style="display: block;">
        <%= session.getAttribute("error") %>
    </div>
    <% session.removeAttribute("error"); %>
    <% } %>

    <div class="card">
        <h3>Ticket #<%= request.getAttribute("ticket") != null ? ((Ticket) request.getAttribute("ticket")).getTicketId() : "N/A" %></h3>
        <% Ticket ticket = (Ticket) request.getAttribute("ticket"); %>
        <% if (ticket != null) { %>
        <p><strong>Title:</strong> <%= ticket.getTitle() %></p>
        <p><strong>Description:</strong> <%= ticket.getDescription() %></p>
        <p><strong>Status:</strong> <%= ticket.getStatus() %></p>
        <p><strong>Priority:</strong> <%= ticket.getPriority() %></p>
        <p><strong>Submitted By:</strong> <%= ticket.getSubmittedByUsername() %></p>
        <p><strong>Category:</strong> <%= ticket.getCategoryName() != null ? ticket.getCategoryName() : "N/A" %></p>
        <p><strong>Created At:</strong> <%= ticket.getCreatedAt() %></p>
        <p><strong>Attachments:</strong>
            <% List<String> attachmentNames = (List<String>) request.getAttribute("attachmentNames"); %>
            <% if (attachmentNames != null && !attachmentNames.isEmpty()) { %>
            <% for (String fileName : attachmentNames) { %>
            <a href="hrViewAttachment?ticketId=<%= ticket.getTicketId() %>&fileName=<%= java.net.URLEncoder.encode(fileName, "UTF-8") %>" target="_blank"><%= fileName %></a><br>
            <% } %>
            <% } else { %>
            None
            <% } %>
        </p>

        <h3>Responses</h3>
        <div class="response-list">
            <% List<TicketResponse> responses = (List<TicketResponse>) request.getAttribute("responses"); %>
            <% if (responses != null && !responses.isEmpty()) { %>
            <% for (TicketResponse ticketResponse : responses) { %>
            <div class="response-item">
                <p><strong><%= ticketResponse.getRespondedByUsername() %>:</strong> <%= ticketResponse.getMessage() %></p>
                <p><small>Posted on: <%= ticketResponse.getCreatedAt() %></small></p>
            </div>
            <% } %>
            <% } else { %>
            <p>No responses yet.</p>
            <% } %>
        </div>

        <h3>Add Response</h3>
        <form action="addResponse" method="post">
            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
            <input type="hidden" name="respondedBy" value="<%= ((User) session.getAttribute("user")).getUserId() %>">
            <div class="form-group">
                <label for="response">Response</label>
                <textarea id="response" name="message" placeholder="Enter your response" required></textarea>
            </div>
            <button type="submit" class="btn">Submit Response</button>
        </form>

        <h3>Update Status</h3>
        <form action="hrStaffUpdateTicketStatus" method="post">
            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status" required>
                    <option value="Open" <%= "Open".equals(ticket.getStatus()) ? "selected" : "" %>>Open</option>
                    <option value="In Progress" <%= "In Progress".equals(ticket.getStatus()) ? "selected" : "" %>>In Progress</option>
                    <option value="Wait Listed" <%= "Wait Listed".equals(ticket.getStatus()) ? "selected" : "" %>>Wait Listed</option>
                    <option value="Resolved" <%= "Resolved".equals(ticket.getStatus()) ? "selected" : "" %>>Resolved</option>
                </select>
            </div>
            <button type="submit" class="btn">Update Status</button>
        </form>
        <% } else { %>
        <div class="error-message" style="display: block;">
            Ticket not found.
        </div>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script>
    particlesJS("particles-js", {
        particles: {
            number: { value: 50, density: { enable: true, value_area: 1000 } },
            color: { value: "#ffffff" },
            shape: { type: "circle" },
            opacity: { value: 0.2, random: true },
            size: { value: 2, random: true },
            line_linked: { enable: false },
            move: { enable: true, speed: 1, direction: "none", random: true, out_mode: "out" }
        },
        interactivity: {
            detect_on: "canvas",
            events: { onhover: { enable: false }, onclick: { enable: false }, resize: true }
        }
    });
</script>
</body>
</html>