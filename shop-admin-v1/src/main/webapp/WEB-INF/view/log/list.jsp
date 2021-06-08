<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>日志列表展示</title>
    <!-- Bootstrap -->
    <link href="/js/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <![endif]-->
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <%--查询条件--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">日志查询</h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" id="logForm">
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">登录名:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="userName" name="userName" placeholder="请输入登录名">
                            </div>

                            <label  class="col-sm-2 control-label">真实名称:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="realName" name="realName" placeholder="请输入真实名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">操作信息:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="info" name="info" placeholder="请输入操作信息">
                            </div>

                            <label  class="col-sm-2 control-label">操作时间:</label>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="startDate" name="startDate" placeholder="请输入开始时间">
                                    <span class="input-group-addon" id="sizing-assonl">-</span>
                                    <input type="text" class="form-control" id="endDate" name="endDate" placeholder="请输入结束时间">
                                </div>
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
    <button type="button" class="btn btn-success" onclick="exportExcel();"><span class="glyphicon glyphicon-download-alt"></span>导出Excel</button>
    <button type="button" class="btn btn-success" onclick="exportPdf();"><span class="glyphicon glyphicon-download-alt"></span>导出Pdf</button>
    <button type="button" class="btn btn-success" onclick="exportWord();"><span class="glyphicon glyphicon-download-alt"></span>导出Word</button>
    <div class="row">
        <div class="col-md-12">
            <%--带有分页的表格--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">日志列表</h3>
                </div>
                <div class="panel-body">
                    <table id="logTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th>用户名</th>
                            <th>真实名称</th>
                            <th>操作信息</th>
                            <th>时间</th>
                            <th>详情</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th>用户名</th>
                            <th>真实名称</th>
                            <th>操作信息</th>
                            <th>时间</th>
                            <th>详情</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    //初始化函数
    $(function () {
        initDate();
        initLogTable();
    });

    //渲染日期插件
    function initDate() {
        $('#startDate').datetimepicker({
            format:'YYYY-MM-DD HH:mm',
            locale:'zh-CN',
            showClear:true
        });
        $('#endDate').datetimepicker({
            format:'YYYY-MM-DD HH:mm',
            locale:'zh-CN',
            showClear:true
        });
    }

    //条件查询
    function search() {
        //传参
        var v_param = {};
        v_param.userName = $("#userName").val();
        v_param.realName = $("#realName").val();
        v_param.info = $("#info").val();
        v_param.startDate = $("#startDate").val();
        v_param.endDate = $("#endDate").val();

        v_logTable.settings()[0].ajax.data = v_param;
        v_logTable.ajax.reload();
    }

    //全局变量
    var v_logTable;
    //列表展示
    function initLogTable() {
        v_logTable = $('#logTable').DataTable({
            "language": {
                "url": "/js/DataTables/Chinese.json" // 汉化
            },
            // 是否允许检索
            "searching": false,
            "processing": true,
            "lengthMenu": [5,10,15,20],
            "serverSide": true,
            "ajax": {
                "url": "/log/findList.jhtml",
                "type": "POST"
            },
            "columns": [
                { "data": "userName" },
                { "data": "realName"},
                { "data": "info"},
                { "data": "insertTime"},
                { "data": "paramInfo",
                    "render": function (data, type, row, meta) {
                        return '<button type="button" class="btn btn-primary" onclick="showParamDlg(\''+data+'\');"><span class="glyphicon glyphicon-search"></span>详情</button>';
                    }
                }
            ]
        });
    }

    //导出Pdf
    function exportPdf() {
        var v_logForm = document.getElementById("logForm");
        v_logForm.action="/log/exportPdf.jhtml";
        v_logForm.method="post";
        v_logForm.submit();
    }

    //导出Word
    function exportWord() {
        var v_logForm = document.getElementById("logForm");
        v_logForm.action="/log/exportWord.jhtml";
        v_logForm.method="post";
        v_logForm.submit();
    }

    //导出Excel
    function exportExcel() {
        var v_logForm = document.getElementById("logForm");
        v_logForm.action="/log/exportExcel.jhtml";
        v_logForm.method="post";
        v_logForm.submit();
    }

    //查询详情信息
    function showParamDlg(data) {
        bootbox.alert({
            message:"<span class='glyphicon glyphicon-exclamation-sign'></span>"+data,
            size:'large',
            title:"提示信息"
        });
    }

</script>
</body>
</html>
