<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>测试Json</title>

</head>
<body>
<table align="center" width="500" border="1" cellspacing="0" cellpadding="0" style="border-collapse:collapse" bordercolor="#0099FF">
    <tr id="table-title">
        <td>书名</td>
        <td>出版社</td>
        <td>价格</td>
    </tr>
</table>

<br/>

<table width="300" border="0" cellspacing="0" cellpadding="0" align="center">
    <tbody>
    <tr>
        <td>书名</td>
        <td><input type="text" id="bname"></td>
    </tr>
    <tr>
        <td>出版社</td>
        <td><input type="text" id="pub"></td>
    </tr>
    <tr>
        <td>价格</td>
        <td><input type="text" id="price"></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><input id="btn-add-book" type="button" value="添加"></td>
    </tr>
    </tbody>
</table>

<script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
    $("#btn-add-book").click(function(){
        var bname = $("#bname").val(),
            pub = $("#pub").val(),
            price = $("#price").val();

        $.post("add_book",{"bname":bname,"pub":pub,"price":price},
            function(data){
                if (data == "OK"){
                    var newBook = "<tr><td>" + bname + "</td><td>" + pub + "</td><td>" + price + "</td></tr>";
                    $("#table-title").after(newBook);
                }
            })
    })
</script>
</body>
</html>
