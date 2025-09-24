<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard - HR Help Desk</title>
  <style>
    /* Same styles as provided in Code 2 */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
    }

    body {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: #121212;
      overflow-x: hidden;
      position: relative;
    }

    .container {
      padding: 40px;
      max-width: 1400px;
      width: 90%;
      background: rgba(30, 30, 30, 0.95);
      backdrop-filter: blur(8px);
      border-radius: 16px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
      animation: fadeIn 0.8s ease-out;
      position: relative;
      z-index: 2;
      margin: 40px auto;
    }

    @keyframes fadeIn {
      0% { opacity: 0; transform: translateY(20px); }
      100% { opacity: 1; transform: translateY(0); }
    }

    h1 {
      font-size: 2.5em;
      color: #ffffff;
      font-weight: 600;
      text-align: center;
      margin-bottom: 40px;
      letter-spacing: 0.5px;
    }

    h2 {
      font-size: 1.6em;
      color: #e0e0e0;
      font-weight: 500;
      margin-bottom: 24px;
      position: relative;
    }

    h2::after {
      content: '';
      position: absolute;
      bottom: -4px;
      left: 0;
      width: 40px;
      height: 2px;
      background: #3b82f6;
      border-radius: 2px;
    }

    .form-container, .table-container {
      background: rgba(18, 18, 18, 0.87);
      border-radius: 12px;
      padding: 32px;
      margin-bottom: 32px;
      border: 1px solid rgba(255, 255, 255, 0.05);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .form-container:hover, .table-container:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 24px rgba(0, 0, 0, 0.4);
    }

    .form-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 24px;
    }

    label {
      font-size: 0.95em;
      color: #b0b0b0;
      font-weight: 400;
      display: block;
      margin-bottom: 8px;
    }

    input, select {
      width: 100%;
      padding: 12px 16px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 8px;
      background: rgba(255, 255, 255, 0.05);
      color: #ffffff;
      font-size: 0.95em;
      transition: border-color 0.3s ease, box-shadow 0.3s ease;
    }

    select {
      background: transparent;
      appearance: none;
      padding-right: 32px;
      background-image: url('data:image/svg+xml;utf8,<svg fill="%23b0b0b0" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>');
      background-repeat: no-repeat;
      background-position: right 8px center;
    }

    input:focus, select:focus {
      outline: none;
      border-color: #3b82f6;
      box-shadow: 0 0 8px rgba(59, 130, 246, 0.3);
    }

    button {
      padding: 12px 32px;
      background: #3b82f6;
      color: #ffffff;
      font-size: 0.95em;
      font-weight: 500;
      text-transform: uppercase;
      border-radius: 8px;
      border: none;
      cursor: pointer;
      transition: background 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
      display: block;
      margin: 24px auto 0;
    }

    button:hover {
      background: #2563eb;
      transform: translateY(-2px);
      box-shadow: 0 8px 16px rgba(59, 130, 246, 0.3);
    }

    .remove-button {
      background: #ef4444;
    }

    .remove-button:hover {
      background: #dc2626;
      box-shadow: 0 8px 16px rgba(239, 68, 68, 0.3);
    }

    .update-button {
      background: #10b981;
    }

    .update-button:hover {
      background: #059669;
      box-shadow: 0 8px 16px rgba(16, 185, 129, 0.3);
    }

    table {
      width: 100%;
      border-collapse: collapse;
      color: #d0d0d0;
      font-size: 0.9em;
    }

    th, td {
      padding: 16px;
      text-align: left;
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    }

    th {
      background: rgba(255, 255, 255, 0.02);
      font-size: 0.85em;
      text-transform: uppercase;
      color: #3b82f6;
      font-weight: 500;
      letter-spacing: 0.5px;
    }

    td {
      font-size: 0.9em;
    }

    tr {
      transition: background 0.3s ease;
    }

    tr:hover {
      background: rgba(255, 255, 255, 0.04);
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

    .error-message {
      color: #ef4444;
      text-align: center;
      margin-bottom: 16px;
      font-size: 0.95em;
    }

    @media (max-width: 768px) {
      h1 {
        font-size: 2em;
      }

      h2 {
        font-size: 1.4em;
      }

      .container {
        padding: 24px;
        margin: 16px;
      }

      .form-grid {
        grid-template-columns: 1fr;
      }

      input, select, button {
        font-size: 0.9em;
        padding: 10px 14px;
      }

      th, td {
        padding: 12px;
        font-size: 0.85em;
      }

      .logout-btn {
        font-size: 0.85em;
        padding: 8px 16px;
      }
    }

    h3 {
      color: white;
    }

    @media (max-width: 480px) {
      h1 {
        font-size: 1.8em;
      }

      h2 {
        font-size: 1.2em;
      }

      .container {
        padding: 16px;
      }

      input, select, button {
        font-size: 0.85em;
      }

      th, td {
        padding: 10px;
        font-size: 0.8em;
      }
    }
  </style>
  <script>
    function toggleDepartmentField() {
      var roleId = document.getElementById("role_id").value;
      var deptSelect = document.getElementById("dept_id");
      deptSelect.disabled = (roleId == "5"); // Disable if Management role (role_id = 5)
      if (roleId == "5") {
        deptSelect.value = ""; // Clear department if Management
      }
    }

    function toggleUpdateDepartmentField() {
      var roleId = document.getElementById("update_role_id").value;
      var deptSelect = document.getElementById("update_dept_id");
      deptSelect.disabled = (roleId == "5"); // Disable if Management role (role_id = 5)
      if (roleId == "5") {
        deptSelect.value = ""; // Clear department if Management
      }
    }

    function validatePhoneNumbers(phoneInput) {
      const phonePattern = /^(07\d{8})(, ?(07\d{8}))*$/;
      if (phoneInput.value && !phonePattern.test(phoneInput.value)) {
        alert("Please enter valid Sri Lankan mobile numbers (e.g., 0772343345 or 077-123-4567, but use plain digits for simplicity, comma-separated for multiple).");
        phoneInput.focus();
        return false;
      }
      return true;
    }

    // Attach validation to form submissions
    document.addEventListener('DOMContentLoaded', function () {
      const addForm = document.querySelector('form[action="adminDashboard"][method="post"]');
      const updateForm = document.querySelector('form[action="adminDashboard"][method="post"] .update-button').form;

      addForm.addEventListener('submit', function (event) {
        const phoneInput = document.getElementById('phone_numbers');
        if (!validatePhoneNumbers(phoneInput)) {
          event.preventDefault();
        }
      });

      if (updateForm) {
        updateForm.addEventListener('submit', function (event) {
          const phoneInput = document.getElementById('update_phone_numbers');
          if (!validatePhoneNumbers(phoneInput)) {
            event.preventDefault();
          }
        });
      }
    });
  </script>
</head>
<body>
<div id="particles-js"></div>
<div class="container">
  <a href="logout" class="logout-btn">Logout</a>
  <h1>Admin Dashboard</h1>

  <!-- Error Message Display -->
  <c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
  </c:if>

  <!-- Add User Form -->
  <div class="form-container">
    <h2>Add New User</h2>
    <form action="adminDashboard" method="post" class="form-grid">
      <input type="hidden" name="action" value="add">
      <div>
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>
      </div>
      <div>
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
      </div>
      <div>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div>
        <label for="first_name">First Name</label>
        <input type="text" id="first_name" name="first_name" required>
      </div>
      <div>
        <label for="last_name">Last Name</label>
        <input type="text" id="last_name" name="last_name" required>
      </div>
      <div>
        <label for="dept_id">Department</label>
        <select id="dept_id" name="dept_id">
          <option value="">None</option>
          <c:forEach var="dept" items="${departments}">
            <option value="${dept.deptId}">${dept.deptName}</option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label for="job_title">Job Title</label>
        <select id="job_title" name="job_title" required>
          <option value="">Select Job Title</option>
          <c:forEach var="jobTitle" items="${jobTitles}">
            <option value="${jobTitle}">${jobTitle}</option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label for="phone_numbers">Phone Numbers (comma-separated)</label>
        <input type="text" id="phone_numbers" name="phone_numbers" placeholder="e.g., 0772343345 ,077-123-4567" title="Enter valid phone numbers, comma-separated (e.g., 077-7676777)" pattern="^(\+?\d{1,3}[-]?\d{3}[-]?\d{3}[-]?\d{4}(,\s*\+?\d{1,3}[-]?\d{3}[-]?\d{3}[-]?\d{4})*)$">
      </div>
      <div>
        <label for="address">Address</label>
        <input type="text" id="address" name="address">
      </div>
      <div>
        <label for="role_id">Role</label>
        <select id="role_id" name="role_id" required onchange="toggleDepartmentField()">
          <option value="">Select Role</option>
          <c:forEach var="role" items="${roles}">
            <option value="${role.roleId}">${role.roleName}</option>
          </c:forEach>
        </select>
      </div>
      <button type="submit">Add User</button>
    </form>
  </div>

  <!-- Search User Form -->
  <div class="form-container">
    <h2>Search, Update, or Remove User</h2>
    <form action="adminDashboard" method="post" class="form-grid">
      <input type="hidden" name="action" value="search">
      <div>
        <label for="employee_id">Employee ID</label>
        <input type="text" id="employee_id" name="employee_id" required placeholder="e.g., EM0001">
      </div>
      <button type="submit">Search User</button>
    </form>

    <!-- User Details, Update, and Removal Forms (Displayed after search) -->
    <c:if test="${not empty searchedUser}">
      <div class="table-container" style="margin-top: 24px;">
        <h3>User Details</h3>
        <table>
          <thead>
          <tr>
            <th>Employee ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Department</th>
            <th>Job Title</th>
            <th>Phone Numbers</th>
            <th>Address</th>
            <th>Role</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>${searchedUser.employeeId}</td>
            <td>${searchedUser.username}</td>
            <td>${searchedUser.email}</td>
            <td>${searchedUser.firstName}</td>
            <td>${searchedUser.lastName}</td>
            <td>${searchedUser.deptName != null ? searchedUser.deptName : 'N/A'}</td>
            <td>${searchedUser.jobTitle}</td>
            <td>${searchedUser.phoneNumbers != null ? searchedUser.phoneNumbers : 'N/A'}</td>
            <td>${searchedUser.address}</td>
            <td>
              <c:forEach var="role" items="${roles}">
                <c:if test="${role.roleId == searchedUser.roleId}">${role.roleName}</c:if>
              </c:forEach>
            </td>
          </tr>
          </tbody>
        </table>
        <!-- Update User Form -->
        <h3 style="margin-top: 24px;">Update User Details</h3>
        <form action="adminDashboard" method="post" class="form-grid">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="user_id" value="${searchedUser.userId}">
          <div>
            <label for="update_first_name">First Name</label>
            <input type="text" id="update_first_name" name="first_name" value="${searchedUser.firstName}" required>
          </div>
          <div>
            <label for="update_last_name">Last Name</label>
            <input type="text" id="update_last_name" name="last_name" value="${searchedUser.lastName}" required>
          </div>
          <div>
            <label for="update_email">Email</label>
            <input type="email" id="update_email" name="email" value="${searchedUser.email}" required>
          </div>
          <div>
            <label for="update_dept_id">Department</label>
            <select id="update_dept_id" name="dept_id">
              <option value="">None</option>
              <c:forEach var="dept" items="${departments}">
                <option value="${dept.deptId}" <c:if test="${dept.deptId == searchedUser.deptId}">selected</c:if>>${dept.deptName}</option>
              </c:forEach>
            </select>
          </div>
          <div>
            <label for="update_job_title">Job Title</label>
            <select id="update_job_title" name="job_title" required>
              <option value="">Select Job Title</option>
              <c:forEach var="jobTitle" items="${jobTitles}">
                <option value="${jobTitle}" <c:if test="${jobTitle == searchedUser.jobTitle}">selected</c:if>>${jobTitle}</option>
              </c:forEach>
            </select>
          </div>
          <div>
            <label for="update_phone_numbers">Phone Numbers (comma-separated)</label>
            <input type="text" id="update_phone_numbers" name="phone_numbers" value="${searchedUser.phoneNumbers != null ? searchedUser.phoneNumbers : ''}" placeholder="e.g., 0772343345 , 077-123-4567" title="Enter valid phone numbers, comma-separated (e.g., 0772343345 )" pattern="^(\+?\d{1,3}[-]?\d{3}[-]?\d{3}[-]?\d{4}(,\s*\+?\d{1,3}[-]?\d{3}[-]?\d{3}[-]?\d{4})*)$">
          </div>
          <div>
            <label for="update_address">Address</label>
            <input type="text" id="update_address" name="address" value="${searchedUser.address}">
          </div>
          <div>
            <label for="update_role_id">Role</label>
            <select id="update_role_id" name="role_id" required onchange="toggleUpdateDepartmentField()">
              <option value="">Select Role</option>
              <c:forEach var="role" items="${roles}">
                <option value="${role.roleId}" <c:if test="${role.roleId == searchedUser.roleId}">selected</c:if>>${role.roleName}</option>
              </c:forEach>
            </select>
          </div>
          <button type="submit" class="update-button">Update User</button>
        </form>
        <!-- Removal Confirmation Form -->
        <h3 style="margin-top: 24px;">Remove User</h3>
        <form action="adminDashboard" method="post" class="form-grid">
          <input type="hidden" name="action" value="remove">
          <input type="hidden" name="user_id" value="${searchedUser.userId}">
          <div>
            <label for="reason">Reason for Removal</label>
            <select id="reason" name="reason" required>
              <option value="">Select Reason</option>
              <option value="Employee leaves the company">Employee leaves the company</option>
              <option value="User tries to hack or damage the system">User tries to hack or damage the system</option>
              <option value="User sends fake or spam requests">User sends fake or spam requests</option>
              <option value="User uses bad or rude words">User uses bad or rude words</option>
              <option value="Account not used for a long time">Account not used for a long time</option>
              <option value="Management asks to remove">Management asks to remove</option>
            </select>
          </div>
          <button type="submit" class="remove-button">Confirm Removal</button>
        </form>
      </div>
    </c:if>
  </div>

  <!-- Active User List Table -->
  <div class="table-container">
    <h2>Active User List</h2>
    <table>
      <thead>
      <tr>
        <th>Employee ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Department</th>
        <th>Job Title</th>
        <th>Phone Numbers</th>
        <th>Role</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${activeUsers}">
        <tr>
          <td>${user.employeeId}</td>
          <td>${user.username}</td>
          <td>${user.email}</td>
          <td>${user.firstName}</td>
          <td>${user.lastName}</td>
          <td>${user.deptName != null ? user.deptName : 'N/A'}</td>
          <td>${user.jobTitle}</td>
          <td>${user.phoneNumbers != null ? user.phoneNumbers : 'N/A'}</td>
          <td>
            <c:forEach var="role" items="${roles}">
              <c:if test="${role.roleId == user.roleId}">${role.roleName}</c:if>
            </c:forEach>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <!-- Removed User List Table -->
  <div class="table-container">
    <h2>Removed User List</h2>
    <table>
      <thead>
      <tr>
        <th>Employee ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Department</th>
        <th>Job Title</th>
        <th>Phone Numbers</th>
        <th>Role</th>
        <th>Deletion Reason</th>
        <th>Deleted At</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${removedUsers}">
        <tr>
          <td>${user.employeeId}</td>
          <td>${user.username}</td>
          <td>${user.email}</td>
          <td>${user.firstName}</td>
          <td>${user.lastName}</td>
          <td>${user.deptName != null ? user.deptName : 'N/A'}</td>
          <td>${user.jobTitle}</td>
          <td>${user.phoneNumbers != null ? user.phoneNumbers : 'N/A'}</td>
          <td>
            <c:forEach var="role" items="${roles}">
              <c:if test="${role.roleId == user.roleId}">${role.roleName}</c:if>
            </c:forEach>
          </td>
          <td>${user.deletionReason}</td>
          <td>${user.deletedAt}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/particles.js@2.0.0/particles.min.js"></script>
<script>
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
</script>
</body>
</html>