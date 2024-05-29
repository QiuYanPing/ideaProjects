<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加学生信息</title>
</head>
<body>
<form id="form1" name="form1" method="post" action="studentinsert">
  <table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
    <tr>
      <td width="116" height="30" align="right" valign="middle">学号：</td>
      <td width="378" align="left" valign="middle">
        <input type="text" name="sno" id="sno" /></td>
    </tr>
    <tr>
      <td width="116" height="30" align="right" valign="middle">姓名：</td>
      <td width="378" align="left" valign="middle">
        <input type="text" name="sname" id="sname" /></td>
    </tr>
    <tr>
      <td width="116" height="30" align="right" valign="middle">性别：</td>
      <td width="378" align="left" valign="middle">
        <input type="text" name="ssex" id="ssex" /></td>
    </tr>
    <tr>
      <td width="116" height="30" align="right" valign="middle">籍贯：</td>
      <td width="378" align="left" valign="middle">
        <input type="text" name="snative" id="snative" /></td>
    </tr>
    <tr>
      <td width="116" height="30" align="right" valign="middle">专业：</td>
      <td width="378" align="left" valign="middle">
        <input type="text" name="mno" id="mno" /></td>
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
