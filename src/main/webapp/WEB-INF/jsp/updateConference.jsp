<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Conference Update</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3>Error</h3>
    </c:when>
    <c:otherwise>
        <h3>To update new conference edit a title of conference, choose an appointed conference manager and press
            'Submit Conference Update' button</h3>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_update_conference">Try again</a>
            </c:when>
            <c:otherwise>
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page">Do Not Try again :)))</a>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=update_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&conferenceId=${requestScope.conferenceId}"
              method="post">
            <label for="conferenceTitleField"> Conference title:</label>
            <c:forEach var="conference" items="${requestScope.conferences}">
                <c:if test="${conference.id == requestScope.conferenceId}">
                    <input type="text" id="conferenceTitleField" name="conferenceTitle"
                           value="${conference.conferenceTitle}">
                </c:if>
            </c:forEach>

            <br>
            <label for="managerField"> Conference manager: </label>
            <select name="managerConf" id="managerField">
                <c:forEach var="user" items="${requestScope.users}">
                    <c:choose>
                        <c:when test="${requestScope.managerConference == user.id}">
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
            <input type="submit" value="Submit Conference Update" class="button">
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