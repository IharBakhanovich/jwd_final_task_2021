<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>New Report Creation</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3>Error</h3>
    </c:when>
    <c:otherwise>
        <h3>To create new report enter a report text, choose type of a new report and press
            'Submit new REport Creation' button</h3>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p class="error_message">${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_report&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}&sectionName=${requestScope.sectionName}">Try
            again</a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_report&conferenceId=${requestScope.conferenceId}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}"
            method="post">
            <label for="sectionNameField"> Section name:</label>
            <input type="text" id="sectionNameField" name="sectionName" value="${requestScope.sectionName}" readonly>

            <br>
            <label for="conferenceTitleField"> Conference title:</label>
            <input type="text" id="conferenceTitleField" name="conferenceTitle" value="${requestScope.conferenceTitle}"
                   readonly>

            <br>
            <label for="reportTextField"> Report text:</label>
            <textarea name="reportText" id="reportTextField" rows="1" cols="75"
                      wrap="soft"></textarea>

            <br>
            <label for="reportTypeField"> Report type: </label>
            <select name="reportType" id="reportTypeField">
                <c:forEach var="reportType" items="${ReportType.valuesAsList()}">
                    <option>${reportType}</option>
                </c:forEach>
            </select>
                <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
            <br>
            <label for="applicantIdField"> Applicant:</label>
            <input type="text" id="applicantIdField" name="conferenceTitle" value="${sessionScope.userName}"
                   readonly>

            <br>
            <input type="submit" value="Submit new Report Creation" class="button">
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
