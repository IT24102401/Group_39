<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Error</title></head>
<body>
<h1>Error</h1>
<p><%= session.getAttribute("error") != null ? session.getAttribute("error") : "An unexpected error occurred." %></p>
<a href="login.jsp">Back to Login</a>
</body>
</html>