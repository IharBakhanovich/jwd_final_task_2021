<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>New Conference Creation</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3>Error</h3>
    </c:when>
    <c:otherwise>
        <h3>To create new conference enter a title of conference, choose an appointed conference manager and press
            'Submit new Conference Creation' button</h3>
    </c:otherwise>
</c:choose>

<br>
<!--как switch в java. но если необходимо if else - делаем when/otherwise это как switch с одним case-->
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_create_conference">Try again</a>
            </c:when>
            <c:otherwise>
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page">Do Not Try again :)))</a>
            </c:otherwise>
        </c:choose>

    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"
              method="post">
            <label for="conferenceTitleField"> Conference title:</label>
            <input type="text" id="conferenceTitleField" name="conferenceTitle">
            <!-- name запихнет в пост запрос значения -->

            <br>
            <label for="managerField"> Conference manager: </label>
            <select name="managerConf" id="managerField">
                <c:forEach var="user" items="${requestScope.users}">
                    <c:choose>
                        <c:when test="${requestScope.creatorId==user.id}">
                            <option selected>${user.nickname}</option>
                        </c:when>
                        <c:otherwise>
                            <option>${user.nickname}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
                <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
            <br>

            <br>
            <input type="submit" value="Submit new Conference Creation" class="button">
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
