<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>规格列表</title>
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
                    <h3 class="panel-title">规格查询</h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">规格名:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="specName" placeholder="请输入规格名">
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
        <button type="button" class="btn btn-danger" onclick="deleteBatch();"><span class="glyphicon glyphicon-trash"></span>批量删除</button>
    </div>
    <div class="row">
        <div class="col-md-12">
            <%--带有分页的表格--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">规格列表</h3>
                </div>
                <div class="panel-body">
                    <table id="specTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>规格名</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th>选择</th>
                            <th>规格名</th>
                            <th>操作</th>
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

    //跳转到新增页面
    function toAdd() {
        location.href="/spec/toAdd.jhtml";
    }

    //初始化函数
    $(function () {
        initSpecTable();
    })

    //条件查询
    function search() {
        //获取参数值
        var v_specName = $("#specName").val();
        //传参
        var v_param = {};
        v_param.specName = v_specName;
        v_specTable.settings()[0].ajax.data = v_param;
        v_specTable.ajax.reload();
    }

    //全局变量
    var v_specTable;
    //列表展示
    function initSpecTable() {
        v_specTable = $('#specTable').DataTable({
            "language": {
                "url": "/js/DataTables/Chinese.json" // 汉化
            },
            // 是否允许检索
            "searching": false,
            "processing": true,
            "lengthMenu": [5,10,15,20],
            "serverSide": true,
            "ajax": {
                "url": "/spec/findList.jhtml",
                "type": "POST"
            },
            "columns": [
                {  "data": "id",
                    "render": function (data, type, row, meta) {
                        console.log(data);
                        return '<input type="checkbox" name="ids" value="'+data+'"/>';
                    }
                },
                { "data": "specName" },
                { "data": "id" ,
                    "render": function (data, type, row, meta) {
                        return '<div class="btn-group" role="group">' +
                            '<button type="button" class="btn btn-warning" onclick="editSpec(\''+data+'\')"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '<button type="button" class="btn btn-danger" onclick="deleteSpec(\''+data+'\')"><span class="glyphicon glyphicon-trash"></span>删除</button>' +
                            '</div>';
                    }
                }
            ]
        });
    }

    function editSpec(id) {
        location.href="/spec/toEdit.jhtml?id="+id;
    }
    
    function deleteSpec(id) {
        $.post({
            url:"/spec/deleteSpec.jhtml",
            data:{"id":id},
            success:function (result) {
                if(result.code == 200){
                    search();
                }
            }
        })
    }
    
    function deleteBatch() {
        var idArr = [];
        $("input[name='ids']:checkbox:checked").each(function () {
            idArr.push(this.value);
        })
        if(idArr.length > 0){
            var ids = idArr.join(",");
            $.post({
                url:"/spec/deleteBatch.jhtml",
                data:{"ids":ids},
                success:function (result) {
                    if(result.code == 200){
                        search();
                    }
                }
            })
        }
    }

</script>
</body>
</html>
