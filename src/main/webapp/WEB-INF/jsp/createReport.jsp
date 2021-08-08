<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.newReportCreation"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <h3><fmt:message key="label.toCreateAReportEnterAReportTextChooseTypeOfReportAndPressButton"/></h3>
        </c:otherwise>
    </c:choose>

    <br>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_create_report&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}&sectionName=${requestScope.sectionName}"><fmt:message
                    key="label.try_again"/></a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=create_new_report&conferenceId=${requestScope.conferenceId}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&sectionId=${requestScope.sectionId}&questionReportId=0"
                  method="post">
                <label for="sectionNameField"><fmt:message key="label.sectionName"/></label>
                <input type="text" id="sectionNameField" name="sectionName" value="${requestScope.sectionName}"
                       readonly>

                <br>
                <label for="conferenceTitleField"><fmt:message key="label.conferenceTitle"/></label>
                <input type="text" id="conferenceTitleField" name="conferenceTitle"
                       value="${requestScope.conferenceTitle}"
                       readonly>

                <br>
                <label for="reportTextField"><fmt:message key="label.reportText"/></label>
                <textarea name="reportText" id="reportTextField" rows="1" cols="75"
                          wrap="soft"></textarea>

                <br>
                <label for="reportTypeField"><fmt:message key="label.reportType"/></label>
                <select name="reportType" id="reportTypeField">
                    <c:forEach var="reportType" items="${ReportType.valuesAsList()}">
                        <option>${reportType}</option>
                    </c:forEach>
                </select>
                <br>
                <label for="applicantNicknameField"><fmt:message key="label.applicant"/></label>
                <input type="text" id="applicantNicknameField" name="applicantNickname" value="${sessionScope.userName}"
                       readonly>

                <br>
                <input type="submit" value=
                    <fmt:message key="label.submitNewReportCreationButton"/> class="button">
                    <%--                            <c:choose>--%>
                    <%--                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                    <%--                                </c:when>--%>
                    <%--                                <c:otherwise>--%>

                    <%--                                </c:otherwise>--%>
                    <%--                            </c:choose>--%>
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
