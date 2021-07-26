<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>User page</title>
</head>
<body>
<c:if test="${not empty requestScope.user}">

    <h3>User</h3>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p>${requestScope.error}</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${requestScope.user.get().id}">Try again</a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=update_user&updaterId=${sessionScope.userId}&updaterRole=${sessionScope.userRole}" method="post">
                <label for="idField"> Id:</label>
                <input type="text" id="idField" name="id" value="${requestScope.user.get().id}" readonly>
                <!-- name запихнет в пост запрос значения -->
                <br>
                <label for="emailField"> Email:</label>
                <c:choose>
                    <c:when test="${requestScope.user.get().firstName == 'default@email.com'} ">
                        <input type="text" id="emailField" name="email">
                    </c:when>
                    <c:otherwise>
                        <input type="text" id="emailField" name="email" value="${requestScope.user.get().email}">
                    </c:otherwise>
                </c:choose>
                <!-- name запихнет в пост запрос значения -->
                <br>
                <label for="nicknameField"> Nickname: </label>
                <input type="text" id="nicknameField" name="nickname" value="${requestScope.user.get().nickname}" readonly>
                <br>
                <label for="firstNameField"> FirstName: </label>
                <input type="text" id="firstNameField" name="firstName" value="${requestScope.user.get().firstName}">
                <br>
                <label for="surNameField"> SurName: </label>
                <input type="text" id="surNameField" name="surname" value="${requestScope.user.get().surname}">
                <br>
                <label for="roleField"> Role: </label>
                <c:choose>
                    <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                        <select name="role" id="roleField">
                            <c:forEach var="role" items="${Role.valuesAsList()}">
                                <c:choose>
                                    <c:when test="${requestScope.user.get().role eq role}">
                                        <option selected>${role}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option>${role}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
                    </c:when>
                    <c:otherwise>
                        <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}" readonly>
                    </c:otherwise>
                </c:choose>
                <br>
                <c:if test="${sessionScope.userRole eq Role.ADMIN or requestScope.user.get().id == sessionScope.userId}">
                    <input type="submit" value="Update user details" class="button">
                </c:if>
            </form>
        </c:otherwise>
    </c:choose>


</c:if>
<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the future</a>
</body>
</html>
