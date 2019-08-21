<html>
<head>
    <title>
        Freemarker的表单提交
    </title>
    <meta http-equiv="Content-Type">
    <meta charset="UTF-8">
</head>
<body>
<h1><font class="center-pill">注册成功！！！返回用户注册信息！</font></h1>
    <div class="default-color0">返回的用户信息中，银行卡号被加密！</div>
    <ul>
        <li>姓名：${userInfo.username}</font> </li><br/>
        <li> 卡号：${userInfo.carId}</li><br/>
        <li> 电话号码：${userInfo.phoneNumber}</li><br/>
        <li> 身份证号码：${userInfo.userId}</li><br/>

    </ul>

</body>
</html>