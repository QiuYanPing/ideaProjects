<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>修改课程信息</title>
</head>
<body>
<form id="form1" name="form1" method="post" action="courseupdate">
    <table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
        <tr>
            <td width="116" height="30" align="right" valign="middle">课程号：</td>
            <td width="378" align="left" valign="middle">
                <input type="text" name="cno" readonly="readonly" id="cno" value="${course.cno}"/></td>
        </tr>
        <tr>
            <td width="116" height="30" align="right" valign="middle">课程名：</td>
            <td width="378" align="left" valign="middle">
                <input type="text" name="cname" id="cname" value="${course.cname}"/></td>
        </tr>
        <tr>
            <td width="116" height="30" align="right" valign="middle">课时：</td>
            <td width="378" align="left" valign="middle">
                <input type="text" name="period" id="period" value="${course.period}" /></td>
        </tr>
        <tr>
            <td width="116" height="30" align="right" valign="middle">图示号：</td>
            <td width="378" align="left" valign="middle">
                <input type="text" name="bookid" id="bookid" value="${course.bookid}" /></td>
        </tr>

        <tr>
            <td height="30" align="right" valign="middle">&nbsp;</td>
            <td align="left" valign="middle"><input type="submit" name="button" id="button" value="提交" />
                <input type="reset" name="button2" id="button2" value="重置" /></td>
        </tr>
    </table>
</form>
</body>
</html>
