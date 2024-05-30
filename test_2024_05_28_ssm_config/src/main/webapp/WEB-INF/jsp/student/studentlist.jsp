<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>学生信息列表</title>
  <script>
    function frmSubmit(){
      document.form1.submit();
    }
  </script>
</head>
<body>
<center>学生信息</center>
<form id="form1" name="form1" method="post" action="studentdelete">
  <table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
    <tr>
      <td>选择</td>
      <td>学号</td>
      <td>姓名</td>
      <td>性别</td>
      <td>籍贯</td>
      <td>专业号</td>
      <td>操作&nbsp;&nbsp;<a href="studentpreinsert">添加</a>&nbsp;&nbsp;<a href="#" onclick="javascript:frmSubmit();">删除</a></td>
    </tr>
    <c:forEach items="${studentList}" var="student">
      <tr>
        <td><input type="checkbox" name="snoArray" value="${student.sno}"></td>
        <td>${student.sno}</td>
        <td>${student.sname}</td>
        <td>${student.ssex}</td>
        <td>${student.snative}</td>
        <td>${student.mno}</td>
        <td>
          <a href="studentpreupdate?sno=${student.sno}&sname=${student.sname}&ssex=${student.ssex}&snative=${student.snative}&mno=${student.mno}">修改</a>
        </td>
      </tr>
    </c:forEach>
  </table>
</form>
</body>
</html>
