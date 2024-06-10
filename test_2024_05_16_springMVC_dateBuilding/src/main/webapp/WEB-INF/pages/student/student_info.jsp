<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>选课信息</title>
</head>
<body>
<center>选课信息</center>
<table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
    <tr>
        <td>学号</td>
        <td>姓名</td>
        <td>性别</td>
        <td>课程列表</td>
    </tr>
    <tr>
        <td>${student.sno}</td>
        <td>${student.sname}</td>
        <td>${student.age}</td>
        <td>
            <c:forEach items="${student.courseList}" var="cname">
                ${cname}<br/>
            </c:forEach>
        </td>
</table>
</body>
</html>
