<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Registration</title>
    </head>
    <body>
        <h1>Регистрация</h1>

        <form action="auth?form=registration" method="post">
            Login: <input type="text" name="login"/><br>
			Email: <input type="text" name="email"/><br>
            Password: <input type="password" name="pass"/><br>
            <input class="button" type="submit" value="Зарегистрироваться"/>
        </form>
    </body>
</html>