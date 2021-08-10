<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<head>
    <title><fmt:message key="label.processApplication"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <h3><fmt:message key="label.toProcessAnApplicationChooseTheReportTypePart1"/>
                ' ${requestScope.applicationText} '
                <fmt:message key="label.toProcessAnApplicationChooseTheReportTypePart2"/></h3>
        </c:otherwise>
    </c:choose>

    <br>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <c:choose>
                <c:when test="${(sessionScope.userRole eq requestScope.managerRole) and (sessionScope.userId == requestScope.managerId)}">
                    <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                    <c:choose>
                        <c:when test="${requestScope.applicationToken eq 'applicantApplication'}">
                            <a href="${pageContext.request.contextPath}/controller?command=show_process_application&applicationId=${requestScope.applicationId}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${requestScope.conferenceId}&sectionId=${requestScope.sectionId}&applicationText=${requestScope.applicationText}&applicationToken=applicantApplication"><fmt:message
                                    key="label.try_again"/></a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=show_process_application&applicationId=${requestScope.applicationId}&managerId=${requestScope.managerId}&managerRole=${sessionScope.userRole}&conferenceId=${requestScope.conferenceId}&sectionId=${requestScope.sectionId}&applicationText=${requestScope.applicationText}&applicationToken=userApplication"><fmt:message
                                    key="label.try_again"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                    <a href="${pageContext.request.contextPath}/controller?command=show_main_page"><fmt:message
                            key="label.doNotTryAgain"/></a>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=update_report&updaterId=${sessionScope.userId}&updaterRole=${sessionScope.userRole}"
                  method="post">
                <label for="applicationIdField"><fmt:message key="label.applicationId"/> </label>
                <input type="text" id="applicationIdField" name="id" value="${requestScope.applicationId}" readonly>

                <br>
                <label for="sectionNameField"><fmt:message key="label.sectionName"/> </label>
                <input type="text" id="sectionNameField" name="sectionName" value="${requestScope.sectionName}" readonly>

                <br>
                <label for="conferenceNameField"><fmt:message key="label.ConferenceColumnName"/> </label>
                <input type="text" id="conferenceNameField" name="conferenceTitle" value="${requestScope.conferenceTitle}"
                       readonly>

                <br>
                <label for="applicationTextField"><fmt:message key="label.applicationText"/> </label>
                <textarea name="reportText" id="applicationTextField" rows="1" cols="75"
                          wrap="soft">${requestScope.applicationText}</textarea>

                <br>
                <label for="applicationTypeField"> <fmt:message key="label.reportType"/></label>
                <select name="reportType" id="applicationTypeField">
                    <c:forEach var="reportType" items="${requestScope.allowedReportTypes}">
                        <option>${reportType}</option>
                    </c:forEach>
                </select>
                    <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
                <br>
                <label for="applicantNicknameField"> <fmt:message key="label.applicant"/></label>
                <input type="text" id="applicantNicknameField" name="applicant" value="${requestScope.applicant}"
                       readonly>

                <br>
                <label for="questionReportIdField"><fmt:message key="label.questionId"/></label>
                <input type="text" id="questionReportIdField" name="questionReportId" value="${requestScope.questionId}"
                       readonly>

                <br>
                <input type="submit" value=
                    <fmt:message key="label.processApplicationButton"/> class="button">
                    <%--                                            <c:choose>--%>
                    <%--                                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                    <%--                                                </c:when>--%>
                    <%--                                                <c:otherwise>--%>

                    <%--                                                </c:otherwise>--%>
                    <%--                                            </c:choose>--%>
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

