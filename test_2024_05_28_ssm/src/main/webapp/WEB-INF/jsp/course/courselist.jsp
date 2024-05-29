<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>课程信息列表</title>
    <script>
        function frmSubmit(){
            document.form1.submit();
        }
    </script>
</head>
<body>
<center>课程信息</center>
<form id="form1" name="form1" method="post" action="coursedelete">
    <table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
        <tr>
            <td>选择</td>
            <td>课程号</td>
            <td>课程名</td>
            <td>课时</td>
            <td>图书号</td>
            <td>操作&nbsp;&nbsp;<a href="coursepreinsert">添加</a>&nbsp;&nbsp;<a href="#" onclick="javascript:frmSubmit();">删除</a></td>
        </tr>
        <c:forEach items="${courseList}" var="course">
            <tr>
                <td><input type="checkbox" name="cnoArray" value="${course.cno}"></td>
                <td>${course.cno}</td>
                <td>${course.cname}</td>
                <td>${course.period}</td>
                <td>${course.bookid}</td>
                <td>
                    <a href="coursepreupdate?cno=${course.cno}&cname=${course.cname}&period=${course.period}&bookid=${course.bookid}">修改</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>
