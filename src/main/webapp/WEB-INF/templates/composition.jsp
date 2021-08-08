<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
<%--    <title>Conferences</title>--%>
</head>
<body>

    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/header.jsp"/>

    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/navigation.jsp"/>

    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/templates/footer.jsp"/>


</body>
<%--<footer class="footer">--%>
<%--    --%>
<%--</footer>--%>
</html>
