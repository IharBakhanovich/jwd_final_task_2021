<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Questions</title>
</head>
<body>
<h2>Questions from applicants:</h2>
<c:choose>
    <c:when test="${not empty requestScope.questions}">
        <h3>There are some questions from applicants</h3>
        <tr>
            <th>ReportID</th>
            <th>Section</th>
            <th>Conference</th>
            <th>ReportText</th>
            <th>ReportType</th>
            <th>Applicant</th>
        </tr>
        <br>
        <c:forEach var="question" items="${requestScope.questions}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${question.id}">${question.id}</a>
                </td>
                <c:forEach var="section" items="${requestScope.sections}">
                    <c:if test="${question.sectionId==section.id}">
                        <td>${section.sectionName}</td>
                    </c:if>
                </c:forEach>
                <c:forEach var="conference" items="${requestScope.conferences}">
                    <c:if test="${question.conferenceId==conference.id}">
                        <td>${conference.conferenceTitle}</td>
                    </c:if>
                </c:forEach>
                <td>${question.reportText}</td>
                <td>${question.reportType}</td>
                <c:forEach var="user" items="${requestScope.users}">
                    <c:if test="${question.applicant==user.id}">
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
        <p class="error_message">There are no questions for you, my lord</p>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/controller">Back to the main page</a>
</body>
</html>
