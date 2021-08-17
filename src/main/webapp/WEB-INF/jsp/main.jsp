<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>

<html lang="${sessionScope.language}">

<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.main_page"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<%--    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/header.jsp"/>--%>
<%--    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/navigation.jsp"/>--%>
<div class="outer">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${empty sessionScope.userName} ">
                    <p><fmt:message key="label.hello"/></p>
                </c:when>
                <c:otherwise>
                    <p><fmt:message key="label.hello"/>, ${sessionScope.userName}</p>
                </c:otherwise>
            </c:choose>
            <!-- show all the conferences in the system-->

            <h2><fmt:message key="label.conference.section"/></h2>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
            <a href="${pageContext.request.contextPath}/controller?command=main_page"><fmt:message
                    key="label.doNotWorry"/></a>
        </c:when>
        <c:otherwise>
            <c:if test="${sessionScope.userRole eq Role.ADMIN}">
                <div class="link">
                    <a href="${pageContext.request.contextPath}/controller?command=show_create_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message
                            key="label.createConferenceReference"/></a>
                </div>

            </c:if>
            <c:if test="${not empty requestScope.conferences}">
                <table border="2" cellpadding="5">
                    <caption><h3><fmt:message key="label.conference.content"/></h3></caption>
                    <c:choose>
                        <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                            <tr>
                                <th width="30"><fmt:message key="label.conferenceTable.id"/></th>
                                <th width="150"><fmt:message key="label.conferenceTable.title"/></th>
                                <th width="70"><fmt:message key="label.conferenceTable.managerNickname"/></th>
                                <th width="150"><fmt:message
                                        key="label.conferenceTable.updateConferenceReference"/></th>
                            </tr>
                            <c:forEach var="conference" items="${requestScope.conferences}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}">${conference.id}</a>
                                    </td>
                                    <td>${conference.conferenceTitle}</td>
                                    <c:forEach var="user" items="${requestScope.users}">
                                        <c:if test="${conference.managerConf==user.id}">
                                            <td>
                                                <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                                            </td>
                                        </c:if>
                                    </c:forEach>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/controller?command=show_update_conference&conferenceId=${conference.id}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message
                                                key="label.update"/> '${conference.conferenceTitle}'</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <th width="30"><fmt:message key="label.conferenceTable.id"/></th>
                                <th width="150"><fmt:message key="label.conferenceTable.title"/></th>
                                <th width="100"><fmt:message key="label.conferenceTable.managerNickname"/></th>
                            </tr>
                            <c:forEach var="conference" items="${requestScope.conferences}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}">${conference.id}</a>
                                    </td>
                                    <td>${conference.conferenceTitle}</td>
                                    <c:forEach var="user" items="${requestScope.users}">
                                        <c:if test="${conference.managerConf==user.id}">
                                            <td>
                                                <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>
                                            </td>
                                        </c:if>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </table>
            </c:if>


            <%--    <h2><fmt:message key="label.personal.section"/></h2>--%>
            <%--    <c:choose>--%>
            <%--        <c:when test="${empty sessionScope.userName}">--%>
            <%--            <a href="${pageContext.request.contextPath}/controller?command=show_login"><fmt:message--%>
            <%--                    key="label.loginReference"/></a>--%>

            <%--            <br>--%>
            <%--            <br>--%>
            <%--            <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message--%>
            <%--                    key="label.registrationReference"/></a>--%>
            <%--        </c:when>--%>
            <%--        <c:otherwise>--%>
            <%--            <c:if test="${sessionScope.userRole eq Role.ADMIN}">--%>
            <%--                <p><fmt:message key="label.clickToSeeAllUsers"/></p>--%>
            <%--                <a href="${pageContext.request.contextPath}/controller?command=show_users"><fmt:message--%>
            <%--                        key="label.userPageReference"/></a>--%>

            <%--                <br>--%>
            <%--                <p><fmt:message key="label.clickToAddNewConference"/></p>--%>
            <%--                <a href="${pageContext.request.contextPath}/controller?command=show_create_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message--%>
            <%--                        key="label.createConferenceReference"/></a>--%>
            <%--            </c:if>--%>

            <%--            <br>--%>
            <%--            <c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">--%>
            <%--                <p><fmt:message key="label.clickToSeeQuestionsFromUsers"/></p>--%>
            <%--                <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}"><fmt:message--%>
            <%--                        key="label.ShowQuestionsFromUsersReference"/></a>--%>
            <%--            </c:if>--%>

            <%--            <br>--%>
            <%--            <c:if test="${not empty sessionScope.userName}">--%>
            <%--                <p><fmt:message key="label.clickToSeeYourQuestions"/></p>--%>
            <%--                <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantQuestions"><fmt:message--%>
            <%--                        key="label.showYourQuestionsReference"/></a>--%>
            <%--            </c:if>--%>

            <%--            <br>--%>
            <%--            <p><fmt:message key="label.clickToLogOut"/></p>--%>
            <%--            <a href="${pageContext.request.contextPath}/controller?command=logout"><fmt:message--%>
            <%--                    key="label.logoutReference"/></a>--%>
            <%--        </c:otherwise>--%>
            <%--    </c:choose>--%>

            <%--<h3>--%>
            <%--    <fmt:message key="label.chooseSessionLocale"/>--%>
            <%--</h3>--%>
            <%--<ul>--%>
            <%--    <li><a href="${pageContext.request.contextPath}/?sessionLocale=en"><fmt:message key="label.lang.en"/></a></li>--%>
            <%--    <li><a href="${pageContext.request.contextPath}/?sessionLocale=de"><fmt:message key="label.lang.de"/></a></li>--%>
            <%--    <li><a href="${pageContext.request.contextPath}/?sessionLocale=ru"><fmt:message key="label.lang.ru"/></a></li>--%>
            <%--</ul>--%>
        </c:otherwise>
    </c:choose>

</div>
</body>
<%--<footer >--%>
<%--    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/footer.jsp"/>--%>
<%--</footer>--%>
</html>
