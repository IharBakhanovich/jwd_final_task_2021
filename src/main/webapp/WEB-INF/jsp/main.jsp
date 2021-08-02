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
    <c:choose>
        <c:when test="${sessionScope.userRole eq Role.ADMIN}">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Manager(Nickname)</th>
                <th>Update conference reference</th>
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
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=show_update_conference&conferenceId=${conference.id}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}">Update '${conference.conferenceTitle}'</a>
                    </td>
                </tr>
                <br>
            </c:forEach>
        </c:when>
        <c:otherwise>
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
        </c:otherwise>
    </c:choose>

</c:if>

<h2>Personal section</h2>
<!-- не хотим показывать незарегистрированным userам-->
<c:choose>
    <c:when test="${empty sessionScope.userName}">

        <!-- ссылка это a тег. pageContext.request.contextPath подставит автоматически contextPath когда таковой появиться -->
        <a href="${pageContext.request.contextPath}/controller?command=show_login">LoginPage</a>
        <br>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user">Register</a>
        <br>
    </c:when>
    <c:otherwise>
        <c:if test="${sessionScope.userRole eq Role.ADMIN}">
            <p>Click below to see all users</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_users">UsersPage</a>

            <br>
            <br>
            <p>Click below to add a new conference</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_create_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}">Create
                conference</a>
        </c:if>

        <br>
        <br>
        <c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">
            <p>Click below to see questions from users</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Show questions</a>
        </c:if>

        <br>
        <br>
        <p>Click below to log out from the system</p>
        <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
    </c:otherwise>
</c:choose>
</body>
</html>
