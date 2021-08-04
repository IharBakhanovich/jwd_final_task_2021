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
<c:choose>
    <c:when test="${requestScope.sectionName eq 'applicantQuestions'}">
        <h2>Your questions:</h2>
    </c:when>
    <c:otherwise>
        <h2>Questions from applicants:</h2>
    </c:otherwise>
</c:choose>

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
            <th>QuestionID</th>
            <th>ShowQuestionContextLink</th>
            <th>WriteAnswerLink</th>

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
            <td>
                ${question.questionReportId}
            </td>

            <td>
                <a href="${pageContext.request.contextPath}/controller?command=show_question_context&questionIdForContext=${question.id}&managerId=${requestScope.managerId}&questionReportIdForContext=${question.questionReportId}&sectionName=${requestScope.sectionName}">Show question's history</a>
            </td>
            <td>
            <c:choose>
                <c:when test="${question.questionReportId == 0}">
                    <a href="${pageContext.request.contextPath}/controller?command=show_create_answer&questionId=${question.id}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${question.conferenceId}&sectionId=${question.sectionId}&questionText=${question.reportText}">Write an answer</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/controller?command=show_create_answer&questionId=${question.questionReportId}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${question.conferenceId}&sectionId=${question.sectionId}&questionText=${question.reportText}">Write an answer</a>
                </c:otherwise>
            </c:choose>
        </td>
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
