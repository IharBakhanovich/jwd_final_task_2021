<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.reports"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <h2><fmt:message key="label.reports"/></h2>
    <c:choose>
        <c:when test="${requestScope.conferenceTitle eq 'question'}">
            <c:choose>
                <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">
                    <c:choose>
                        <c:when test="${requestScope.sectionName eq 'applicantQuestions'}">
                            <p><fmt:message key="label.clickBelowToBackToQuestions"/></p>
                            <div class="link">
                                <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantQuestions"><fmt:message
                                        key="label.BackToQuestions"/></a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p><fmt:message key="label.clickBelowToBackToQuestions"/></p>
                            <div class="link">
                                <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}"><fmt:message
                                        key="label.BackToQuestions"/></a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p><fmt:message key="label.clickBelowToBackToQuestions"/></p>
                    <div class="link">
                        <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantQuestions"><fmt:message
                                key="label.BackToQuestions"/></a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty sessionScope.userName}">
                <div class="link">
                    <a href="${pageContext.request.contextPath}/controller?command=show_create_report&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}&sectionName=${requestScope.sectionName}"><fmt:message
                            key="label.createNewReportInSectionPart1"/>
                        '${requestScope.conferenceTitle}'/'${requestScope.sectionName}' <fmt:message
                                key="label.createNewReportInSectionPart2"/></a>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty requestScope.reports}">
            <table border="2" cellpadding="5">
                <c:choose>
                    <c:when test="${requestScope.conferenceTitle eq 'question'}">
                        <caption><h3><fmt:message key="label.questionHistory"/></h3></caption>
                    </c:when>
                    <c:otherwise>
                        <caption><h3><fmt:message key="label.reportsOfSectionPart1"/> '${requestScope.sectionName}'
                            <fmt:message key="label.reportsOfSectionPart2"/></h3></caption>
                    </c:otherwise>
                </c:choose>

                <tr>
                    <th width="30"><fmt:message key="label.ReportIdColumnName"/></th>
                    <th><fmt:message key="label.sectionNameColumnName"/></th>
                    <th><fmt:message key="label.conferenceTitleColumnName"/></th>
                    <th><fmt:message key="label.reportTextColumnName"/></th>
                    <th><fmt:message key="label.ReportTypeColumnName"/></th>
                    <th><fmt:message key="label.ApplicantColumnName"/></th>
                </tr>
                <c:forEach var="report" items="${requestScope.reports}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${report.id}&managerRole=${sessionScope.userRole}&managerId=${sessionScope.userId}">${report.id}</a>
                        </td>
                        <c:forEach var="section" items="${requestScope.sections}">
                                <ctg:choose-another-element-ForTables
                                        valueWithWhichCompare="${section.id}" valueToCompare="${report.sectionId}"
                                        valueToShow="${section.sectionName}"/>
                        </c:forEach>
                        <c:forEach var="conference" items="${requestScope.conferences}">
                            <ctg:choose-another-element-ForTables
                                    valueWithWhichCompare="${conference.id}" valueToCompare="${report.conferenceId}"
                                    valueToShow="${conference.conferenceTitle}"/>
                        </c:forEach>
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
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <p class="error_message"><fmt:message key="label.thereAreNoReportsInThisSection"/></p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
