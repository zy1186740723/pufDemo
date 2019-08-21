<html>
<head>
    <meta charset="utf-8">
    <title>前端试验</title>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<h1><font class="center-pill">不通过无损压缩服务，获取数据库的数据</font></h1>
<li>数据库中的卡号是被加密的，如果不调用无损压缩是无法看到用户的完整信息的</li>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>userId</th>
                    <th>username</th>
                    <th>carId</th>
                    <th>phoneNumber</th>
                    <th colspan="2">操作</th>
                </tr>
                </thead>
                <tbody>

                <#--循环-->
                <#list userInfoPage.getContent() as userInfo>
                    <tr>
                        <td>${userInfo.userId}</td>
                        <td>${userInfo.username}</td>
                        <td>${userInfo.carId}</td>
                        <td>${userInfo.phoneNumber}</td>
                        <td>详情</td>
                        <td>取消</td>
                    </tr>
                </#list>

                </tbody>
            </table>
        </div>
        <#--分页-->
        <div class="col-md-12 column">
            <ul class="pagination pull-right">
                <#if currentPage lte 1>
                    <li class="disabled"><a href="#">上一页</a></li>
                <#else >
                    <li><a href="/pufDemo//findAll?page=${currentPage-1}&size=${size}">上一页</a></li>
                </#if>

                <#list 1..userInfoPage.getTotalPages() as index>
                    <#if currentPage==index>
                        <li class="disabled" ><a href="#">${index}</a></li>
                    <#else>
                        <li><a href="/pufDemo/findAll?page=${index}&size=${size}">${index}</a></li>
                    </#if>

                </#list>

                <#if currentPage gte userInfoPage.getTotalPages() >
                    <li class="disbled"><a href="#">下一页</a></li>
                <#else >
                    <li><a href="/pufDemo/findAll?page=${currentPage+1}&size=${size}">下一页</a></li>
                </#if>

            </ul>
        </div>
    </div>
</div>
</body>
</html>