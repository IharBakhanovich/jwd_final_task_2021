<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sections</title>
</head>
<body>
<h2>Sections List:</h2>
<c:if test="${not empty requestScope.sections}">
    <h3>Sections of '${requestScope.conferenceTitle}' conference</h3>
    <ul>
        <c:forEach var="section" items="${requestScope.sections}">
            <a href="${pageContext.request.contextPath}/controller?command=show_reports?id=${section.id}?sectionName=${section.sectionName}?conferenceID=${requestScope.conferenceId}">${section.id}</a>
            <li>${section.conferenceId}</li>
            <li>${section.sectionName}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>
