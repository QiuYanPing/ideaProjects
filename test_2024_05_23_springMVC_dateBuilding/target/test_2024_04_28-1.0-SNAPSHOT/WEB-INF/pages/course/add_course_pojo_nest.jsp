<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>添加课程信息</title>
</head>
<body>
<center>
    <form id="form1" name="form1" method="post" action="course_add_pojo_nest">
        <table width="500" height="95" border="0" cellpadding="0" cellspacing="1" style="background-color: #3A8ECD; color: #000;">
            <tr>
                <td height="30" colspan="2" align="center" valign="middle" bgcolor="#FFFFFF">课程信息</td>
            </tr>
            <tr>
                <td width="114" height="30" align="right" valign="middle" bgcolor="#FFFFFF">课程编号：</td>
                <td width="227" height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input name="cid" type="text" size="10" /></td>
            </tr>
            <tr>
                <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">课程名称：</td>
                <td height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input type="text" name="cname" /></td>
            </tr>
            <tr>
                <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">学时数：</td>
                <td height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input name="period" type="text" value="0" size="10" /></td>
            </tr>
            <tr>
                <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">教材名称：</td>
                <td height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input type="text" name="book.bname" /></td>
            </tr>
            <tr>
                <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">出版社：</td>
                <td height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input type="text" name="book.pub" /></td>
            </tr>
            <tr>
                <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">价格：</td>
                <td height="20" align="left" valign="middle" bgcolor="#FFFFFF">
                    <input name="book.price" type="text" value="0." size="10" /></td>
            </tr>
            <tr>
                <td height="30" colspan="2" align="center" valign="middle" bgcolor="#FFFFFF"><input type="submit" name="button" id="button" value="提交" />&nbsp;&nbsp;
                    <input type="reset" name="button2" id="button2" value="重置" /></td>
            </tr>
        </table>
    </form>
</center>
</body>
</html>
