<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>品牌增加</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <form class="form-horizontal" action="/brand/addBrand.jhtml" method="post">
                <div class="form-group">
                    <label  class="col-sm-2 control-label">品牌名:</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="brandName" name="brandName" placeholder="请输入品牌名">
                    </div>
                </div>
                <div class="form-group" style="height: 400px;">
                    <label  class="col-sm-2 control-label">Logo:</label>
                    <div class="col-sm-4" style="height: 300px;">
                        <input type="file" id="logoFile" name="imageFile">
                        <input type="hidden" id="logo" name="logo">
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label">年份:</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="createYear" name="createYear" placeholder="请输入年份">
                    </div>
                </div>
                <div style="text-align: center;">
                    <button type="submit" class="btn btn-primary">提交</button>
                    <button type="reset" class="btn btn-default">重置</button>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>
    //初始化函数
    $(function () {
        initImage();
    })
    //渲染文件上传组件
    function initImage() {
        //配置信息
        var setting = {
            language: 'zh',
            uploadUrl: "/file/uploadImage.jhtml", // 后台上传文件的url地址
            showUpload : false,
            showRemove : false
        };
        $("#logoFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
            console.log(r.response.data);
            $("#logo").val(r.response.data);
        });
    }
</script>
</body>
</html>
