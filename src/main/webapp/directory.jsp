<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Explorer</title>
</head>
<body>
    <a href="<c:url value="${parentPath}"/>">Вверх</a>
    <table>
        <tr>
            <th>Файл</th>
            <th>Размер</th>
            <th>Дата</th>
        </tr>

        <c:forEach items="${listFiles}" var="file">
            <tr>
                <td><a href="<c:url value="${file[0]}"/>">${file[1]}</a></td>
                <td>${file[2]}</td>
                <td>${file[3]}</td>
            </tr>
        </c:forEach>

        <c:forEach items="${listDirectories}" var="directory">
            <tr>
                <td><a href="<c:url value="${directory[0]}"/>">${directory[1]}</a></td>
                <td></td>
                <td>${directory[3]}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>