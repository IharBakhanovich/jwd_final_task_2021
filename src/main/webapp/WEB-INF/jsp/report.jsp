<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.report"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:if test="${not empty requestScope.report}">

        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <h3><fmt:message key="label.error"/></h3>
            </c:when>
            <c:otherwise>
                <h3><fmt:message key="label.report"/></h3>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${requestScope.report.get().id}"><fmt:message
                        key="label.try_again"/></a>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/controller?command=update_report&updaterId=${sessionScope.userId}&updaterRole=${sessionScope.userRole}&applicationToken=report"
                      method="post">
                    <br>
                    <label for="idField"><fmt:message key="label.id"/></label>
                    <input type="text" id="idField" name="id" value="${requestScope.report.get().id}" readonly>
                    <br>
                    <label for="sectionField"><fmt:message key="label.section"/></label>
                    <c:choose>
                        <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                            <select name="sectionName" id="sectionField">
                                <c:forEach var="section" items="${requestScope.sections}">
                                    <c:choose>
                                        <c:when test="${requestScope.report.get().sectionId==section.id}">
                                            <option selected>${section.sectionName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option>${section.sectionName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="section" items="${requestScope.sections}">
                                <c:if test="${requestScope.report.get().sectionId==section.id}">
                                    <c:choose>
                                        <c:when test="${requestScope.report.get().applicant==sessionScope.userId or sessionScope.userId == requestScope.idOfManagerOfReportsSection}">
                                            <input type="text" id="sectionField" name="sectionName"
                                                   value="${section.sectionName}" readonly>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/controller?command=show_reports&id=${section.id}&conferenceId=${report.get().conferenceId}&&sectionName=${section.sectionName}"
                                               id="sectionField" name="sectionName">${section.sectionName}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <br>
                    <label for="conferenceField"><fmt:message key="label.conference"/></label>
                    <c:choose>
                        <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                            <select name="conferenceTitle" id="conferenceField">
                                <c:forEach var="conference" items="${requestScope.conferences}">
                                    <c:choose>
                                        <c:when test="${requestScope.report.get().conferenceId==conference.id}">
                                            <option selected>${conference.conferenceTitle}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option>${conference.conferenceTitle}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="conference" items="${requestScope.conferences}">
                                <c:if test="${requestScope.report.get().conferenceId==conference.id}">
                                    <c:choose>
                                        <c:when test="${requestScope.report.get().applicant==sessionScope.userId or sessionScope.userId == requestScope.idOfManagerOfReportsSection}">
                                            <input type="text" id="conferenceField" name="conferenceTitle"
                                                   value="${conference.conferenceTitle}" readonly>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}"
                                               id="conferenceField"
                                               name="conferenceTitle">${conference.conferenceTitle}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <br>
                    <label for="reportTextField"><fmt:message key="label.reportText"/></label>
                    <textarea name="reportText" id="reportTextField" rows="1" cols="75"
                              wrap="soft">${requestScope.report.get().reportText}</textarea>
                    <br>
                    <label for="reportTypeField"><fmt:message key="label.reportType"/></label>
                    <c:choose>
                        <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.report.get().applicant or sessionScope.userId == requestScope.idOfManagerOfReportsSection}">
                            <select name="reportType" id="reportTypeField">
                                <c:forEach var="reportType" items="${requestScope.allowedReportTypes}">
                                    <c:choose>
                                        <c:when test="${requestScope.report.get().reportType eq reportType}">
                                            <option selected>${reportType}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option>${reportType}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <input type="text" id="reportTypeField" name="reportType"
                                   value="${requestScope.report.get().reportType}" readonly>
                        </c:otherwise>
                    </c:choose>

                    <br>
                    <label for="applicantField"><fmt:message key="label.applicant"/></label>
                    <c:forEach var="user" items="${requestScope.users}">
                        <c:if test="${report.get().applicant==user.id}">
                            <c:choose>
                                <c:when test="${requestScope.report.get().applicant==sessionScope.userId or sessionScope.userId == requestScope.idOfManagerOfReportsSection or sessionScope.userRole == Role.ADMIN}">
                                    <input type="text" id="applicantField" name="applicant"
                                           value="${user.nickname}" readonly>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}"
                                       id="applicantField" name="applicant">${user.nickname}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>

                    <br>
                    <label for="questionIdField"><fmt:message key="label.questionId"/></label>
                    <input type="text" id="questionIdField" name="questionReportId"
                           value="${requestScope.report.get().questionReportId}" readonly>

                    <br>
                    <br>
                    <c:if test="${sessionScope.userRole eq Role.ADMIN or requestScope.report.get().applicant == sessionScope.userId or sessionScope.userId == requestScope.idOfManagerOfReportsSection}">
                        <input type="submit" value=
                            <fmt:message key="label.updateReportDetailsButton"/> class="button">
                    </c:if>
                </form>
            </c:otherwise>
        </c:choose>
    </c:if>
    <br>
<%--    <br>--%>
<%--    <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>--%>
</div>
</body>
</html>