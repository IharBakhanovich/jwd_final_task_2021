<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epam.jwd.Conferences.dto.ReportType" %>
<%@page import="com.epam.jwd.Conferences.dto.Role" %>
<html>
<head>
    <style>
        <%@include file="/resources/appStyle.css"%>
    </style>
    <title>Report</title>
</head>
<body>
<c:if test="${not empty requestScope.report}">

    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <h3>Error</h3>
        </c:when>
        <c:otherwise>
            <h3>Report</h3>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <p class="error_message">${requestScope.error}</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_report&id=${requestScope.report.get().id}">Try
                again</a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller?command=update_report&updaterId=${sessionScope.userId}&updaterRole=${sessionScope.userRole}"
                  method="post">
                <label for="idField"> Id:</label>
                <input type="text" id="idField" name="id" value="${requestScope.report.get().id}" readonly>
                <!-- name запихнет в пост запрос значения -->
                <br>
                <label for="sectionField"> Section:</label>
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
                        <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="section" items="${requestScope.sections}">
                            <c:if test="${requestScope.report.get().sectionId==section.id}">
                                <c:choose>
                                    <c:when test="${requestScope.report.get().applicant==sessionScope.userId}">
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
                        <%--                        <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}" readonly>--%>
                    </c:otherwise>
                </c:choose>


                    <%--                <c:choose>--%>
                    <%--                    <c:when test="${requestScope.user.get().firstName == 'default@email.com'} ">--%>
                    <%--                        <input type="text" id="emailField" name="email">--%>
                    <%--                    </c:when>--%>
                    <%--                    <c:otherwise>--%>
                    <%--                        <input type="text" id="sectionField" name="email" value="${requestScope.user.get().email}">--%>
                    <%--                    </c:otherwise>--%>
                    <%--                </c:choose>--%>
                    <%--                <!-- name запихнет в пост запрос значения -->--%>
                <br>
                <label for="conferenceField"> Conference: </label>
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
                        <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="conference" items="${requestScope.conferences}">
                            <c:if test="${requestScope.report.get().conferenceId==conference.id}">
                                <c:choose>
                                    <c:when test="${requestScope.report.get().applicant==sessionScope.userId}">
                                        <input type="text" id="conferenceField" name="conferenceTitle"
                                               value="${conference.conferenceTitle}" readonly>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}"
                                           id="conferenceField" name="conferenceTitle">${conference.conferenceTitle}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>
                        <%--                        <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}" readonly>--%>
                    </c:otherwise>
                </c:choose>


                    <%--                <input type="text" id="nicknameField" name="nickname" value="${requestScope.user.get().nickname}"--%>
                    <%--                       readonly>--%>
                <br>
                <label for="reportTextField"> ReportText: </label>
                <textarea name="reportText" id="reportTextField" rows="1" cols="75"
                          wrap="soft">${requestScope.report.get().reportText}</textarea>
                    <%--                <input type="text" id="reportTextField" name="reportText"--%>
                    <%--                       value="${requestScope.report.get().reportText}">--%>
                <br>
                <label for="reportTypeField"> ReportType: </label>
                <c:choose>
                    <c:when test="${sessionScope.userRole eq Role.ADMIN or sessionScope.userId == requestScope.report.get().applicant}">
                        <select name="reportType" id="reportTypeField">
                            <c:forEach var="reportType" items="${ReportType.valuesAsList()}">
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
                        <%--                <input type="text" id="roleField" name="role" value="${requestScope.user.get().role}">--%>
                    </c:when>
                    <c:otherwise>
                        <input type="text" id="reportTypeField" name="reportType"
                               value="${requestScope.report.get().reportType}" readonly>
                    </c:otherwise>
                </c:choose>
                <br>
                <label for="applicantField"> Applicant: </label>
                <c:forEach var="user" items="${requestScope.users}">
                    <c:if test="${report.get().applicant==user.id}">
                        <c:choose>
                            <c:when test="${requestScope.report.get().applicant==sessionScope.userId}">
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
                    <%--                <input type="text" id="surNameField" name="surname" value="${requestScope.user.get().surname}">--%>
                <br>
                <br>
                <c:if test="${sessionScope.userRole eq Role.ADMIN or requestScope.report.get().applicant == sessionScope.userId}">
                    <input type="submit" value="Update report details" class="button">
                </c:if>
            </form>
        </c:otherwise>
    </c:choose>

    <%--    <tr>--%>
    <%--        <th>Id</th>--%>
    <%--        <th>Section</th>--%>
    <%--        <th>Conference</th>--%>
    <%--        <th>ReportText</th>--%>
    <%--        <th>ReportType</th>--%>
    <%--        <th>Applicant</th>--%>
    <%--    </tr>--%>
    <%--    <br>--%>
    <%--    <tr>--%>
    <%--        <td>${requestScope.report.get().id}</td>--%>
    <%--        <c:forEach var="section" items="${requestScope.sections}">--%>
    <%--            <c:if test="${report.get().sectionId==section.id}">--%>
    <%--                <td>--%>
    <%--                    <a href="${pageContext.request.contextPath}/controller?command=show_reports&id=${section.id}&conferenceId=${report.get().conferenceId}&&sectionName=${section.sectionName}">${section.sectionName}</a>--%>
    <%--                </td>--%>
    <%--            </c:if>--%>
    <%--        </c:forEach>--%>
    <%--        <c:forEach var="conference" items="${requestScope.conferences}">--%>
    <%--            <c:if test="${report.get().conferenceId==conference.id}">--%>
    <%--                <td>--%>
    <%--                    <a href="${pageContext.request.contextPath}/controller?command=show_sections&id=${conference.id}&conferenceTitle=${conference.conferenceTitle}">${conference.conferenceTitle}</a>--%>
    <%--                </td>--%>
    <%--            </c:if>--%>
    <%--        </c:forEach>--%>
    <%--        <td>${requestScope.report.get().reportText}</td>--%>
    <%--        <td>${requestScope.report.get().reportType}</td>--%>
    <%--        <c:forEach var="user" items="${requestScope.users}">--%>
    <%--            <c:if test="${report.get().applicant==user.id}">--%>
    <%--                <td>--%>
    <%--                    <a href="${pageContext.request.contextPath}/controller?command=show_user&id=${user.id}">${user.nickname}</a>--%>
    <%--                </td>--%>
    <%--            </c:if>--%>
    <%--        </c:forEach>--%>
    <%--    </tr>--%>
</c:if>
<br>
<br>
<a href="${pageContext.request.contextPath}/controller">Back to the main page</a>

</body>
</html>