<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User page</title>
</head>
<body>
<c:if test="${not empty requestScope.user}">
    <h3>User</h3>
    <tr>
        <th>Id</th>
        <th>Email</th>
        <th>Nickname</th>
        <th>FirstName</th>
        <th>SurName</th>
        <th>Role</th>
    </tr>
    <br>
        <tr>
            <td>${requestScope.user.get().id}</td>
            <td>${requestScope.user.get().email}</td>
            <td>${requestScope.user.get().nickname}</td>
            <td>${requestScope.user.get().firstName}</td>
            <td>${requestScope.user.get().surname}</td>
            <td>${requestScope.user.get().role}</td>
        </tr>
</c:if>
<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the future</a>
</body>
</html>
