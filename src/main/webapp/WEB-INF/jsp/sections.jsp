<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.sections"/></title>
</head>
<body>
<h2><fmt:message key="label.sections"/></h2>
<c:choose>
    <c:when test="${not empty requestScope.sections}">
        <h3><fmt:message key="label.avalibleSectionInConferencePart1"/> '${requestScope.conferenceTitle}' <fmt:message key="label.avalibleSectionInConferencePart2"/></h3>
        <tr>
            <th><fmt:message key="label.sectionIdColumnName"/></th>
            <th><fmt:message key="label.sectionNameColumnName"/></th>
            <th><fmt:message key="label.sectionManagerColumnName"/></th>
            <th><fmt:message key="label.updateSectionReferenceColumnName"/></th>
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
                        <a href="${pageContext.request.contextPath}/controller?command=show_update_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${section.id}&conferenceManagerId=${requestScope.conferenceManager}"><fmt:message key="label.update"/>
                            '${section.sectionName}'</a>
                    </td>
                </c:if>
            </tr>
            <br>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <p class="error_message"><fmt:message key="label.noSectionsInThisConference"/></p>
    </c:otherwise>
</c:choose>
<br>
<c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.conferenceManager}">
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=show_create_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message key="label.createSectionInConferencePart1"/> '${requestScope.conferenceTitle}' <fmt:message key="label.createSectionInConferencePart2"/></a>
</c:if>

<br>
<br>
<a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
</body>
</html>
