<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2023/1/7
  Time: 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile"></br>
    <input type="text" name="userName"></br>
    <input type="submit" value="提交"></br>
</form>
</body>
</html>
