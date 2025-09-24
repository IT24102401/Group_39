
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.User" %>
<%@ page import="com.hrhelpdesk.model.Ticket" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Manager Dashboard - HR Help Desk</title>
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
            display: flex;
            flex-direction: column;
            justify-content: space-between;
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
        .main-content {
            flex: 1;
            padding: 40px;
            background: linear-gradient(135deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95));
            backdrop-filter: blur(10px);
            border-radius: 16px;
            margin: 20px;
            color: #ffffff;
            animation: fadeIn 0.8s ease-out;
            position: relative;
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
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.3);
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
            margin: 10px;
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background: linear-gradient(90deg, #2563eb, #3b82f6);
            transform: translateY(-3px);
            box-shadow: 0 6px 20px rgba(59, 130, 246, 0.6);
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
        .notification-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 20px;
            height: 20px;
            background: #ef4444; /* Red background for visibility */
            color: #ffffff; /* White text */
            font-size: 0.8em;
            font-weight: 600;
            border-radius: 10px;
            padding: 0 6px;
            margin-left: 8px;
            transition: transform 0.3s ease;
        }
        .sidebar a:hover .notification-badge {
            transform: scale(1.1); /* Slight scale effect on hover */
        }
        .notification-badge {
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="dashboard-container">
    <div class="sidebar">
        <h2>HR Help Desk</h2>
        <a href="#profile" class="active">Profile Management</a>
        <a href="#staff-list">Staff List</a>
        <a href="#open-tickets">
            Open Tickets
            <% Integer unassignedTicketsCount = (Integer) request.getAttribute("unassignedTicketsCount"); %>
            <% if (unassignedTicketsCount != null && unassignedTicketsCount > 0) { %>
            <span class="notification-badge"><%= unassignedTicketsCount %></span>
            <% } %>
        </a>
        <a href="#in-progress-tickets">In Progress Tickets</a>
        <a href="#resolved-tickets">Resolved Tickets</a>
    </div>
    <div class="main-content">
        <a href="logout" class="logout-btn">Logout</a>
        <h1>Welcome, <%= ((User) session.getAttribute("user")).getFirstName() %>!</h1>

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
            <form action="HRManagerUpdateProfile" method="post">
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
                HR Manager information is not available. Please log in again or contact support.
            </div>
            <% } %>
        </div>

        <div id="staff-list" class="card" style="display: none;">
            <h3>Staff List (HR Staff)</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>User ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Department</th>
                </tr>
                </thead>
                <tbody>
                <% List<User> staff = (List<User>) request.getAttribute("staff"); %>
                <% if (staff != null && !staff.isEmpty()) { %>
                <% for (User s : staff) { %>
                <tr>
                    <td><%= s.getUserId() %></td>
                    <td><%= s.getFirstName() + " " + s.getLastName() %></td>
                    <td><%= s.getEmail() %></td>
                    <td><%= s.getPhoneNumbers() != null ? s.getPhoneNumbers() : "N/A" %></td>
                    <td><%= s.getDeptName() != null ? s.getDeptName() : "N/A" %></td>
                </tr>
                <% } %>
                <% } else { %>
                <tr><td colspan="5">No HR staff found.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div id="open-tickets" class="card" style="display: none;">
            <h3>Open Tickets - High Priority</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Submitted By</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% List<Ticket> openTickets = (List<Ticket>) request.getAttribute("openTickets"); %>
                <% if (openTickets != null && !openTickets.isEmpty()) { %>
                <% for (Ticket ticket : openTickets) { %>
                <% if ("High".equals(ticket.getPriority())) { %>
                <tr>
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getPriority() %></td>
                    <td><%= ticket.getSubmittedByUsername() %></td>
                    <td>
                        <a href="ticketDetail?ticketId=<%= ticket.getTicketId() %>" class="btn">View Details</a>
                        <form action="assignTicket" method="post" style="display: inline;">
                            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
                            <input type="hidden" name="assignedTo" value="<%= ((User) session.getAttribute("user")).getUserId() %>">
                            <button type="submit" class="btn">Handle Myself</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                <% } %>
                <% } else { %>
                <tr><td colspan="5">No high-priority open tickets.</td></tr>
                <% } %>
                </tbody>
            </table>

            <h3 style="margin-top: 30px;">Open Tickets - Medium/Low Priority</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Submitted By</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% if (openTickets != null && !openTickets.isEmpty()) { %>
                <% for (Ticket ticket : openTickets) { %>
                <% if (!"High".equals(ticket.getPriority())) { %>
                <tr>
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getPriority() %></td>
                    <td><%= ticket.getSubmittedByUsername() %></td>
                    <td>
                        <a href="ticketDetail?ticketId=<%= ticket.getTicketId() %>" class="btn">View Details</a>
                        <form action="assignTicket" method="post" style="display: inline;">
                            <input type="hidden" name="ticketId" value="<%= ticket.getTicketId() %>">
                            <select name="assignedTo">
                                <% for (User s : staff) { %>
                                <option value="<%= s.getUserId() %>"><%= s.getFirstName() + " " + s.getLastName() %></option>
                                <% } %>
                            </select>
                            <button type="submit" class="btn">Assign to Staff</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                <% } %>
                <% } else { %>
                <tr><td colspan="5">No medium/low-priority open tickets.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div id="in-progress-tickets" class="card" style="display: none;">
            <h3>In Progress Tickets - High Priority</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Submitted By</th>
                    <th>Assigned To</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% List<Ticket> inProgressTickets = (List<Ticket>) request.getAttribute("inProgressTickets"); %>
                <% if (inProgressTickets != null && !inProgressTickets.isEmpty()) { %>
                <% for (Ticket ticket : inProgressTickets) { %>
                <% if ("High".equals(ticket.getPriority())) { %>
                <tr>
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getPriority() %></td>
                    <td><%= ticket.getSubmittedByUsername() %></td>
                    <td><%= ticket.getAssignedToUsername() != null ? ticket.getAssignedToUsername() : "N/A" %></td>
                    <td>
                        <a href="ticketDetail?ticketId=<%= ticket.getTicketId() %>" class="btn">View/Monitor</a>
                    </td>
                </tr>
                <% } %>
                <% } %>
                <% } else { %>
                <tr><td colspan="6">No high-priority in-progress tickets.</td></tr>
                <% } %>
                </tbody>
            </table>

            <h3 style="margin-top: 30px;">In Progress Tickets - Medium/Low Priority</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Submitted By</th>
                    <th>Assigned To</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% if (inProgressTickets != null && !inProgressTickets.isEmpty()) { %>
                <% for (Ticket ticket : inProgressTickets) { %>
                <% if (!"High".equals(ticket.getPriority())) { %>
                <tr>
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getPriority() %></td>
                    <td><%= ticket.getSubmittedByUsername() %></td>
                    <td><%= ticket.getAssignedToUsername() != null ? ticket.getAssignedToUsername() : "N/A" %></td>
                    <td>
                        <a href="ticketDetail?ticketId=<%= ticket.getTicketId() %>" class="btn">View/Monitor</a>
                    </td>
                </tr>
                <% } %>
                <% } %>
                <% } else { %>
                <tr><td colspan="6">No medium/low-priority in-progress tickets.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div id="resolved-tickets" class="card" style="display: none;">
            <h3>Resolved Tickets</h3>
            <table class="ticket-table">
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Submitted By</th>
                    <th>Assigned To</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% List<Ticket> resolvedTickets = (List<Ticket>) request.getAttribute("resolvedTickets"); %>
                <% if (resolvedTickets != null && !resolvedTickets.isEmpty()) { %>
                <% for (Ticket ticket : resolvedTickets) { %>
                <tr>
                    <td>#<%= ticket.getTicketId() %></td>
                    <td><%= ticket.getTitle() %></td>
                    <td><%= ticket.getPriority() %></td>
                    <td><%= ticket.getSubmittedByUsername() %></td>
                    <td><%= ticket.getAssignedToUsername() != null ? ticket.getAssignedToUsername() : "N/A" %></td>
                    <td>
                        <a href="ticketDetail?ticketId=<%= ticket.getTicketId() %>" class="btn">View Details</a>
                    </td>
                </tr>
                <% } %>
                <% } else { %>
                <tr><td colspan="6">No resolved tickets.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <footer>
            &copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> HR Help Desk.
        </footer>
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

    const links = document.querySelectorAll('.sidebar a');
    const sections = document.querySelectorAll('.main-content .card');

    links.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetId = link.getAttribute('href').substring(1);
            links.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
            sections.forEach(section => {
                section.style.display = section.id === targetId ? 'block' : 'none';
            });
        });
    });

    document.getElementById('profile').style.display = 'block';



    // Password validation
    function validatePasswordForm() {
        const newPassword = document.getElementById('newPassword-password').value;
        const confirmPassword = document.getElementById('confirmPassword-password').value;
        const newPasswordError = document.getElementById('newPasswordError-password');
        const confirmPasswordError = document.getElementById('confirmPasswordError-password');
        let isValid = true;

        newPasswordError.style.display = 'none';
        confirmPasswordError.style.display = 'none';

        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        if (!passwordRegex.test(newPassword)) {
            newPasswordError.textContent = 'Password must be at least 8 characters long and contain at least one letter and one number.';
            newPasswordError.style.display = 'block';
            isValid = false;
        }

        if (newPassword !== confirmPassword) {
            confirmPasswordError.textContent = 'Passwords do not match.';
            confirmPasswordError.style.display = 'block';
            isValid = false;
        }

        return isValid;
    }

    function updateNotificationBadge() {
        fetch('/getUnassignedTicketsCount')
            .then(response => response.json())
            .then(data => {
                const badge = document.querySelector('.notification-badge');
                if (data.count > 0) {
                    badge.textContent = data.count;
                    badge.style.display = 'inline-flex';
                } else {
                    badge.style.display = 'none';
                }
            })
            .catch(error => console.error('Error fetching ticket count:', error));
    }
    setInterval(updateNotificationBadge, 60000); // Update every minute
</script>
</body>
</html>
