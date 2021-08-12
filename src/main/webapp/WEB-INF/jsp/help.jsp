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
    <title><fmt:message key="label.helpPage"/></title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/composition.jsp"/>
<div class="outer">
    <h3><fmt:message key="label.aboutTheApplication"/></h3>
    <p>
        <fmt:message key="label.aboutTheApplication_text"/>
    </p>

    <h3><fmt:message key="label.aboutTheRoles"/></h3>
    <p><fmt:message key="label.aboutTheRoles_text"/></p>

    <h3><fmt:message key="label.aboutNavigation"/></h3>

    <h4><fmt:message key="label.unauthorizedUsers"/></h4>
    <p><fmt:message key="label.unauthorizedUsers_textPart1"/></p>
    <p><fmt:message key="label.unauthorizedUsers_textPart2"/></p>
    <h4><fmt:message key="label.authorizedUsers"/></h4>
    <p><fmt:message key="label.authorizedUsers_text"/></p>

    <h4><fmt:message key="label.moderator"/></h4>
    <p><fmt:message key="label.moderator_text"/></p>

    <h4><fmt:message key="label.administrator"/></h4>
    <p><fmt:message key="label.administrator_text"/></p>

    <h3><fmt:message key="label.aboutReports"/></h3>
    <h4><fmt:message key="label.reportsStructure"/></h4>
    <p><fmt:message key="label.reportsStructure_text_Part1"/></p>
    <p><fmt:message key="label.reportsStructure_text_Part2"/></p>
    <br>
    <br>
</div>
</body>
</html>
