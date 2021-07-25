<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>

<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Main Page</title>
</head>
<body>
<c:choose>
    <c:when test="${empty sessionScope.userName} ">
        <p>Hello.</p>
    </c:when>
    <c:otherwise>
        <p>Hello, ${sessionScope.userName}</p>
    </c:otherwise>
</c:choose>
<!-- show all the conferences in the system-->

<h2>Conference section</h2>
<c:if test="${not empty requestScope.conferences}">
    <h3>Available conferences</h3>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Manager(Nickname)</th>
    </tr>
    <br>
    <c:forEach var="conference" items="${requestScope.conferences}">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}">${conference.id}</a>
            </td>
            <td>${conference.conferenceTitle}</td>
            <c:forEach var="user" items="${requestScope.users}">
                <c:if test="${conference.managerConf==user.id}">
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                    </td>
                </c:if>
            </c:forEach>
        </tr>
        <br>
    </c:forEach>
</c:if>

<h2>Authentication section</h2>
<!-- не хотим показывать незарегистрированным userам-->
<c:choose>
    <c:when test="${empty sessionScope.userName}">

        <!-- ссылка это a тег. pageContext.request.contextPath подставит автоматически contextPath когда таковой появиться -->
        <a href="${pageContext.request.contextPath}/controller?command=show_login">LoginPage</a>
    </c:when>
    <c:otherwise>
        <c:if test="${sessionScope.userRole eq Role.ADMIN}">
            <p>Hello. Click below to see all users</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_users">UsersPage</a>
            <br>
        </c:if>
        <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
    </c:otherwise>
</c:choose>
</body>
</html>
