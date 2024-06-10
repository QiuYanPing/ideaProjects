<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<html>
<head>
    <title>课程信息</title>
</head>
<body>
<p>课程编号：${course.cid}</p>
<p>课程名称：${course.cname}</p>
<p>学时数：${course.period}</p>
<p>教材名称：${course.bname}</p>
<p>出版社：${course.pub}</p>
<p>价格：${course.price}</p>
</body>
</html>
