<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Reports</title>
</head>
<body>
<h2>Report List:</h2>
<c:if test="${not empty requestScope.reports}">
    <h3>Reports of '${requestScope.sectionName}' section</h3>
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
</c:if>
</body>
</html>
