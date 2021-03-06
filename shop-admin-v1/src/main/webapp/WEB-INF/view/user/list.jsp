<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户管理</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <%--查询条件--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">用户查询</h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">用户名:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="userName" placeholder="请输入用户名">
                            </div>

                            <label  class="col-sm-2 control-label">真实名称:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="realName" placeholder="请输入真实名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">生日:</label>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="startDate" name="startDate" placeholder="请输入开始时间">
                                    <span class="input-group-addon" id="sizing-assonl">-</span>
                                    <input type="text" class="form-control" id="endDate" name="endDate" placeholder="请输入结束时间">
                                </div>
                            </div>

                            <label  class="col-sm-2 control-label">性别:</label>
                            <div class="col-sm-4">
                                <input type="radio" name="sex" value="1">男
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="sex" value="0">女
                            </div>
                        </div>
                        <div style="text-align: center;">
                            <button type="button" class="btn btn-primary" onclick="search()"><span class="glyphicon glyphicon-search"></span> 查询</button>
                            <button type="reset" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span> 重置</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button type="button" class="btn btn-primary" onclick="showAddDlg();"><span class="glyphicon glyphicon-plus"></span>增加(弹框)</button>
        <button type="button" class="btn btn-danger" onclick="deleteBatch();"><span class="glyphicon glyphicon-trash"></span>批量删除</button>
        <button type="button" class="btn btn-success" onclick="importExcel();"><span class="glyphicon glyphicon-upload"></span>导入Excel</button>
    </div>
    <div class="row">
        <div class="col-md-12">
            <%--带有分页的表格--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">用户列表</h3>
                </div>
                <div class="panel-body">
                    <table id="userTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>用户名</th>
                            <th>性别</th>
                            <th>头像</th>
                            <th>真实名称</th>
                            <th>生日</th>
                            <th>邮箱</th>
                            <th>电话</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th>选择</th>
                            <th>用户名</th>
                            <th>性别</th>
                            <th>头像</th>
                            <th>真实名称</th>
                            <th>生日</th>
                            <th>邮箱</th>
                            <th>电话</th>
                            <th>操作</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="importExcelDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group" style="height: 400px;">
            <label  class="col-sm-2 control-label">请选择要导入的Excel文件:</label>
            <div class="col-sm-5" style="height: 300px;">
                <input type="file" id="add_ExcelFile" name="uploadFileInfo">
                <input type="hidden" id="add_Excel">
            </div>
        </div>
    </form>
</div>
<div id="addUserDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">用户名:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_userName" placeholder="请输入用户名">
            </div>

            <label  class="col-sm-2 control-label">真实名称:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_realName" placeholder="请输入真实名称">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">密码:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_password" placeholder="请输入密码">
            </div>

            <label  class="col-sm-2 control-label">确认密码:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_confirm_password" placeholder="请再次输入密码">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">生日:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_birthday">
            </div>

            <label  class="col-sm-2 control-label">性别:</label>
            <div class="col-sm-4">
                <input type="radio" name="add_sex" value="1">男
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="add_sex" value="0">女
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">邮箱:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_mail" placeholder="请输入邮箱">
            </div>

            <label  class="col-sm-2 control-label">电话:</label>
                <div class="col-sm-4">
                <input type="text" class="form-control" id="add_phone" placeholder="请输入电话">
            </div>
        </div>
        <div class="form-group" style="height: 400px;">
            <label  class="col-sm-2 control-label">头像:</label>
            <div class="col-sm-5" style="height: 300px;">
                <input type="file" id="add_headImageFile" name="imageFile">
                <input type="hidden" id="add_headImage">
            </div>
        </div>
    </form>
