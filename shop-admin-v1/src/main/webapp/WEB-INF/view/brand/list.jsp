<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>品牌列表</title>
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
                    <h3 class="panel-title">品牌查询</h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">品牌名:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="brandName" placeholder="请输入品牌名">
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
        <button type="button" class="btn btn-primary" onclick="toAdd();"><span class="glyphicon glyphicon-plus"></span>增加</button>
        <button type="button" class="btn btn-primary" onclick="showAddDlg();"><span class="glyphicon glyphicon-plus"></span>增加(弹框)</button>
        <button type="button" class="btn btn-danger" onclick="deleteBatch();"><span class="glyphicon glyphicon-trash"></span>批量删除</button>
    </div>
    <div class="row">
        <div class="col-md-12">
            <%--带有分页的表格--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">品牌列表</h3>
                </div>
                <div class="panel-body">
                    <table id="brandTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>品牌名</th>
                            <th>logo</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tfoot>
                        <tr>
                            <th>选择</th>
                            <th>品牌名</th>
                            <th>logo</th>
                            <th>操作</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="addBrandDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">品牌名:</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="add_brandName" name="brandName" placeholder="请输入品牌名">
            </div>
        </div>
        <div class="form-group" style="height: 400px;">
            <label  class="col-sm-2 control-label">Logo:</label>
            <div class="col-sm-5" style="height: 300px;">
                <input type="file" id="add_logoFile" name="imageFile">
                <input type="hidden" id="add_logo">
            </div>
        </div>
    </form>
