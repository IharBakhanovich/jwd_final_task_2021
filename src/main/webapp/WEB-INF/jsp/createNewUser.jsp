<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>NewUserCreation</title>
</head>
<body>
<h3>To create new user enter nickname and password two times and press 'SubmitNewUserCreation' button</h3>
<br>
<!--как switch в java. но если необходимо if else - делаем when/otherwise это как switch с одним case-->
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user">Try again</a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_user" method="post">
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
            <input type="submit" value="SubmitNewUserCreation" class="button">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
