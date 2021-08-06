<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.conferenceUpdate"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3><fmt:message key="label.error"/></h3>
    </c:when>
    <c:otherwise>
        <c:forEach var="conference" items="${requestScope.conferences}">
            <c:if test="${conference.id == requestScope.conferenceId}">
                <h3><fmt:message key="label.toUpdateConferenceEditTitleChooseManagerAndPressButtonPart1"/> '${conference.conferenceTitle}' <fmt:message key="label.toUpdateConferenceEditTitleChooseManagerAndPressButtonPart2"/> </h3>
            </c:if>
        </c:forEach>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_update_conference&conferenceId=${requestScope.conferenceId}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message key="label.try_again"/></a>
            </c:when>
            <c:otherwise>
                <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page"><fmt:message key="label.doNotTryAgain"/></a>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=update_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&conferenceId=${requestScope.conferenceId}"
              method="post">
            <label for="conferenceTitleField"><fmt:message key="label.conferenceTitle"/></label>
            <c:forEach var="conference" items="${requestScope.conferences}">
                <c:if test="${conference.id == requestScope.conferenceId}">
                    <input type="text" id="conferenceTitleField" name="conferenceTitle"
                           value="${conference.conferenceTitle}">
                </c:if>
            </c:forEach>

            <br>
            <label for="managerField"><fmt:message key="label.conferenceManager"/></label>
            <select name="managerConf" id="managerField">
                <c:forEach var="user" items="${requestScope.users}">
                    <c:choose>
                        <c:when test="${requestScope.managerConf == user.id}">
                            <option selected>${user.nickname}</option>
                        </c:when>
                        <c:otherwise>
                            <option>${user.nickname}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
            <br>

            <br>
            <input type="submit" value=<fmt:message key="label.submitConferenceUdateButton"/> class="button">
                <%--                            <c:choose>--%>
                <%--                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                <%--                                </c:when>--%>
                <%--                                <c:otherwise>--%>

                <%--                                </c:otherwise>--%>
                <%--                            </c:choose>--%>
        </form>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
</body>
</html>