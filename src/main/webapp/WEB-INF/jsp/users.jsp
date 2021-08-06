<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.usersPage"/></title>
</head>
<body>
<h2><fmt:message key="label.usersTable"/></h2>
<c:if test="${not empty requestScope.users}">
    <h3><fmt:message key="label.users"/></h3>
    <tr class="tableTab">
        <th><fmt:message key="label.userIdColumn"/></th>
        <th><fmt:message key="label.nicknameColumn"/></th>
        <th><fmt:message key="label.roleColumn"/></th>
    </tr>
    <br>
    <c:forEach var="user" items="${requestScope.users}">
        <tr class="tableTab">
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
    <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message key="label.createNewUser"/></a>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
</body>
</html>