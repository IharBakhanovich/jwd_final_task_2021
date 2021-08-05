<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<head>
    <title><fmt:message key="label.answerCreation"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <h3><fmt:message key="label.error"/></h3>
    </c:when>
    <c:otherwise>
        <h3><fmt:message key="label.toCreateAnAnswerToTheQuestionEnterAnAnswerPart1"/> ' ${requestScope.questionText} '
            <fmt:message key="label.toCreateAnAnswerToTheQuestionEnterAnAnswerPart2"/></h3>
    </c:otherwise>
</c:choose>

<br>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:choose>
            <c:when test="${(sessionScope.userRole eq requestScope.creatorRole) and (sessionScope.userId == requestScope.creatorId)}">
                <p class="error_message"><fmt:message key="label.${requestScope.error}" /></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_create_answer&&questionId=${requestScope.questionId}&managerId=${requestScope.creatorId}&managerRole=${requestScope.creatorRole}&conferenceId=${requestScope.conferenceId}&sectionId=${requestScope.sectionId}&questionText=${requestScope.questionText}"><fmt:message key="label.try_again"/></a>
            </c:when>
            <c:otherwise>
                <p class="error_message">${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=show_main_page"><fmt:message key="label.doNotTryAgain"/></a>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=create_new_report&conferenceTitle=${requestScope.conferenceTitle}&creatorId=${requestScope.creatorId}&creatorRole=${requestScope.creatorRole}&sectionName=${requestScope.sectionName}&questionReportId=${requestScope.questionId}"
              method="post">
            <label for="sectionIdField"><fmt:message key="label.sectionId"/> </label>
            <input type="text" id="sectionIdField" name="sectionId" value="${requestScope.sectionId}" readonly>

            <br>
            <label for="conferenceIdField"><fmt:message key="label.conferenceId"/> </label>
            <input type="text" id="conferenceIdField" name="conferenceId" value="${requestScope.conferenceId}"
                   readonly>

            <br>
            <label for="reportTextField"><fmt:message key="label.answerText"/> </label>
            <textarea name="reportText" id="reportTextField" rows="1" cols="75"
                      wrap="soft"></textarea>

            <br>
            <label for="reportTypeField"> <fmt:message key="label.reportType"/></label>
            <select name="reportType" id="reportTypeField">
                <c:forEach var="reportType" items="${ReportType.valuesAsList()}">
                    <option>${reportType}</option>
                </c:forEach>
            </select>
                <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
            <br>
            <label for="applicantNicknameField"> <fmt:message key="label.answerCreator"/></label>
            <input type="text" id="applicantNicknameField" name="applicantNickname" value="${sessionScope.userName}"
                   readonly>

            <br>
            <label for="questionReportIdField"><fmt:message key="label.questionId"/></label>
            <input type="text" id="questionReportIdField" name="questionReportId" value="${requestScope.questionId}"
                   readonly>

            <br>
            <input type="submit" value=<fmt:message key="label.submitAnswerCreationButton"/> class="button">
                <%--                                            <c:choose>--%>
                <%--                                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                <%--                                                </c:when>--%>
                <%--                                                <c:otherwise>--%>

                <%--                                                </c:otherwise>--%>
                <%--                                            </c:choose>--%>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
