<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<html>
<head>
</head>
<body>
<div class="navigation" id="navigation">
    <h2><fmt:message key="label.Menu"/></h2>
    <c:choose>
        <c:when test="${empty sessionScope.userName}">

            <a href="${pageContext.request.contextPath}/controller?command=show_login"><fmt:message
                    key="label.loginReference"/></a>

            <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message
                    key="label.registrationReference"/></a>
            <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
        </c:when>
        <c:otherwise>
            <c:if test="${sessionScope.userRole eq Role.ADMIN}">
                <%--                <p><fmt:message key="label.clickToSeeAllUsers"/></p>--%>
                <a href="${pageContext.request.contextPath}/controller?command=show_users"><fmt:message
                        key="label.userPageReference"/></a>

                <%--                <p><fmt:message key="label.clickToAddNewConference"/></p>--%>
                <a href="${pageContext.request.contextPath}/controller?command=show_create_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message
                        key="label.createConferenceReference"/></a>
            </c:if>

            <c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">
                <%--                <p><fmt:message key="label.clickToSeeQuestionsFromUsers"/></p>--%>
                <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}"><fmt:message
                        key="label.ShowQuestionsFromUsersReference"/></a>
                <a href="${pageContext.request.contextPath}/controller?command=show_applications&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}"><fmt:message
                        key="label.ShowApplicationsFromUsersReference"/></a>
            </c:if>

            <c:if test="${not empty sessionScope.userName}">
                <%--                <p><fmt:message key="label.clickToSeeYourQuestions"/></p>--%>
                <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantQuestions"><fmt:message
                        key="label.showYourQuestionsReference"/></a>
                <a href="${pageContext.request.contextPath}/controller?command=show_own_applications&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantApplications"><fmt:message
                        key="label.showYourApplicationReference"/></a>

            </c:if>

            <%--            <p><fmt:message key="label.clickToLogOut"/></p>--%>
            <a href="${pageContext.request.contextPath}/controller?command=logout"><fmt:message
                    key="label.logoutReference"/></a>
            <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
