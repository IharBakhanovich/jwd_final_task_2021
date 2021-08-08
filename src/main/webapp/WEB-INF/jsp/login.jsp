<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.login_page"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <h3><fmt:message key="label.toAuthenticateInSystemEnterYourLogin"/></h3>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_login"><fmt:message
                    key="label.try_again"/></a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
                <label for="loginField"><fmt:message key="label.loginFieldName"/> </label>
                <input type="text" id="loginField" name="login">
                <br>
                <label for="passwordField"><fmt:message key="label.passwordFieldName"/> </label>
                <input type="password" id="passwordField" name="password">
                <br>
                <input type="submit" value="<fmt:message key="label.LogInButton" />" class="button">
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
