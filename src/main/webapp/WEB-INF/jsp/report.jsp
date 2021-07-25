<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Report</title>
</head>
<body>
<c:if test="${not empty requestScope.report}">
    <h3>Report</h3>
    <tr>
        <th>Id</th>
        <th>Section</th>
        <th>Conference</th>
        <th>ReportText</th>
        <th>ReportType</th>
        <th>Applicant</th>
    </tr>
    <br>
    <tr>
        <td>${requestScope.report.get().id}</td>
        <c:forEach var="section" items="${requestScope.sections}">
            <c:if test="${report.get().sectionId==section.id}">
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_reports&id=${section.id}&conferenceId=${report.get().conferenceId}&&sectionName=${section.sectionName}">${section.sectionName}</a>
                </td>
            </c:if>
        </c:forEach>
        <c:forEach var="conference" items="${requestScope.conferences}">
            <c:if test="${report.get().conferenceId==conference.id}">
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}">${conference.conferenceTitle}</a>
                </td>
            </c:if>
        </c:forEach>
        <td>${requestScope.report.get().reportText}</td>
        <td>${requestScope.report.get().reportType}</td>
        <c:forEach var="user" items="${requestScope.users}">
            <c:if test="${report.get().applicant==user.id}">
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                </td>
            </c:if>
        </c:forEach>
    </tr>
</c:if>
<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the future</a>

</body>
</html>
