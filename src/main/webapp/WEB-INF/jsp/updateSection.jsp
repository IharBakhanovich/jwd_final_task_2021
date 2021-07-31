<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>SectionUpdate</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3>Error</h3>
    </c:when>
    <c:otherwise>
        <h3>To update '${requestScope.sectionName}' section in '${requestScope.conferenceTitle}' conference edit a title of the section, choose
            an appointed section manager and press 'Submit Section Update' button</h3>
    </c:otherwise>
</c:choose>
<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${(sessionScope.userRole eq Role.ADMIN) or (sessionScope.userId == requestScope.conferenceManagerId) or (sessionScope.userId == requestScope.sectionManager)}">
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_update_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${requestScope.sectionId}&conferenceManagerId=${requestScope.conferenceManagerId}">Try
                    again</a>
            </c:when>
            <c:otherwise>
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page">Do Not Try again :)))</a>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=update_section&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${requestScope.sectionId}&conferenceManagerId=${requestScope.conferenceManagerId}"
              method="post">
            <label for="conferenceIdField"> ConferenceId:</label>
            <input type="text" id="conferenceIdField" name="conferenceId" value="${requestScope.conferenceId}" readonly>
            <br>

            <label for="sectionNameField"> Section Name:</label>
            <input type="text" id="sectionNameField" name="sectionName" value="${requestScope.sectionName}">

            <br>
            <label for="managerField"> Section manager: </label>
            <c:choose>
                <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.conferenceManagerId}">
                    <select name="managerSectNickname" id="managerField">
                        <c:forEach var="user" items="${requestScope.users}">
                            <c:choose>
                                <c:when test="${requestScope.sectionManagerId==user.id}">
                                    <option selected>${user.nickname}</option>
                                </c:when>
                                <c:otherwise>
                                    <option>${user.nickname}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${requestScope.users}">
                        <c:if test="${requestScope.sectionManagerId==user.id}">
                            <input type="text" id="managerField" name="managerSectNickname" value="${user.nickname}" readonly>
                        </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

            <br>
            <br>
            <input type="submit" value="Submit Section Update" class="button">
<%--                                            <c:choose>--%>
<%--                                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

<%--                                                </c:when>--%>
<%--                                                <c:otherwise>--%>

<%--                                                </c:otherwise>--%>
<%--                                            </c:choose>--%>
        </form>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/controller">Back to the main page</a>

</body>
</html>
