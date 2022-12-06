<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
        <h2>Bitrix processes</h2>
    <c:forEach var="process" items="${processes}">

        <c:url var="AddButton" value="/AddProcessElement">
            <c:param name="ProcessID" value="${process.id}"/>
        </c:url>

        ${process.name}
        <input type="button" value="Add element" onclick="window.location.href = '${AddButton}'">
        <br/><br/>
    </c:forEach>
</body>
</html>
