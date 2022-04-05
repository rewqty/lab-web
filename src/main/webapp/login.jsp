<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Login</title>
    </head>
    <body>
        <h1>Вход</h1>

        <form action="auth?form=login" method="post">
            Login: <input type="text" name="login"/><br>
            Password: <input type="password" name="pass"/><br>
            <input class="button" type="submit" value="Войти"/>
        </form>
        <button onclick="location.href = 'auth?form=registration';">Регистрация</button>
    </body>
</html>