<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.jsp">后台管理平台</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="/user/toList.jhtml">用户管理</a></li>
                <li><a href="/brand/toList.jhtml">品牌管理</a></li>
                <li><a href="/log/toList.jhtml">日志管理</a></li>
                <li><a href="/spec/toList.jhtml">规格管理</a></li>
                <li><a href="/type/toList.jhtml">类型管理</a></li>
                <li><a href="/cate/toList.jhtml">分类管理</a></li>
                <li><a href="/spu/toList.jhtml">商品管理</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">欢迎${user.realName}登录</a></li>
                <li><a href="#">上次的登录时间:${loginTime}</a></li>
                <li><a href="#">今天是第${loginCount}次登录</a></li>
                <li><a href="/user/logout.jhtml">注销</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
