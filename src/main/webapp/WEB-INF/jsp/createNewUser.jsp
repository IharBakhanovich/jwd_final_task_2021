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
    <title><fmt:message key="label.newUserCreation"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3><fmt:message key="label.error"/></h3>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                <h3><fmt:message key="label.toCreateNewUserEnterNicknameAndPasswordTwoTimesAndPressButton"/></h3>
            </c:when>
            <c:otherwise>
                <h3><fmt:message key="label.toCreateAnAccountEnterNicknameAndPasswordTwoTimesAndPressButton"/></h3>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p class="error_message"><fmt:message key="label.${requestScope.error}" /></p>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message key="label.try_again"/></a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_user&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}" method="post">
            <label for="nicknameField"><fmt:message key="label.nicknameFieldName"/> </label>
            <input type="text" id="nicknameField" name="nickname"> <!-- name запихнет в пост запрос значения -->
            <br>
            <label for="newPasswordField"><fmt:message key="label.passwordFieldName"/></label>
            <input type="password" id="newPasswordField" name="password">
            <br>
            <label for="newPasswordRepeatField"><fmt:message key="label.passwordRepeatFieldName"/></label>
            <input type="password" id="newPasswordRepeatField" name="passwordRepeat">
            <br>
            <br>
            <c:choose>
                <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                    <input type="submit" value=<fmt:message key="label.submitNewUserCreationButton"/> class="button">
                </c:when>
                <c:otherwise>
                    <input type="submit" value=<fmt:message key="label.registerButton"/> class="button">
                </c:otherwise>
            </c:choose>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
