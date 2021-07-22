<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>UsersPage</title>
</head>
<body>
<h2>User List:</h2>
<c:if test="${not empty requestScope.users}">
    <h3>Users</h3>
    <ul>
        <c:forEach var="user" items="${requestScope.users}">
            <li>${user.name}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>