</div>
<div id="editBrandDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">品牌名:</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="edit_brandName" placeholder="请输入品牌名">
            </div>
        </div>
        <div class="form-group" style="height: 400px;">
            <label  class="col-sm-2 control-label">Logo:</label>
            <div class="col-sm-5" style="height: 300px;">
                <input type="file" id="edit_logoFile" name="imageFile">
                <input type="hidden" id="edit_logo">
                <input type="hidden" id="edit_old_logo">
            </div>
        </div>
    </form>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    //初始化函数
    $(function () {
        initBrandTable();
    });

    //批量删除
    function deleteBatch() {
        //获取选中的复选框的id集合
        var v_idArr = [];
        $("input[name='ids']:checkbox:checked").each(function () {
            console.log($(this).val());
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
                            url:"/brand/deleteBatch.jhtml",
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
        var v_brandHtml = $("#addBrandDiv").html();
        //渲染上传组件
        //配置信息
        var setting = {
            language: 'zh',
            uploadUrl: "/file/uploadImage.jhtml", // 后台上传文件的url地址
            showUpload : false,
            showRemove : false
        };
        $("#add_logoFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
            console.log(r.response.data);
            $("#add_logo",v_brandDlg).val(r.response.data);
        });
        //弹框
        var v_brandDlg = bootbox.dialog({
            title:'增加品牌',
            message:$("#addBrandDiv form"),
            size:"large",
            buttons:{
                confirm:{
                    label:'<span class="glyphicon-exclamation-ok"></span>确认',
                    className:'btn-primary',
                    callback:function () {
                        //获取参数
                        var v_brandName = $("#add_brandName",v_brandDlg).val();
                        var v_logo = $("#add_logo",v_brandDlg).val();
                        var param = {};
                        param.brandName = v_brandName;
                        param.logo = v_logo;
                        console.log(param);
                        //发送ajax请求，传递参数
                        $.post({
                            url:"/brand/addBrandInfo.jhtml",
                            data:param,
                            success:function (result) {
                                if(result.code == 200){
                                    //添加成功，刷新
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
        $("#addBrandDiv").html(v_brandHtml);
    }

    //条件查询
    function search() {
        //获取参数值
        var v_brandName = $("#brandName").val();
        //传参
        var v_param = {};
        v_param.brandName = v_brandName;
        v_brandTable.settings()[0].ajax.data = v_param;
        v_brandTable.ajax.reload();
    }

    //跳转到新增页面
    function toAdd() {
        location.href="/brand/toAdd.jhtml";
    }

    //全局变量
    var v_brandTable;
    //列表展示
    function initBrandTable() {
        v_brandTable = $('#brandTable').DataTable({
            "language": {
                "url": "/js/DataTables/Chinese.json" // 汉化
            },
            // 是否允许检索
            "searching": false,
            "processing": true,
            "lengthMenu": [5,10,15,20],
            "serverSide": true,
            "ajax": {
                "url": "/brand/findList.jhtml",
                "type": "POST"
            },
            "columns": [
                {  "data": "id",
                    "render": function (data, type, row, meta) {
                        console.log(data);
                        return '<input type="checkbox" name="ids" value="'+data+'"/>';
                    }
                },
                { "data": "brandName" },
                { "data": "logo",
                    "render": function (data, type, row, meta) {
                        return '<img src="'+data+'" width="50px" height="50px"/>';
                    }
                },
                { "data": "id" ,
                    "render": function (data, type, row, meta) {
                        return '<div class="btn-group" role="group">' +
                            '<button type="button" class="btn btn-warning" onclick="editBrand(\''+data+'\')"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '<button type="button" class="btn btn-danger" onclick="del(\''+data+'\')"><span class="glyphicon glyphicon-trash"></span>删除</button>' +
                            '</div>';
                    }
                }
            ]
        });
    }

    //回显
    function editBrand(id) {
        $.ajax({
            type:"get",
            url:"/brand/findBrand.jhtml",
            data:{"id":id},
            success:function (result) {
                if(result.code == 200){
                    console.log(result);
                    var v_brand = result.data;
                    //回显数据
                    $("#edit_brandName").val(v_brand.brandName);
                    var imageArr = [];
                    imageArr.push(v_brand.logo);
                    $("#edit_old_logo").val(v_brand.logo);
                    //备份
                    var v_brandHtml = $("#editBrandDiv").html();
                    //配置信息
                    var setting = {
                        language: 'zh',
                        uploadUrl: "/file/uploadImage.jhtml", // 后台上传文件的url地址
                        showUpload : false,
                        showRemove : false,
                        initialPreview:imageArr,
                        initialPreviewAsData:true
                    };
                    //渲染组件
                    $("#edit_logoFile").fileinput(setting).on("fileuploaded", function (event, r, previewId, index) {
                        console.log(r.response.data);
                        $("#edit_logo",v_editBrandDlg).val(r.response.data);
                    });
                    //弹框
                    var v_editBrandDlg = bootbox.dialog({
                        title:'修改品牌',
                        message:$("#editBrandDiv form"),
                        size:"large",
                        buttons:{
                            confirm:{
                                label:'<span class="glyphicon-exclamation-ok"></span>确认',
                                className:'btn-primary',
                                callback:function () { //更新的操作
                                    //获取参数
                                    var v_brandName = $("#edit_brandName",v_editBrandDlg).val();
                                    var v_logo = $("#edit_logo",v_editBrandDlg).val();
                                    var v_old_logo = $("#edit_old_logo",v_editBrandDlg).val();
                                    var param = {};
                                    param.brandName = v_brandName;
                                    param.logo = v_logo;
                                    param.oldLogo = v_old_logo;
                                    param.id = v_brand.id;
                                    console.log(param);
                                    $.post({
                                        url:"/brand/update.jhtml",
                                        data:param,
                                        success:function (result) {
                                            if(result.code == 200){
                                                //刷新
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
                    $("#editBrandDiv").html(v_brandHtml);
                }
            }
        });
    }
    
    //删除
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
                        url:"/brand/del.jhtml",
                        data:{"id":id},
                        success:function (result) {
                            console.log(result);
                            if (result.code == 200){
                                //删除成功，刷新页面,重新查询
                                search();
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
