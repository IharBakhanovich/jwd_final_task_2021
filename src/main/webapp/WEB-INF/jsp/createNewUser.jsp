<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>NewUserCreation</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3>Error</h3>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                <h3>To create new user enter nickname and password two times and press 'SubmitNewUserCreation' button</h3>
            </c:when>
            <c:otherwise>
                <h3>To create an account enter your nickname and password two times and press 'Register' button</h3>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<br>
<!--как switch в java. но если необходимо if else - делаем when/otherwise это как switch с одним case-->
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p class="error_message">${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user">Try again</a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_user&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}" method="post">
            <label for="nicknameField"> NickName:</label>
            <input type="text" id="nicknameField" name="nickname"> <!-- name запихнет в пост запрос значения -->
            <br>
            <label for="newPasswordField"> Password: </label>
            <input type="password" id="newPasswordField" name="password">
            <br>
            <label for="newPasswordRepeatField"> Password: </label>
            <input type="password" id="newPasswordRepeatField" name="passwordRepeat">
            <br>
            <br>
            <c:choose>
                <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                    <input type="submit" value="SubmitNewUserCreation" class="button">
                </c:when>
                <c:otherwise>
                    <input type="submit" value="Register" class="button">
                </c:otherwise>
            </c:choose>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
