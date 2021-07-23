<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
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
    </c:forEach>
</c:if>
</body>
</html>
