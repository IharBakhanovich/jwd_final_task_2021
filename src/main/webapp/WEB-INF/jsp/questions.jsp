<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.questions"/></title>
</head>
<body>
<c:choose>
    <c:when test="${requestScope.sectionName eq 'applicantQuestions'}">
        <h2><fmt:message key="label.yourQuestions"/></h2>
    </c:when>
    <c:otherwise>
        <h2><fmt:message key="label.questionsFromApplicants"/></h2>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${not empty requestScope.questions}">
        <table border="2" cellpadding="5">
            <caption><h3><fmt:message key="label.thereAreSomeQuestionsFromApplicants"/></h3></caption>
            <tr>
                <th width="30"><fmt:message key="label.ReportIdColumnName"/></th>
                <th><fmt:message key="label.SectionColumnName"/></th>
                <th><fmt:message key="label.ConferenceColumnName"/></th>
                <th><fmt:message key="label.ReportTextColumnName"/></th>
                <th><fmt:message key="label.ReportTypeColumnName"/></th>
                <th><fmt:message key="label.ApplicantColumnName"/></th>
                <th><fmt:message key="label.QuestionIdColumnName"/></th>
                <th><fmt:message key="label.ShowQuestionLinkColumnName"/></th>
                <th><fmt:message key="label.WriteAnswerLinkColumnName"/></th>
            </tr>
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
                    <td>
                            ${question.questionReportId}
                    </td>

                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=show_question_context&questionIdForContext=${question.id}&managerId=${requestScope.managerId}&questionReportIdForContext=${question.questionReportId}&sectionName=${requestScope.sectionName}"><fmt:message
                                key="label.showQuestionHistory"/></a>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${question.questionReportId == 0}">
                                <a href="${pageContext.request.contextPath}/controller?command=show_create_answer&questionId=${question.id}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${question.conferenceId}&sectionId=${question.sectionId}&questionText=${question.reportText}"><fmt:message
                                        key="label.writeAnAnswer"/></a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/controller?command=show_create_answer&questionId=${question.questionReportId}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${question.conferenceId}&sectionId=${question.sectionId}&questionText=${question.reportText}"><fmt:message
                                        key="label.writeAnAnswer"/></a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p class="error_message"><fmt:message key="label.thereAreNoQuestionsForYouMyLord"/></p>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
</body>
</html>
