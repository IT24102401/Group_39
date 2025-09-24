<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Company Management Dashboard - HR Help Desk</title>
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
            text-transform: uppercase;
            letter-spacing: 1px;
            position: relative;
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
        .table-container {
            overflow-x: auto;
        }
        .report-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .report-table th, .report-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
            color: #d0d0d0;
            font-weight: 400;
        }
        .report-table th {
            background: rgba(255, 255, 255, 0.03);
            color: #3b82f6;
            font-weight: 500;
            font-size: 0.85em;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .report-table tr:hover {
            background: rgba(255, 255, 255, 0.06);
        }
        .download-links {
            margin: 10px 0;
        }
        .download-links a {
            color: #3b82f6;
            text-decoration: none;
            margin-right: 15px;
            font-size: 0.9em;
        }
        .download-links a:hover {
            text-decoration: underline;
        }
        .form-container {
            margin-bottom: 20px;
        }
        .form-container label {
            display: inline-block;
            margin-right: 10px;
            color: #d0d0d0;
            font-size: 0.9em;
        }
        .form-container select, .form-container input[type="date"], .form-container input[type="text"] {
            padding: 8px;
            margin-right: 10px;
            border-radius: 4px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            font-size: 0.9em;
        }
        .form-container button {
            padding: 8px 16px;
            background: #3b82f6;
            color: #ffffff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }
        .form-container button:hover {
            background: #2563eb;
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
        canvas {
            max-width: 600px;
            margin: 20px auto;
        }
    </style>
</head>
<body>
<div id="particles-js"></div>
<div class="dashboard-container">
    <div class="sidebar">
        <h2>HR Help Desk</h2>
        <a href="#users-report" class="active">Users Report</a>
        <a href="#tickets-report">Tickets Report</a>
        <a href="#feedback-report">Feedback Report</a>
        <a href="#performance-report">Performance Report</a>
        <a href="#generated-reports">Generated Reports</a>
    </div>
    <div class="main-content">
        <a href="logout" class="logout-btn">Logout</a>
        <h1>Welcome, <c:out value="${sessionScope.user.firstName}"/>!</h1>

        <c:if test="${not empty sessionScope.success}">
            <div class="success-message"><c:out value="${sessionScope.success}"/></div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="error-message"><c:out value="${sessionScope.error}"/></div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div id="users-report" class="card">
            <h3>Users Report</h3>
            <div class="download-links">
                <a href="companyManagementDashboard?download=users&format=csv">Download CSV</a>
                <a href="companyManagementDashboard?download=users&format=pdf">Download PDF</a>
            </div>
            <div class="table-container">
                <table class="report-table">
                    <thead>
                    <tr>
                        <th>Employee ID</th>
                        <th>Username</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Department</th>
                        <th>Status</th>
                        <th>Deleted By</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty users}">
                            <tr><td colspan="8">No users found.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td><c:out value="${user.employeeId}"/></td>
                                    <td><c:out value="${user.username}"/></td>
                                    <td><c:out value="${user.firstName} ${user.lastName}"/></td>
                                    <td><c:out value="${user.email != null ? user.email : 'N/A'}"/></td>
                                    <td><c:out value="${user.roleName}"/></td>
                                    <td><c:out value="${user.deptName != null ? user.deptName : 'N/A'}"/></td>
                                    <td><c:out value="${user.deleted ? 'Removed' : 'Active'}"/></td>
                                    <td><c:out value="${user.deleted && user.deletedByUsername != null ? user.deletedByUsername : 'N/A'}"/></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="tickets-report" class="card" style="display: none;">
            <h3>Tickets Report</h3>
            <div class="download-links">
                <a href="companyManagementDashboard?download=tickets&format=csv">Download CSV</a>
                <a href="companyManagementDashboard?download=tickets&format=pdf">Download PDF</a>
            </div>
            <c:choose>
                <c:when test="${empty ticketsByCategory}">
                    <p>No tickets found.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="entry" items="${ticketsByCategory}">
                        <h4><c:out value="${entry.key}"/></h4>
                        <div class="table-container">
                            <table class="report-table">
                                <thead>
                                <tr>
                                    <th>Ticket ID</th>
                                    <th>Title</th>
                                    <th>Status</th>
                                    <th>Priority</th>
                                    <th>Submitted By</th>
                                    <th>Assigned To</th>
                                    <th>Created At</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="ticket" items="${entry.value}">
                                    <tr>
                                        <td>#${ticket.ticketId}</td>
                                        <td><c:out value="${ticket.title}"/></td>
                                        <td><c:out value="${ticket.status}"/></td>
                                        <td><c:out value="${ticket.priority}"/></td>
                                        <td><c:out value="${ticket.submittedByUsername}"/></td>
                                        <td><c:out value="${ticket.assignedToUsername != null ? ticket.assignedToUsername : 'Unassigned'}"/></td>
                                        <td><fmt:formatDate value="${ticket.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <div id="feedback-report" class="card" style="display: none;">
            <h3>Feedback Report</h3>
            <div class="download-links">
                <a href="companyManagementDashboard?download=feedback&format=csv">Download CSV</a>
                <a href="companyManagementDashboard?download=feedback&format=pdf">Download PDF</a>
            </div>
            <div class="table-container">
                <table class="report-table">
                    <thead>
                    <tr>
                        <th>Feedback ID</th>
                        <th>Message</th>
                        <th>Rating</th>
                        <th>Submitted By</th>
                        <th>Created At</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty feedbackList}">
                            <tr><td colspan="5">No feedback found.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="feedback" items="${feedbackList}">
                                <tr>
                                    <td>#${feedback.feedbackId}</td>
                                    <td><c:out value="${feedback.message}"/></td>
                                    <td>${feedback.rating}/5</td>
                                    <td><c:out value="${feedback.submittedByUsername != null ? feedback.submittedByUsername : 'Unknown'}"/></td>
                                    <td><fmt:formatDate value="${feedback.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="performance-report" class="card" style="display: none;">
            <h3>Performance Report</h3>
            <div class="download-links">
                <a href="companyManagementDashboard?download=performance&format=csv">Download CSV</a>
                <a href="companyManagementDashboard?download=performance&format=pdf">Download PDF</a>
            </div>
            <canvas id="performanceChart"></canvas>
        </div>

        <div id="generated-reports" class="card" style="display: none;">
            <h3>Generated Reports</h3>
            <div class="form-container">
                <form method="post" action="companyManagementDashboard">
                    <label for="reportType">Report Type:</label>
                    <select id="reportType" name="reportType">
                        <option value="Ticket">Ticket Report</option>
                        <option value="Leave">Leave Report</option>
                    </select>
                    <label for="periodFrom">From:</label>
                    <input type="date" id="periodFrom" name="periodFrom" required>
                    <label for="periodTo">To:</label>
                    <input type="date" id="periodTo" name="periodTo" required>
                    <label for="parameters">Parameters (e.g., Dept ID for Leave):</label>
                    <input type="text" id="parameters" name="parameters">
                    <button type="submit">Generate Report</button>
                </form>
            </div>
            <div class="download-links">
                <a href="companyManagementDashboard?download=reports&format=csv">Download All Reports CSV</a>
                <a href="companyManagementDashboard?download=reports&format=pdf">Download All Reports PDF</a>
            </div>
            <div class="table-container">
                <table class="report-table">
                    <thead>
                    <tr>
                        <th>Report ID</th>
                        <th>Type</th>
                        <th>Period From</th>
                        <th>Period To</th>
                        <th>Parameters</th>
                        <th>Generated By</th>
                        <th>Generated At</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty reports}">
                            <tr><td colspan="8">No reports found.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="report" items="${reports}">
                                <tr>
                                    <td>${report.reportId}</td>
                                    <td><c:out value="${report.reportType}"/></td>
                                    <td><fmt:formatDate value="${report.periodFrom}" pattern="yyyy-MM-dd"/></td>
                                    <td><fmt:formatDate value="${report.periodTo}" pattern="yyyy-MM-dd"/></td>
                                    <td><c:out value="${report.parameters != null ? report.parameters : 'N/A'}"/></td>
                                    <td><c:out value="${report.generatedByUsername}"/></td>
                                    <td><fmt:formatDate value="${report.generatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>
                                        <a href="companyManagementDashboard?download=report&reportId=${report.reportId}&format=csv">CSV</a>
                                        <a href="companyManagementDashboard?download=report&reportId=${report.reportId}&format=pdf">PDF</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <footer>
            &copy; <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy"/> HR Help Desk.
        </footer>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
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

    document.getElementById('users-report').style.display = 'block';

    // Performance Chart
    const ctx = document.getElementById('performanceChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [<c:forEach var="entry" items="${performanceMetrics}">'<c:out value="${entry.key}"/>',</c:forEach>],
            datasets: [{
                label: 'Average Resolution Time (Days)',
                data: [<c:forEach var="entry" items="${performanceMetrics}">${entry.value},</c:forEach>],
                backgroundColor: 'rgba(59, 130, 246, 0.6)',
                borderColor: 'rgba(59, 130, 246, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: 'Days', color: '#ffffff' }
                },
                x: {
                    title: { display: true, text: 'Ticket Category', color: '#ffffff' }
                }
            },
            plugins: {
                legend: { labels: { color: '#ffffff' } },
                title: { display: true, text: 'Ticket Resolution Performance', color: '#ffffff' }
            }
        }
    });
</script>
</body>
</html>