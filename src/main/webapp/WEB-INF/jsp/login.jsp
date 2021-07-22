<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<!--как switch в java. но если необходимо if else - делаем when/otherwise это как switch с одним case-->
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=show_login">Try again</a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
            <label for="loginField"> Login:</label>
            <input type="text" id="loginField" name="login"> <!-- name запихнет в пост запрос значения -->
            <br>
            <label for="passwordField"> Password: </label>
            <input type="password" id="passwordField" name="password">
            <br>
            <input type="submit" value="Log In">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
