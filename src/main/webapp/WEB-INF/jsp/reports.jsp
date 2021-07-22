<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reports</title>
</head>
<body>
<h2>Report List:</h2>
<c:if test="${not empty requestScope.reports}">
    <h3>Sections of '${requestScope.sectionName}' section</h3>
    <tr>
        <th>ReportID</th>
        <th>SectionId</th>
        <th>ConferenceId</th>
        <th>ReportText</th>
        <th>ReportType</th>
        <th>Applicant</th>
    </tr>
    <c:forEach var="report " items="${requestScope.reports}">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_report?id=${report.id}">${report.id}</a>
            </td>
            <td>${report.sectionId}</td>
            <td>${report.conferenceId}</td>
            <td>${report.reportText}</td>
            <td>${report.reportType}</td>
            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_user?id=${report.applicant}">${report.applicant}</a>
            </td>
        </tr>
    </c:forEach>

    <!--
    <ul>
    <c:forEach var="report" items="${requestScope.reports}">
        <a href="${pageContext.request.contextPath}/controller?command=show_report?id=${report.id}">${report.id}</a>
        <li>${report.sectionId}</li>
        <li>${report.conferenceId}</li>
        <li>${report.reportText}</li>
        <li>${report.reportType}</li>
        <a href="${pageContext.request.contextPath}/controller?command=show_user?id=${report.applicant}">${report.applicant}</a>
    </c:forEach>
    </ul>
    -->
</c:if>
</body>
</html>
