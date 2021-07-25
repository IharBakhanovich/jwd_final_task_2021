<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Sections</title>
</head>
<body>
<h2>Sections List</h2>
<c:if test="${not empty requestScope.sections}">
    <h3>Available sections of ${requestScope.conferenceTitle} conference</h3>
    <tr>
        <th>ID</th>
        <th>SectionName</th>
        <th>Manager</th>
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
            </tr>
            <br>
    </c:forEach>
</c:if>
</body>
</html>
