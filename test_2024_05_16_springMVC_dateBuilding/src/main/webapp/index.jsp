<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<%--<c:out value="${param.username}" default="unknown"/>
<c:forEach var="i" begin="10" end="10" step="1">${i}</c:forEach>--%>
<h2>Hello World!</h2>
<a href="course/to_course_add_basic">添加课程（基本数据类型绑定）</a><br/>
<a href="course/to_course_add_pojo">添加课程（POJO绑定）</a><br/>
<a href="course/to_course_add_pojo_nest">添加课程（包装POJO类绑定）</a><br/>
<a href="student/to_add_student">学生选课</a><br/>
<a href="book/to_add_list">图书列表</a><br/>


</body>
</html>