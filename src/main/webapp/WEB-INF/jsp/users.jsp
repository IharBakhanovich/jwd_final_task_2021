<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<html>
<head>
    <title><fmt:message key="label.usersPage"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<%--<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/header.jsp"/>--%>
<div class="outer">
    <h2><fmt:message key="label.usersTable"/></h2>
    <c:if test="${not empty sessionScope.userName and sessionScope.userRole eq Role.ADMIN}">
        <div class="link">
            <a href="${pageContext.request.contextPath}/controller?command=show_create_new_user"><fmt:message
                    key="label.createNewUser"/></a>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.users}">
        <table border="2" cellpadding="5">
            <caption><h3><fmt:message key="label.users"/></h3></caption>
            <tr>
                <th width="30"><fmt:message key="label.userIdColumn"/></th>
                <th><fmt:message key="label.nicknameColumn"/></th>
                <th><fmt:message key="label.roleColumn"/></th>
            </tr>
            <c:forEach var="user" items="${requestScope.users}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.id}</a>
                    </td>
                    <td>${user.nickname}</td>
                    <td>${user.role}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

<%--    <br>--%>
<%--    <br>--%>
<%--    <a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>--%>
</div>
</body>
<%--<footer class="footer">--%>
<%--    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/footer.jsp"/>--%>
<%--</footer>--%>
</html>