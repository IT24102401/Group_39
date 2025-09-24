<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hrhelpdesk.model.TicketCategory" %>
<%@ page import="com.hrhelpdesk.model.User" %>
<%@ page import="com.hrhelpdesk.model.Department" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit HR Ticket - HR Help Desk</title>
    <!-- Flatpickr CSS for beautiful calendar -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <style>
        /* Modernized dark theme */
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif; }
        body { min-height: 100vh; background: #121212; color: #ffffff; padding: 40px; }
        .container { max-width: 900px; margin: 0 auto; background: rgba(30, 30, 30, 0.95); padding: 40px; border-radius: 20px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2); }
        h1 { font-size: 2.5em; margin-bottom: 30px; font-weight: 600; color: #ffffff; text-align: center; }
        .form-group { margin-bottom: 25px; }
        label { display: block; color: #b0b0b0; margin-bottom: 10px; font-size: 1.1em; font-weight: 500; }

        /* Input and Textarea Styling */
        input, textarea {
            width: 100%;
            padding: 14px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            border-radius: 10px;
            font-size: 1em;
            transition: border-color 0.3s ease, background 0.3s ease;
        }
        input:focus, textarea:focus {
            outline: none;
            border-color: #60a5fa;
            background: rgba(255, 255, 255, 0.1);
        }
        textarea { resize: vertical; min-height: 100px; }

        /* Transparent and Modern Select Boxes */
        select {
            width: 100%;
            padding: 14px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            border-radius: 10px;
            font-size: 1em;
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' fill='%23ffffff' viewBox='0 0 24 24'%3E%3Cpath d='M7 10l5 5 5-5z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 1rem center;
            background-size: 12px;
            transition: border-color 0.3s ease, background 0.3s ease;
        }
        select:focus {
            outline: none;
            border-color: #60a5fa;
            background: rgba(255, 255, 255, 0.1);
        }
        select option { background: #1e1e1e; color: #ffffff; }

        /* Filterable Dropdown Styling */
        .filterable-select { position: relative; }
        .filter-input {
            width: 100%;
            padding: 14px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            background: rgba(255, 255, 255, 0.05);
            color: #ffffff;
            border-radius: 10px;
            font-size: 1em;
        }
        .filter-input:focus {
            outline: none;
            border-color: #60a5fa;
            background: rgba(255, 255, 255, 0.1);
        }
        .options-list {
            max-height: 200px;
            overflow-y: auto;
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 10px;
            background: rgba(35, 35, 35, 0.85);
            position: absolute;
            width: 100%;
            z-index: 10;
            display: none;
            top: 100%;
            left: 0;
        }
        .options-list div {
            padding: 10px;
            cursor: pointer;
        }
        .options-list div:hover {
            background: #3b82f6;
        }

        /* Button Styling */
        .back-btn {
            position: absolute;
            top: 20px;
            left: 20px;
            padding: 10px 24px;
            background: #3b82f6;
            color: #ffffff;
            font-size: 0.9em;
            font-weight: 500;
            border-radius: 8px;
            text-decoration: none;
            transition: background 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
        }
        .back-btn:hover {
            background: #2563eb;
            transform: translateY(-2px);
            box-shadow: 0 8px 16px rgba(59, 130, 246, 0.3);
        }
        .btn {
            padding: 14px 40px;
            background: #3b82f6;
            color: #ffffff;
            border: none;
            cursor: pointer;
            border-radius: 10px;
            font-size: 1.1em;
            font-weight: 500;
            transition: background 0.3s ease, transform 0.2s ease;
        }
        .btn:hover { background: #2563eb; transform: translateY(-2px); }
        .btn:active { transform: translateY(0); }

        /* Specific Fields */
        .specific-fields { display: none; }

        /* Error Message */
        .error-message { color: #ef4444; margin-bottom: 20px; text-align: center; font-size: 1.1em; }

        /* Flatpickr Calendar Customization */
        .flatpickr-calendar {
            background: rgba(30, 30, 30, 0.95);
            color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
            width: 350px;
        }
        .flatpickr-day { color: #ffffff; }
        .flatpickr-day:hover { background: #3b82f6; }
        .flatpickr-day.selected { background: #2563eb; border-color: #2563eb; }
        .flatpickr-monthDropdown-months, .flatpickr-year { color: #ffffff; background: rgba(255, 255, 255, 0.05); }
        .flatpickr-prev-month, .flatpickr-next-month { fill: #ffffff; }
        .flatpickr-current-month { color: #ffffff; }
    </style>
</head>
<body>
<div class="main-content">
    <a href="javascript:history.back()" class="back-btn"><i class="fas fa-arrow-left" style="margin-right: 8px;"></i> Back</a>
    <a href="logout" class="logout-btn"><i class="fas fa-sign-out-alt" style="margin-right: 8px;"></i> Logout</a>
</div>
<div class="container">
    <h1>Submit New Ticket</h1>
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
    <form action="submitTicket" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="category">Category</label>
            <select id="category" name="category" required onchange="showSpecificFields()">
                <option value="">Select Category</option>
                <% List<TicketCategory> categories = (List<TicketCategory>) request.getAttribute("categories"); %>
                <% for (TicketCategory cat : categories) { %>
                <option value="<%= cat.getCategoryId() %>" data-isa="<%= cat.getIsaTableName() %>"><%= cat.getCategoryName() %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" id="title" name="title" required>
        </div>
        <div class="form-group">
            <label for="priority">Priority</label>
            <select id="priority" name="priority" required>
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High(Sensitive)</option>
            </select>
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" required></textarea>
        </div>

        <!-- Specific fields for each category -->
        <div id="leave-fields" class="specific-fields">
            <div class="form-group">
                <label for="leave_type">Leave Type</label>
                <select id="leave_type" name="leave_type">
                    <option value="Casual">Casual Leave</option>
                    <option value="Sick">Sick Leave</option>
                    <option value="Annual">Annual Leave</option>
                    <option value="Vacation">Vacation Leave</option>
                    <option value="Maternity">Maternity Leave</option>
                    <option value="Paternity">Paternity Leave</option>
                    <option value="Bereavement">Bereavement Leave</option>
                    <option value="Compensatory">Compensatory Leave</option>
                    <option value="Unpaid">Unpaid Leave</option>
                    <option value="Other">Other</option>
                </select>
            </div>
            <div class="form-group">
                <label for="start_date">Start Date</label>
                <input type="text" id="start_date" name="start_date" class="flatpickr" placeholder="Select Start Date">
            </div>
            <div class="form-group">
                <label for="end_date">End Date</label>
                <input type="text" id="end_date" name="end_date" class="flatpickr" placeholder="Select End Date">
            </div>
        </div>

        <div id="salary-fields" class="specific-fields">
            <div class="form-group">
                <label for="month_year">Month/Year (e.g., 08-2025)</label>
                <input type="text" id="month_year" name="month_year">
            </div>
            <div class="form-group">
                <label for="issue_details">Issue Details</label>
                <textarea id="issue_details" name="issue_details"></textarea>
            </div>
        </div>

        <div id="complaint-fields" class="specific-fields">
            <div class="form-group">
                <label for="against_user_id">Against User</label>
                <div class="filterable-select">
                    <input type="text" id="user_filter" class="filter-input" placeholder="Type to filter users...">
                    <input type="hidden" id="against_user_id" name="against_user_id">
                    <div id="user_options" class="options-list">
                        <% List<User> users = (List<User>) request.getAttribute("users"); %>
                        <% for (User u : users) { %>
                        <div data-value="<%= u.getUserId() %>" data-name="<%= u.getFirstName() + " " + u.getLastName() %> (<%= u.getUsername() %>)"><%= u.getFirstName() + " " + u.getLastName() %> (<%= u.getUsername() %>)</div>
                        <% } %>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="severity">Severity</label>
                <select id="severity" name="severity">
                    <option value="Low">Low</option>
                    <option value="Medium">Medium</option>
                    <option value="High">High(Sensitive)</option>
                </select>
            </div>
        </div>

        <div id="service-letter-fields" class="specific-fields">
            <div class="form-group">
                <label for="letter_type">Letter Type</label>
                <select id="letter_type" name="letter_type">
                    <option value="Experience">Experience</option>
                    <option value="Relieving">Relieving</option>
                    <option value="Recommendation">Recommendation</option>
                </select>
            </div>
            <div class="form-group">
                <label for="recipient_name">Recipient Name</label>
                <input type="text" id="recipient_name" name="recipient_name">
            </div>
        </div>

        <div id="id-card-fields" class="specific-fields">
            <div class="form-group">
                <label for="card_issue_type">Card Issue Type</label>
                <select id="card_issue_type" name="card_issue_type">
                    <option value="New">New</option>
                    <option value="Replacement">Replacement</option>
                    <option value="Lost">Lost</option>
                </select>
            </div>
            <div class="form-group">
                <label for="details">Details</label>
                <textarea id="details" name="details"></textarea>
            </div>
        </div>

        <div id="promotion-transfer-fields" class="specific-fields">
            <div class="form-group">
                <label for="request_type">Request Type</label>
                <select id="request_type" name="request_type">
                    <option value="Promotion">Promotion</option>
                    <option value="Transfer">Transfer</option>
                </select>
            </div>
            <div class="form-group" id="target-dept-group" style="display: none;">
                <label for="target_dept">Target Department for Transfer</label>
                <select id="target_dept" name="target_dept">
                    <% List<Department> departments = (List<Department>) request.getAttribute("departments"); %>
                    <% for (Department dept : departments) { %>
                    <option value="<%= dept.getDeptId() %>"><%= dept.getDeptName() %></option>
                    <% } %>
                </select>
            </div>
            <div class="form-group">
                <label for="reason">Reason</label>
                <textarea id="reason" name="reason"></textarea>
            </div>
        </div>

        <!-- Attachments -->
        <div class="form-group">
            <label for="attachments">Attach Documents (optional, multiple allowed)</label>
            <input type="file" id="attachments" name="attachments" multiple>
        </div>

        <button type="submit" class="btn">Submit Ticket</button>
    </form>
</div>

<!-- Flatpickr JS -->
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script>
    // Initialize Flatpickr for date inputs
    flatpickr(".flatpickr", {
        dateFormat: "Y-m-d",
        theme: "dark",
        minDate: "today",
        enableTime: false,
        altInput: true,
        altFormat: "F j, Y"
    });

    // Show specific fields based on category
    function showSpecificFields() {
        const categorySelect = document.getElementById('category');
        const selectedOption = categorySelect.options[categorySelect.selectedIndex];
        const isa = selectedOption.getAttribute('data-isa');

        // Hide all specific fields
        document.querySelectorAll('.specific-fields').forEach(el => el.style.display = 'none');

        if (isa) {
            let fieldId;
            switch (isa) {
                case 'LEAVE_TICKET': fieldId = 'leave-fields'; break;
                case 'SALARY_TICKET': fieldId = 'salary-fields'; break;
                case 'COMPLAINT_TICKET': fieldId = 'complaint-fields'; break;
                case 'SERVICE_LETTER_TICKET': fieldId = 'service-letter-fields'; break;
                case 'ID_CARD_TICKET': fieldId = 'id-card-fields'; break;
                case 'PROMOTION_TRANSFER_TICKET': fieldId = 'promotion-transfer-fields'; break;
            }
            if (fieldId) {
                document.getElementById(fieldId).style.display = 'block';
            }
        }
    }

    // Show/hide target department based on request type
    document.getElementById('request_type').addEventListener('change', function() {
        const type = this.value;
        const targetGroup = document.getElementById('target-dept-group');
        if (type === 'Transfer') {
            targetGroup.style.display = 'block';
        } else {
            targetGroup.style.display = 'none';
        }
    });

    // Filterable user dropdown
    const filterInput = document.getElementById('user_filter');
    const userOptions = document.getElementById('user_options');
    const againstUserId = document.getElementById('against_user_id');
    const options = userOptions.querySelectorAll('div');

    // Show dropdown on focus or click
    filterInput.addEventListener('focus', () => {
        userOptions.style.display = 'block';
        filterOptions('');
    });

    // Filter options as user types
    filterInput.addEventListener('input', () => {
        const filterText = filterInput.value.toLowerCase();
        filterOptions(filterText);
    });

    // Filter function
    function filterOptions(filterText) {
        let hasVisibleOptions = false;
        options.forEach(option => {
            const text = option.getAttribute('data-name').toLowerCase();
            option.style.display = text.includes(filterText) ? 'block' : 'none';
            if (text.includes(filterText)) hasVisibleOptions = true;
        });
        userOptions.style.display = hasVisibleOptions ? 'block' : 'none';
    }

    // Select option on click
    options.forEach(option => {
        option.addEventListener('click', () => {
            againstUserId.value = option.getAttribute('data-value');
            filterInput.value = option.getAttribute('data-name');
            userOptions.style.display = 'none';
        });
    });

    // Hide dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!filterInput.contains(e.target) && !userOptions.contains(e.target)) {
            userOptions.style.display = 'none';
        }
    });

    // Prevent form submission on Enter key in filter input
    filterInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
        }
    });
</script>
</body>
</html>