<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Reports</title>
</head>
<body>
<h2>Reports:</h2>
<c:choose>
    <c:when test="${not empty requestScope.reports}">
        <c:choose>
            <c:when test="${requestScope.conferenceTitle eq 'question'}">
                <h3>History of the question</h3>
            </c:when>
            <c:otherwise>
                <h3>Reports of '${requestScope.sectionName}' section</h3>
            </c:otherwise>
        </c:choose>
        <tr>
            <th>ReportID</th>
            <th>SectionId</th>
            <th>ConferenceId</th>
            <th>ReportText</th>
            <th>ReportType</th>
            <th>Applicant</th>
        </tr>
        <br>
        <c:forEach var="report" items="${requestScope.reports}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${report.id}">${report.id}</a>
                </td>
                <td>${report.sectionId}</td>
                <td>${report.conferenceId}</td>
                <td>${report.reportText}</td>
                <td>${report.reportType}</td>
                <c:forEach var="user" items="${requestScope.users}">
                    <c:if test="${report.applicant==user.id}">
                        <td>
                            <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                        </td>
                    </c:if>
                </c:forEach>
            </tr>
            <br>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <p class="error_message">There are no reports in this section</p>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${requestScope.conferenceTitle eq 'question'}">
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">
                <c:choose>
                    <c:when test="${requestScope.sectionName eq 'applicantQuestions'}">
                        <p>Great. Click below if you want back to questions</p>
                        <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Back
                            to questions</a>
                    </c:when>
                    <c:otherwise>
                        <p>Great. Click below if you want back to questions</p>
                        <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Back
                            to questions</a>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <p>Great. Click below if you want back to questions</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Back
                    to questions</a>
            </c:otherwise>
        </c:choose>
<%--        <c:if test="${}">--%>
<%--            <p>Great. Click below if you want back to questions</p>--%>
<%--            <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Back--%>
<%--                to questions</a>--%>
<%--        </c:if>--%>
    </c:when>
    <c:otherwise>
        <c:if test="${not empty sessionScope.userName}">
            <a href="${pageContext.request.contextPath}/controller?command=show_create_report&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}&sectionName=${requestScope.sectionName}">Create
                New Report in '${requestScope.conferenceTitle}'/'${requestScope.sectionName}' section</a>
        </c:if>
    </c:otherwise>
</c:choose>

<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the main page</a>
</body>
</html>
