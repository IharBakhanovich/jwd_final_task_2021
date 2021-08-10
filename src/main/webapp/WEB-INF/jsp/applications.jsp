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
    <title><fmt:message key="label.applications"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:choose>
        <c:when test="${requestScope.sectionName eq 'applicantApplications'}">
            <h2><fmt:message key="label.yourApplications"/></h2>
        </c:when>
        <c:otherwise>
            <h2><fmt:message key="label.applicationsFromApplicants"/></h2>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${not empty requestScope.applications}">
            <table border="2" cellpadding="5">
                <caption><h3><fmt:message key="label.thereAreSomeApplicationsFromApplicants"/></h3></caption>
                <tr>
                    <th width="30"><fmt:message key="label.ReportIdColumnName"/></th>
                    <th><fmt:message key="label.SectionColumnName"/></th>
                    <th><fmt:message key="label.ConferenceColumnName"/></th>
                    <th><fmt:message key="label.ReportTextColumnName"/></th>
                    <th><fmt:message key="label.ReportTypeColumnName"/></th>
                    <th><fmt:message key="label.ApplicantColumnName"/></th>
                    <th><fmt:message key="label.QuestionIdColumnName"/></th>
                        <%--                    <th><fmt:message key="label.ShowQuestionLinkColumnName"/></th>--%>
                    <th><fmt:message key="label.ApproveCancelRejectApplicationLinkColumnName"/></th>
                </tr>
                <c:forEach var="application" items="${requestScope.applications}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${application.id}">${application.id}</a>
                        </td>
                        <c:forEach var="section" items="${requestScope.sections}">
                            <c:if test="${application.sectionId==section.id}">
                                <td>${section.sectionName}</td>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="conference" items="${requestScope.conferences}">
                            <c:if test="${application.conferenceId==conference.id}">
                                <td>${conference.conferenceTitle}</td>
                            </c:if>
                        </c:forEach>
                        <td>${application.reportText}</td>
                        <td>${application.reportType}</td>
                        <c:forEach var="user" items="${requestScope.users}">
                            <c:if test="${application.applicant==user.id}">
                                <td>
                                    <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                                </td>
                            </c:if>
                        </c:forEach>
                        <td>
                                ${application.questionReportId}
                        </td>

                            <%--                        <td>--%>
                            <%--                            <a href="${pageContext.request.contextPath}/controller?command=show_question_context&questionIdForContext=${application.id}&managerId=${requestScope.managerId}&questionReportIdForContext=${application.questionReportId}&sectionName=${requestScope.sectionName}"><fmt:message--%>
                            <%--                                    key="label.showQuestionHistory"/></a>--%>
                            <%--                        </td>--%>
                        <td>
                            <c:choose>
                                <c:when test="${requestScope.sectionName eq 'applicantApplications'}">
                                    <div class="link">
                                        <a href="${pageContext.request.contextPath}/controller?command=show_process_application&applicationId=${application.id}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${application.conferenceId}&sectionId=${application.sectionId}&applicationText=${application.reportText}&applicationToken=applicantApplication"><fmt:message
                                                key="label.processThisApplication"/></a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="link">
                                        <a href="${pageContext.request.contextPath}/controller?command=show_process_application&applicationId=${application.id}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${application.conferenceId}&sectionId=${application.sectionId}&applicationText=${application.reportText}&applicationToken=userApplication"><fmt:message
                                                key="label.processThisApplication"/></a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <p class="error_message"><fmt:message key="label.thereAreNoApplicationsInYourSectionsMyLord"/></p>
        </c:otherwise>
    </c:choose>

    <%--    <br>--%>
    <%--    <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>--%>
</div>
</body>
</html>

