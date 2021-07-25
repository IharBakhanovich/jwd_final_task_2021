<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>UsersPage</title>
</head>
<body>
<h2>User List</h2>
<c:if test="${not empty requestScope.users}">
    <h3>Users</h3>
    <tr>
        <th>UserId</th>
        <th>Nickname</th>
        <th>Role</th>
    </tr>
    <br>
    <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.id}</a>
            </td>
            <td>${user.nickname}</td>
            <td>${user.role}</td>
        </tr>
        <br>
    </c:forEach>
</c:if>
<br>
<br>
<c:if test="${not empty sessionScope.userName and sessionScope.userRole eq Role.ADMIN}">
    <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user">Create new user</a>
</c:if>
</body>
</html>
