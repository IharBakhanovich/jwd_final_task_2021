<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title><fmt:message key="label.userPage"/></title>
</head>
<body>
<c:if test="${not empty requestScope.user}">
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3><fmt:message key="label.error"/></h3>
        </c:when>
        <c:otherwise>
            <h3><fmt:message key="label.user"/></h3>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p class="error_message"><fmt:message key="label.${requestScope.error}"/></p>
            <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${requestScope.user.get().id}"><fmt:message key="label.try_again"/></a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=update_user&updaterId=${sessionScope.userId}&updaterRole=${sessionScope.userRole}" method="post">
                <label for="idField"><fmt:message key="label.id"/></label>
                <input type="text" id="idField" name="id" value="${requestScope.user.get().id}" readonly>
                <br>
                <label for="emailField"><fmt:message key="label.email"/></label>
                <c:choose>
                    <c:when test="${requestScope.user.get().firstName == 'default@email.com'} ">
                        <input type="text" id="emailField" name="email">
                    </c:when>
                    <c:otherwise>
                        <input type="text" id="emailField" name="email" value="${requestScope.user.get().email}">
                    </c:otherwise>
                </c:choose>
                <br>
                <label for="nicknameField"><fmt:message key="label.nicknameFieldName"/></label>
                <input type="text" id="nicknameField" name="nickname" value="${requestScope.user.get().nickname}" readonly>
                <br>
                <label for="firstNameField"><fmt:message key="label.firstName"/></label>
                <input type="text" id="firstNameField" name="firstName" value="${requestScope.user.get().firstName}">
                <br>
                <label for="surNameField"><fmt:message key="label.surname"/></label>
                <input type="text" id="surNameField" name="surname" value="${requestScope.user.get().surname}">
                <br>
                <label for="roleField"><fmt:message key="label.role"/></label>
                <c:choose>
                    <c:when test="${sessionScope.userRole eq Role.ADMIN}">
                        <c:choose>
                            <c:when test="${requestScope.user.get().role eq Role.ADMIN}">
                                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}" readonly>
                            </c:when>
                            <c:otherwise>
                                <select name="role" id="roleField">
                                    <c:forEach var="role" items="${Role.valuesAsList()}">
                                        <c:choose>
                                            <c:when test="${requestScope.user.get().role eq role}">
                                                <option selected>${role}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option>${role}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}" readonly>
                    </c:otherwise>
                </c:choose>
                <br>
                <c:if test="${sessionScope.userRole eq Role.ADMIN or requestScope.user.get().id == sessionScope.userId}">
                    <input type="submit" value=<fmt:message key="label.updateUserDetailsButton"/> class="button">
                </c:if>
            </form>
        </c:otherwise>
    </c:choose>
</c:if>
<br>
<br>
<a href="${pageContext.request.contextPath}/controller"><fmt:message key="label.backToMainPage"/></a>
</body>
</html>
