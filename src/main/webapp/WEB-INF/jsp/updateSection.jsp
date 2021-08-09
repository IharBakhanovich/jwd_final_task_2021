<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.SectionUpdate"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <h3><fmt:message key="label.toUpdateSectionInConferenceEditATitlePart1"/> '${requestScope.sectionName}'
                <fmt:message key="label.toUpdateSectionInConferenceEditATitlePart2"/> '${requestScope.conferenceTitle}'
                <fmt:message key="label.toUpdateSectionInConferenceEditATitlePart3"/></h3>
        </c:otherwise>
    </c:choose>
    <br>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <c:choose>
                <c:when test="${(sessionScope.userRole eq Role.ADMIN) or (sessionScope.userId == requestScope.conferenceManagerId) or (sessionScope.userId == requestScope.sectionManager)}">
                    <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                    <a href="${pageContext.request.contextPath}/controller?command=show_update_section&conferenceId=${requestScope.conferenceId}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${requestScope.sectionId}&conferenceManagerId=${requestScope.conferenceManagerId}"><fmt:message
                            key="label.try_again"/></a>
                </c:when>
                <c:otherwise>
                    <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
                    <a href="${pageContext.request.contextPath}/controller?command=show_main_page"><fmt:message
                            key="label.doNotTryAgain"/></a>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=update_section&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}&conferenceTitle=${requestScope.conferenceTitle}&sectionId=${requestScope.sectionId}&conferenceManagerId=${requestScope.conferenceManagerId}"
                  method="post">
                <label for="conferenceIdField"><fmt:message key="label.conferenceId"/></label>
                <input type="text" id="conferenceIdField" name="conferenceId" value="${requestScope.conferenceId}"
                       readonly>
                <br>

                <label for="sectionNameField"><fmt:message key="label.sectionName"/></label>
                <input type="text" id="sectionNameField" name="sectionName" value="${requestScope.sectionName}">

                <br>
                <label for="managerField"><fmt:message key="label.sectionManager"/></label>
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
                                <input type="text" id="managerField" name="managerSectNickname" value="${user.nickname}"
                                       readonly>
                            </c:if>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

                <br>
                <br>
                <input type="submit" value=<fmt:message key="label.SubmitSectionUpdateButton"/> class="button">
                    <%--                                            <c:choose>--%>
                    <%--                                                <c:when test="${sessionScope.userRole eq Role.ADMIN}">--%>

                    <%--                                                </c:when>--%>
                    <%--                                                <c:otherwise>--%>

                    <%--                                                </c:otherwise>--%>
                    <%--                                            </c:choose>--%>
            </form>
        </c:otherwise>
    </c:choose>

<%--    <br>--%>
<%--    <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>--%>
</div>
</body>
</html>
