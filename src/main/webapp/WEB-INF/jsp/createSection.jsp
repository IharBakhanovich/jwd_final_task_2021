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
    <title><fmt:message key="label.newSectionCreation"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3><fmt:message key="label.error"/></h3>
    </c:when>
    <c:otherwise>
        <h3><fmt:message key="label.toCreateNewSectionPart1"/> ' ${requestScope.conferenceTitle} ' <fmt:message key="label.toCreateNewSectionPart2"/></h3>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${(sessionScope.userRole eq Role.ADMIN) or (sessionScope.userId == requestScope.conferenceManagerId)}">
                <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_create_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message
                        key="label.try_again"/></a>
            </c:when>
            <c:otherwise>
                <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page"><fmt:message
                        key="label.doNotTryAgain"/></a>
            </c:otherwise>
        </c:choose>

    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_section&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&conferenceTitle=${requestScope.conferenceTitle}"
              method="post">
            <label for="conferenceIdField"><fmt:message key="label.conferenceId"/></label>
            <input type="text" id="conferenceIdField" name="conferenceId" value="${requestScope.conferenceId}" readonly>
            <br>

            <label for="sectionNameField"><fmt:message key="label.sectionName"/></label>
            <input type="text" id="sectionNameField" name="sectionName">

            <br>
            <label for="managerField"><fmt:message key="label.sectionManager"/></label>
            <select name="managerSect" id="managerField">
                <c:forEach var="user" items="${requestScope.users}">
                    <c:choose>
                        <c:when test="${requestScope.conferenceManagerId==user.id}">
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
            <input type="submit" value=<fmt:message key="label.submitSectionCreationButton"/> class="button">
                <%--                            <c:choose>--%>
                <%--                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                <%--                                </c:when>--%>
                <%--                                <c:otherwise>--%>

                <%--                                </c:otherwise>--%>
                <%--                            </c:choose>--%>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
