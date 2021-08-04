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
    <title><fmt:message key="label.main_page" /></title>
</head>
<body>
<c:choose>
    <c:when test="${empty sessionScope.userName} ">
        <p><fmt:message key="label.hello" /></p>
    </c:when>
    <c:otherwise>
        <p><fmt:message key="label.hello" />, ${sessionScope.userName}</p>
    </c:otherwise>
</c:choose>
<!-- show all the conferences in the system-->

<h2><fmt:message key="label.conference.section" /></h2>
<c:if test="${not empty requestScope.conferences}">
    <h3><fmt:message key="label.conference.content" /></h3>
    <c:choose>
        <c:when test="${sessionScope.userRole eq Role.ADMIN}">
            <tr>
                <th><fmt:message key="label.conferenceTable.id" /></th>
                <th><fmt:message key="label.conferenceTable.title" /></th>
                <th><fmt:message key="label.conferenceTable.managerNickname" /></th>
                <th><fmt:message key="label.conferenceTable.updateConferenceReference" /></th>
            </tr>
            <br>
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
                        <a href="${pageContext.request.contextPath}/controller?command=show_update_conference&conferenceId=${conference.id}&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}">Update
                            '${conference.conferenceTitle}'</a>
                    </td>
                </tr>
                <br>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <th><fmt:message key="label.conferenceTable.id" /></th>
                <th><fmt:message key="label.conferenceTable.title" /></th>
                <th><fmt:message key="label.conferenceTable.managerNickname" /></th>
            </tr>
            <br>
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
                <br>
            </c:forEach>
        </c:otherwise>
    </c:choose>

</c:if>

<h2><fmt:message key="label.personal.section" /></h2>
<!-- не хотим показывать незарегистрированным userам-->
<c:choose>
    <c:when test="${empty sessionScope.userName}">

        <!-- ссылка это a тег. pageContext.request.contextPath подставит автоматически contextPath когда таковой появиться -->
        <a href="${pageContext.request.contextPath}/controller?command=show_login"><fmt:message key="label.loginReference" /></a>
        <br>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message key="label.registrationReference" /></a>
        <br>
    </c:when>
    <c:otherwise>
        <c:if test="${sessionScope.userRole eq Role.ADMIN}">
            <p><fmt:message key="label.clickToSeeAllUsers" /></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_users"><fmt:message key="label.userPageReference" /></a>

            <br>
            <br>
            <p><fmt:message key="label.clickToAddNewConference" /></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_create_conference&creatorId=${sessionScope.userId}&creatorRole=${sessionScope.userRole}"><fmt:message key="label.createConferenceReference" /></a>
        </c:if>

        <br>
        <br>
        <c:if test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">
            <p><fmt:message key="label.clickToSeeQuestionsFromUsers" /></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}"><fmt:message key="label.ShowQuestionsFromUsersReference" /></a>
        </c:if>

        <br>
        <c:if test="${not empty sessionScope.userName}">
            <p><fmt:message key="label.clickToSeeYourQuestions" /></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_own_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}&sectionName=applicantQuestions"><fmt:message key="label.showYourQuestionsReference" /></a>
        </c:if>

<%--        <c:choose>--%>
<%--            <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userRole eq Role.MANAGER}">--%>
<%--                <p>Click below to see questions from users</p>--%>
<%--                <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Show--%>
<%--                    questions</a>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <c:if test="${not empty sessionScope.userName}">--%>
<%--                    <p>Click below to see your questions to admin</p>--%>
<%--                    <a href="${pageContext.request.contextPath}/controller?command=show_questions&managerId=${sessionScope.userId}&managerRole=${sessionScope.userRole}">Show--%>
<%--                        questions</a>--%>
<%--                </c:if>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>


        <br>
        <br>
        <p><fmt:message key="label.clickToLogOut" /></p>
        <a href="${pageContext.request.contextPath}/controller?command=logout"><fmt:message key="label.logoutReference" /></a>
    </c:otherwise>
</c:choose>

<h3>
    <fmt:message key="label.chooseSessionLocale" />
</h3>
<ul>
    <li><a href="${pageContext.request.contextPath}/?sessionLocale=en"><fmt:message key="label.lang.en" /></a></li>
    <li><a href="${pageContext.request.contextPath}/?sessionLocale=de"><fmt:message key="label.lang.de" /></a></li>
    <li><a href="${pageContext.request.contextPath}/?sessionLocale=ru"><fmt:message key="label.lang.ru" /></a></li>
</ul>
</body>
</html>
