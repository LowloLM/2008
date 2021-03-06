<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Insert title here</title>
    <script src="js/jquery.min.js"></script>

    <!--响应式框架-->
    <link rel='stylesheet' href='/js/login/css/bootstrap.min.css'>

    <!--主要样式-->
    <link rel="stylesheet" href="/js/login/css/style.css">

    <link href="/js/bootstrap-3.4.1-dist/css/bootstrap.min.css" rel="stylesheet" />
    <script src="/js/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>

    <!-- 引入boobox弹框插件的css文件和js文件 -->
    <script src="/js/bootstrap-bootbox/bootbox.min.js"></script>
    <script src="/js/bootstrap-bootbox/bootbox.locales.min.js"></script>

</head>
<body>

<div class="container">

    <div class="card-wrap">

        <div class="card border-0 shadow card--welcome is-show" id="welcome">
            <div class="card-body">
                <h2 class="card-title">欢迎光临</h2>
                <div class="btn-wrap">
                    <a class="btn btn-lg btn-register js-btn" data-target="register">注册</a>
                    <a class="btn btn-lg btn-login js-btn" data-target="login">登录</a><br>
                </div>
            </div>
        </div>



  <!--------=登录页面 ------------------------------>
        <div class="card border-0 shadow card--login" id="login">
            <div class="card-body">
                <h2 class="card-title">欢迎登录！</h2>
                <form id="loginForm">
                	<!-- 用户名 -->
                    <div class="form-group">
                        <input class="form-control" type="text" placeholder="用户名" required="required" value="admin" name="username" id="userName" autocomplete="off">
                    </div>
                   <!-- 密码 -->
                    <div class="form-group">
                        <input class="form-control" type="password" placeholder="密码" required="required" value="123" id="password" name="password" autocomplete="off">
                    </div>
                    <button class="btn btn-lg" type="button" onclick="login()">登录</button>
                </form>
            </div>
            <button class="btn btn-back js-btn" data-target="welcome"><i class="fas fa-angle-left"></i></button>
        </div>


<!-- ----------------------注册页面---- -->
        <div class="card border-0 shadow card--register" id="register">
            <div class="card-body">
                <h2 class="card-title">会员注册</h2>
                <form id="registerForm">
                    <!-- 用户名-->
                    <div class="form-group">
                        <input class="form-control" type="text" placeholder="用户名" required="required" name="username" autocomplete="off">
                    </div>
                    <!-- 密码-->
                    <div class="form-group">
                        <input class="form-control" type="password" placeholder="密码" required="required" name="password" autocomplete="off">
                    </div>
                    <!-- 确认密码-->
                    <div class="form-group">
                        <input class="form-control" type="password" placeholder="确认密码" required="required" id="confirmPass" name="confirmPass" autocomplete="off">
                    </div>
                    <button class="btn btn-lg" type="button" onclick="register()">注册</button>
                </form>
            </div>
            <button class="btn btn-back js-btn" data-target="welcome"><i class="fas fa-angle-left"></i></button>
        </div>

    </div>
</div>
<script type="text/javascript" src="/js/bootstrap-3.4.1-dist/js/index.js"></script>
<script src="/js/md5.js"></script>
</body>
<script type="text/javascript">
	
	function login(){
		
        var v_userName = $("#login #userName").val();
        var v_pwd = $("#login #password").val();

        $.post({
            url:"/user/login.jhtml",
            data:{"loginName":v_userName,"password":hex_md5(v_pwd)},
            success:function (result) {
                if(result.code == 200){
                    window.location.href="/brand/toList.jhtml";
                } else {
                    alert(result.msg);
                }
            }
        });
		
	}
</script>
</html>