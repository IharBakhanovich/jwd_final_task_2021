<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
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

<h2>Conference List:</h2>
<c:if test="${not empty requestScope.conferences}">
    <h3>Conferences</h3>
    <tr>
        <th>ConferenceID</th>
        <th>ConferenceTitle</th>
    </tr>
    <c:forEach var="conference" items="${requestScope.conferences}">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_sections?id=${conference.id}?conferenceTitle=${conference.conferenceTitle}">${conference.id}</a>
            </td>
            <td>${conference.conferenceTitle}</td>
        </tr>
    </c:forEach>

    <ul>
        <c:forEach var="conference" items="${requestScope.conferences}">
            <a href="${pageContext.request.contextPath}/controller?command=show_sections?id=${conference.id}?conferenceTitle=${conference.conferenceTitle}">${conference.id}</a>
            <li>${conference.conferenceTitle}</li>
        </c:forEach>
    </ul>

</c:if>

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