</div>
<%--修改--%>
<div id="editUserDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">用户名:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="edit_userName" name="edit_userName" placeholder="请输入用户名">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">真实名称:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="edit_realName" name="edit_realName" placeholder="请输入真实名称">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">密码:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="edit_password" name="edit_password" placeholder="请输入密码">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-2">性别</label>
            <div class="col-md-5">
                <label class="radio-inline">
                    <input type="radio" name="edit_sex" value="1">男
                </label>
                <label class="radio-inline">
                    <input type="radio" name="edit_sex" value="0">女
                </label>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">邮箱:</label>
            <div class="col-sm-4">
                <input type="email" class="form-control" id="edit_mail" placeholder="请输入邮箱">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">手机号:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="edit_phone" placeholder="请输入邮箱">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-2">生日时间</label>
            <div class="col-md-5">
                <input type="text" name="edit_birthday" class="form-control" id="edit_birthday" value="">
            </div>
        </div>
        <div class="form-group" style="height: 400px;">
            <label  class="col-sm-2 control-label">Logo:</label>
            <div class="col-sm-5" style="height: 300px;">
                <input type="file" id="edit_logoFile" name="imageFile">
                <input type="hidden" id="edit_img">
                <input type="hidden" id="edit_old_img">
            </div>
        </div>
    </form>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    //初始化函数
    $(function () {
        initDate();
        initUserTable();
    });

    function importExcel() {
        //备份
        var v_Html = $("#importExcelDiv").html();
        //渲染上传组件
        //配置信息
        var setting = {
            language: 'zh',
            uploadUrl: "/file/uploadFile.jhtml", // 后台上传文件的url地址
            showUpload : false,
            showRemove : false
        };
        $("#add_ExcelFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
            console.log(r.response.data);
            $("#add_Excel",v_Dlg).val(r.response.data);
        });
        //弹框
        var v_Dlg = bootbox.dialog({
            title:'导入Excel',
            message:$("#importExcelDiv form"),
            size:"large",
            buttons:{
                confirm:{
                    label:'<span class="glyphicon-exclamation-ok"></span>确认',
                    className:'btn-primary',
                    callback:function () {
                        var v_filePath = $("#add_Excel",v_Dlg).val();
                        $.post({
                            url:"/user/importExcel.jhtml",
                            data:{"filePath":v_filePath},
                            success:function (result) {
                                if(result.code == 200){
                                    search();
                                }
                            }
                        });
                    }
                },
                cancel:{
                    label:'<span class="glyphicon-exclamation-remove"></span>取消',
                    className:'btn-danger'
                }
            }
        });
        //还原
        $("#importExcelDiv").html(v_Html);
    }

    //渲染日期插件
    function initDate() {
        $('#startDate').datetimepicker({
            format:'YYYY-MM-DD',
            locale:'zh-CN',
            showClear:true
        });
        $('#endDate').datetimepicker({
            format:'YYYY-MM-DD',
            locale:'zh-CN',
            showClear:true
        });
    }

    // 批量删除
    function deleteBatch() {
        //获取选中的复选框的id集合
        var v_idArr = [];
        $("input[name='ids']:checkbox:checked").each(function () {
            v_idArr.push($(this).val());
        });
        if(v_idArr.length == 0){
            //至少要选中一个
            bootbox.alert({
                message:"<span class='glyphicon glyphicon-exclamation-sign'></span>请选择要删除的记录",
                size:'small',
                title:"提示信息"
            });
        } else {
            bootbox.confirm({
                message:"你确定删除吗？",
                size:'small',
                title:"提示信息",
                buttons:{
                    confirm:{
                        label:'<span class="glyphicon-exclamation-ok"></span>确定',
                        className:'btn-success'
                    },
                    cancel:{
                        label:'<span class="glyphicon-exclamation-remove"></span>取消',
                        className:'btn-danger'
                    }
                },
                callback:function (result) {
                    if(result){
                        var v_ids = v_idArr.join(",");
                        $.post({
                            url:"/user/deleteBatch.jhtml",
                            data:{"ids":v_ids},
                            success:function (result) {
                                if(result.code == 200){
                                    search();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    //新增(弹框)
    function showAddDlg() {
        //备份
        var v_userHtml = $("#addUserDiv").html();
        //渲染上传组件
        //配置信息
        var setting = {
            language: 'zh',
            uploadUrl: "/file/uploadImage.jhtml", // 后台上传文件的url地址
            showUpload : false,
            showRemove : false
        };
        $("#add_headImageFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
            console.log(r.response.data);
            $("#add_headImage",v_userDlg).val(r.response.data);
        });
        //弹框
        var v_userDlg = bootbox.dialog({
            title:'增加用户',
            message:$("#addUserDiv form"),
            size:"large",
            buttons:{
                confirm:{
                    label:'<span class="glyphicon-exclamation-ok"></span>确认',
                    className:'btn-primary',
                    callback:function () {
                        //获取参数
                        var v_param = {};
                        v_param.loginName = $("#add_userName",v_userDlg).val();
                        v_param.realName = $("#add_realName",v_userDlg).val();
                        v_param.mail = $("#add_mail",v_userDlg).val();
                        v_param.headImage = $("#add_headImage",v_userDlg).val();
                        v_param.sex = $("input[name='add_sex']:radio:checked",v_userDlg).val();
                        v_param.phone = $("#add_phone",v_userDlg).val();
                        v_param.birthday = $("#add_birthday",v_userDlg).val();
                        v_param.password = $("#add_password",v_userDlg).val();
                        v_param.confirmPassword = $("#add_confirm_password",v_userDlg).val();
                        console.log(v_param);
                        //发送ajax请求，传递参数
                        $.post({
                            url:"/user/addUser.jhtml",
                            data:v_param,
                            success:function (result) {
                                if(result.code == 200){
                                    //添加成功，刷新
                                    search();
                                }else if(result.code == -1){
                                    bootbox.alert({
                                        message:"<span class='glyphicon glyphicon-exclamation-sign'></span>新增失败，系统异常",
                                        size:'small',
                                        title:"提示信息"
                                    });
                                }
                            }
                        });
                    }
                },
                cancel:{
                    label:'<span class="glyphicon-exclamation-remove"></span>取消',
                    className:'btn-danger'
                }
            }
        });
        //还原
        $("#addUserDiv").html(v_userHtml);
    }

    //条件查询
    function search() {
        //获取参数值
        //传参
        var v_param = {};
        v_param.userName = $("#userName").val();
        v_param.realName = $("#realName").val();
        v_param.startDate = $("#startDate").val();
        v_param.endDate = $("#endDate").val();
        v_param.sex = $("input[name='sex']:radio:checked").val();
        v_userTable.settings()[0].ajax.data = v_param;
        v_userTable.ajax.reload();
    }

    //全局变量
    var v_userTable;
    //列表展示
    function initUserTable() {
        v_userTable = $('#userTable').DataTable({
            "language": {
                "url": "/js/DataTables/Chinese.json" // 汉化
            },
            // 是否允许检索
            "searching": false,
            "processing": true,
            "lengthMenu": [5,10,15,20],
            "serverSide": true,
            "ajax": {
                "url": "/user/findList.jhtml",
                "type": "POST"
            },
            "columns": [
                {  "data": "id",
                    "render": function (data, type, row, meta) {
                        console.log(data);
                        return '<input type="checkbox" name="ids" value="'+data+'"/>';
                    }
                },
                { "data": "loginName" },
                { "data": "sex",
                    "render": function (data, type, row, meta) {
                        return data==1?"男":"女";
                    }
                },
                { "data": "headImage",
                    "render": function (data, type, row, meta) {
                        return '<img src="'+data+'" width="50px" height="50px"/>';
                    }
                },
                { "data": "realName" },
                { "data": "birthday" },
                { "data": "mail" },
                { "data": "phone" },
                { "data": "id" ,
                    "render": function (data, type, row, meta) {
                        return '<div class="btn-group" role="group">' +
                            '<button type="button" class="btn btn-warning" onclick="editUser(\''+data+'\')"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '<button type="button" class="btn btn-danger" onclick="del(\''+data+'\')"><span class="glyphicon glyphicon-trash"></span>删除</button>' +
                            '</div>';
                    }
                }
            ]
        });
    }

    function editUser(id) {
        $.ajax({
            type:"get",
            url:"/user/findUser.jhtml",
            data:{"id":id},
            success:function (result) {
                if (result.code == 200) {
                    console.log(result);
                    var v_user = result.data;
                    // 回填数据
                    $("#edit_userName").val(v_user.loginName);
                    $("#edit_realName").val(v_user.realName);
                    $("#edit_password").val(v_user.password);
                    $("#edit_mail").val(v_user.mail);
                    $("#edit_birthday").val(v_user.birthday);
                    $("#edit_phone").val(v_user.phone);
                    $("input[name='edit_sex'][value='"+v_user.sex+"']").prop("checked",true);
                    // 备份
                    var v_userHtml = $("#editUserDiv").html();
                    var imageArr = [];
                    imageArr.push(v_user.headImage);
                    $("#edit_old_img").val(v_user.headImage);
                    // 渲染
                    // 配置信息
                    var setting = {
                        language: 'zh',
                        uploadUrl: "/file/uploadImage.jhtml", // 后台上传文件的url地址
                        showUpload : false,
                        showRemove : false,
                        initialPreview:imageArr,
                        initialPreviewAsData: true
                    };
                    // 渲染组件
                    $("#edit_logoFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
                        console.log(r.response.data);
                        $("#edit_img", v_editUserDlg).val(r.response.data);
                    });
                    //渲染日期插件
                    $("#edit_birthday", v_editUserDlg ).datetimepicker({
                        format:'YYYY-MM-DD',
                        locale:'zh-CN',
                        showClear:true
                    });
                    // 弹框
                    var v_editUserDlg = bootbox.dialog({
                        title: '修改品牌',
                        message:$("#editUserDiv form"),
                        size:"large",
                        buttons: {
                            confirm: {
                                label: '<span class="glyphicon glyphicon-ok"></span>确认',
                                className: 'btn-primary',
                                callback: function(){
                                    // 更新的操作
                                    //获取参数
                                    var v_userName = $("#edit_userName", v_editUserDlg).val();
                                    var v_realName = $("#edit_realName", v_editUserDlg).val();
                                    var v_password = $("#edit_password", v_editUserDlg).val();
                                    var v_img = $("#edit_img", v_editUserDlg).val();
                                    var v_sex = $("[name=edit_sex]:checked",v_editUserDlg).val();
                                    var v_phone = $("#edit_phone", v_editUserDlg).val();
                                    var v_birthday = $("#edit_birthday", v_editUserDlg).val();
                                    var v_mail = $("#edit_mail", v_editUserDlg).val();
                                    var v_old_img = $("#edit_old_img", v_editUserDlg).val();
                                    var param = {};
                                    param.loginName = v_userName;
                                    param.realName = v_realName;
                                    param.password = v_password;
                                    param.headImage = v_img;
                                    param.sex = v_sex;
                                    param.phone = v_phone;
                                    param.birthday = v_birthday;
                                    param.mail = v_mail;
                                    param.oldLogo = v_old_img;
                                    param.id = v_user.id;
                                    console.log(param);
                                    $.ajax({
                                        type:"post",
                                        url:"/user/update.jhtml",
                                        data:param,
                                        success:function (result) {
                                            if (result.code == 200) {
                                                // 刷新
                                                search();
                                            }
                                        }
                                    });
                                }
                            },
                            cancel: {
                                label: '<span class="glyphicon glyphicon-remove"></span>取消',
                                className: 'btn-danger'
                            }
                        }
                    });
                    // 还原
                    $("#editUserDiv").html(v_userHtml);
                }
            }
        })
    }

    // 删除
    function del(id) {
        bootbox.confirm({
            message:"你确定删除吗？",
            size:'small',
            title:"提示信息",
            buttons:{
                confirm:{
                    label:'<span class="glyphicon-exclamation-ok"></span>确定',
                    className:'btn-success'
                },
                cancel:{
                    label:'<span class="glyphicon-exclamation-remove"></span>取消',
                    className:'btn-danger'
                }
            },
            callback:function (result) {
                if(result){
                    $.post({
                        url:"/user/del.jhtml?id="+id,
                        success:function (result) {
                            console.log(result);
                            if (result.code == 200){
                                //删除成功，刷新页面,重新查询
                                search();
                            }else if(result.code == -1){
                                bootbox.alert({
                                    message:"<span class='glyphicon glyphicon-exclamation-sign'></span>删除失败，系统异常",
                                    size:'small',
                                    title:"提示信息"
                                });
                            }
                        }
                    });
                }
            }
        });
    }

</script>
</body>
</html>
