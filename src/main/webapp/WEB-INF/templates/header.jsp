<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<html>
<head>

</head>
<body>
<p class="header">
    <fmt:message key="label.chooseTheLanguage"/>
    <a href="${pageContext.request.contextPath}/controller?sessionLocale=en"><fmt:message key="label.lang.en"/></a>
    <a href="${pageContext.request.contextPath}/controller?sessionLocale=de"><fmt:message key="label.lang.de"/></a>
    <a href="${pageContext.request.contextPath}/controller?sessionLocale=ru"><fmt:message key="label.lang.ru"/></a>

    <a href="${pageContext.request.contextPath}/controller?command=show_help"><fmt:message
            key="label.helpPageLink"/></a>
</p>
</body>
</html>
