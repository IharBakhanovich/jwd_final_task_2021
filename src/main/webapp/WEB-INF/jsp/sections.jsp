<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Sections</title>
</head>
<body>
<h2>Sections</h2>
<c:choose>
    <c:when test="${not empty requestScope.sections}">
        <h3>Available sections of '${requestScope.conferenceTitle}' conference</h3>
        <tr>
            <th>ID</th>
            <th>SectionName</th>
            <th>Manager</th>
            <th>Update section reference</th>
        </tr>

        <br>
        <c:forEach var="section" items="${requestScope.sections}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_reports&id=${section.id}&sectionName=${section.sectionName}&conferenceId=${requestScope.conferenceId}">${section.id}</a>
                </td>
                <td>${section.sectionName}</td>
                <c:forEach var="user" items="${requestScope.users}">
                    <c:if test="${section.managerSect==user.id}">
                        <td>
                            <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                        </td>
                    </c:if>
                </c:forEach>
                <c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.conferenceManager or sessionScope.userId == section.managerSect}">
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=show_update_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${section.id}&conferenceManagerId=${requestScope.conferenceManager}">Update
                            '${section.sectionName}'</a>
                    </td>
                </c:if>
            </tr>
            <br>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <p class="error_message">There are no sections in this conference</p>
    </c:otherwise>
</c:choose>
<br>
<c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.conferenceManager}">
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=show_create_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}">Create
        New Section in '${requestScope.conferenceTitle}' conference</a>
</c:if>

<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the main page</a>
</body>
</html>
