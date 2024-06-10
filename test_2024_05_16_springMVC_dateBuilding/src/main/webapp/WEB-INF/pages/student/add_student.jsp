<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>选修课程</title>
</head>
<body>

<form id="form1" name="form1" method="post" action="add_student"><center>
  <table width="500" border="0" cellspacing="1" cellpadding="0" bgcolor="#0099FF">
    <tr>
      <td height="30" colspan="2" align="center" valign="middle"><span style="color:#FFF">选课信息</span></td>
    </tr>
    <tr>
      <td width="153" height="30" align="right" valign="middle" bgcolor="#FFFFFF">学号：</td>
      <td width="344" align="left" valign="middle" bgcolor="#FFFFFF"><label for="textfield"></label>
        <input type="text" name="sno" id="textfield" /></td>
    </tr>
    <tr>
      <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">姓名：</td>
      <td align="left" valign="middle" bgcolor="#FFFFFF">
        <input type="text" name="sname" id="textfield2" /></td>
    </tr>
    <tr>
      <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">年龄：</td>
      <td align="left" valign="middle" bgcolor="#FFFFFF">
        <input type="text" name="age" id="textfield3" /></td>
    </tr>
    <tr>
      <td height="30" align="right" valign="middle" bgcolor="#FFFFFF">课程列表：</td>
      <td align="left" valign="middle" bgcolor="#FFFFFF"><p>
        <label>
          <input type="checkbox" name="courseList" value="大学英语" id="courseName_0" />大学英语</label>
        <br />
        <label>
          <input type="checkbox" name="courseList" value="高等数学" id="courseName_1" />高等数学</label>
        <br />
        <label>
          <input type="checkbox" name="courseList" value="程序设计基础" id="courseName_2" />程序设计基础</label>
        <br />
        <label>
          <input type="checkbox" name="courseList" value="计算机网络" id="courseName_3" />计算机网络</label>
        <br />
      </p></td>
    </tr>
    <tr>
      <td height="30" colspan="2" align="center" valign="middle" bgcolor="#FFFFFF"><input type="submit" name="button" id="button" value="提交" />
        &nbsp;
        <input type="reset" name="button2" id="button2" value="重置" /></td>
    </tr>
  </table></center>
</form>
</body>
</html>
