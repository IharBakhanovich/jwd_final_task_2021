<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<style>
    <%@include file="/resources/appStyle.css"%>
</style>
<html>

<body>
<p><fmt:message key="label.conferences"/></p>
</body>
</html>
