<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品列表</title>
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
                    <h3 class="panel-title">商品查询</h3>
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">商品名:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="spuName" placeholder="请输入商品名">
                            </div>
                            <label  class="col-sm-2 control-label">价格:</label>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="startPrice" name="startPrice">
                                    <span class="input-group-addon">-</span>
                                    <input type="text" class="form-control" id="endPrice" name="endPrice">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label  class="col-sm-2 control-label">库存:</label>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="startStock" name="startStock">
                                    <span class="input-group-addon">-</span>
                                    <input type="text" class="form-control" id="endStock" name="endStock">
                                </div>
                            </div>
                            <label  class="col-sm-2 control-label">品牌名:</label>
                            <div class="col-sm-4" id="brandDiv">

                            </div>
                        </div>
                        <div class="form-group" id="cateDiv">
                            <label  class="col-sm-2 control-label">分类:</label>
                        </div>
                        <div style="text-align: center;">
                            <button type="button" class="btn btn-primary" onclick="search()"><span class="glyphicon glyphicon-search"></span> 查询</button>
                            <button type="button" class="btn btn-default" onclick="location.reload();"><span class="glyphicon glyphicon-refresh"></span> 重置</button>
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
                    <h3 class="panel-title">商品列表</h3>
                </div>
                <div class="panel-body">
                    <table id="spuTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th></th>
                            <th>选择</th>
                            <th>商品名</th>
                            <th>价格</th>
                            <th>库存</th>
                            <th>品牌名</th>
                            <th>分类名</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th></th>
                            <th>选择</th>
                            <th>商品名</th>
                            <th>价格</th>
                            <th>库存</th>
                            <th>品牌名</th>
                            <th>分类名</th>
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
        location.href="/spu/toAdd.jhtml";
    }

    function editSpu(id) {
        location.href="/spu/toEdit.jhtml?id="+id;
    }

    $(function () {
        initSpuTable();
        initBrand();
        initCate(0);
    });

    //条件查询
    function search() {
        // 获取参数值
        var v_len = $("select[name='cateSelect']").length;
        // 传参
        var v_param = {};
        v_param.spuName = $("#spuName").val();
        v_param.startPrice = $("#startPrice").val();
        v_param.endPrice =$("#endPrice").val();
        v_param.startStock =$("#startStock").val();
        v_param.endStock = $("#endStock").val();
        v_param.brandId = $("#brandSelect").val();
        for (var i = 0; i < v_len; i++) {
            v_param["cate"+(i+1)] = $($("select[name='cateSelect']")[i]).val();
        }
        v_spuTable.settings()[0].ajax.data = v_param;
        v_spuTable.ajax.reload();
        console.log(v_param);
    }

    function initBrand() {
        $.get({
            url:"/brand/findBrandList.jhtml",
            success:function (result) {
                if(result.code == 200){
                    var brandList = result.data;
                    var v_html = '<select class="form-control" id="brandSelect"><option value="-1">===请选择===</option> ';
                    for (let brand of brandList){
                        v_html += '<option value="'+brand.id+'" data-brand-name="'+brand.brandName+'">'+brand.brandName+'</option>';
                    }
                    v_html += '</select>';
                    $("#brandDiv").html(v_html);
                }
            }
        });
    }

    function initCate(id,obj) {
        if(obj){
            $(obj).parent().nextAll().remove();
        }
        if(id > 0){
            //证明该向后台发送请求，获取对应的品牌列表
            $.post({
                url:"/brand/findBrandList.jhtml?cateId="+id,
                success:function (res) {
                    console.log(res);
                    var brandList = res.data;
                    if(brandList){
                        var v_html = '<select class="form-control" id="brandSelect"><option value="-1">===请选择===</option> ';
                        for (let brand of brandList){
                            v_html += '<option value="'+brand.id+'" data-brand-name="'+brand.brandName+'">'+brand.brandName+'</option>';
                        }
                        v_html += '</select>';
                        $("#brandDiv").html(v_html);
                    }
                }
            });
        }
        $.get({
            url:"/cate/findCateChilds.jhtml?id="+id,
            success:function (result) {
                console.log(result);
                if(result.code == 200){
                    var v_cateList = result.data;
                    if(v_cateList.length == 0){
                        //最后一级分类
                        /*var v_typeId = $(obj).find("option:selected").data("type-id");
                        initSpuInfo(v_typeId);*/
                        return;
                    }
                    var v_html = '<div class="col-sm-2"><select class="form-control" name="cateSelect" onchange="initCate(this.value,this)"><option value="-1">===请选择===</option>';
                    for(let v_cate of v_cateList){
                        v_html += '<option value="'+v_cate.id+'" data-type-id="'+v_cate.typeId+'" data-cate-name="'+v_cate.cateName+'">'+v_cate.cateName+'</option>';
                    }
                    v_html += '</select></div>';
                    $("#cateDiv").append(v_html);
                }
            }
        });
    }

    var v_spuTable;
    function initSpuTable() {
        v_spuTable = $('#spuTable').DataTable({
            "language": {
                "url": "/js/DataTables/Chinese.json" // 汉化
            },
            // 是否允许检索
            "searching": false,
            "processing": true,
            "lengthMenu": [5,10,15,20],
            "serverSide": true,
            "ajax": {
                "url": "/spu/findList.jhtml",
                "type": "POST"
            },
            "columns": [
                {
                    "className":      'details-control',
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": ''
                },
                {  "data": "spu.id",
                    "render": function (data, type, row, meta) {
                        return '<input type="checkbox" name="ids" value="'+data+'"/>';
                    }
                },
                { "data": "spu.spuName" },
                { "data": "spu.price" },
                { "data": "spu.stock" },
                { "data": "spu.brandName" },
                { "data": "spu.cateName" },
                { "data": "spu.id" ,
                    "render": function (data, type, row, meta) {
                        var Html = '<div class="btn-group" role="group">' +
                            '<button type="button" class="btn btn-warning" onclick="editSpu(\''+data+'\')"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '<button type="button" class="btn btn-danger" onclick="del(\''+data+'\')"><span class="glyphicon glyphicon-trash"></span>删除</button>';
                            if(row.spu.status == 1){
                                Html += '<button type="button" class="btn btn-danger" onclick="sj(\''+data+'\')"><span class="glyphicon glyphicon-circle-arrow-down"></span>下架</button>';
                            }else {
                                Html += '<button type="button" class="btn btn-success" onclick="xj(\''+data+'\')"><span class="glyphicon glyphicon-circle-arrow-up"></span>上架</button>';
                            }
                            if(row.spu.news == 1){
                                Html += '<button type="button" class="btn btn-danger" onclick="newNo(\''+data+'\')"><span class="glyphicon glyphicon-star-empty"></span>非新品</button>';
                            }else {
                                Html += '<button type="button" class="btn btn-success" onclick="newYes(\''+data+'\')"><span class="glyphicon glyphicon-star"></span>新品</button>';
                            }
                            if(row.spu.recommend == 1){
                                Html += '<button type="button" class="btn btn-danger" onclick="tuiNo(\''+data+'\')"><span class="glyphicon glyphicon-heart-empty"></span>不推荐</button>';
                            }else {
                                Html += '<button type="button" class="btn btn-success" onclick="tuiYes(\''+data+'\')"><span class="glyphicon glyphicon-heart"></span>推荐</button>';
                            }
                            Html += '</div>';
                        return Html;

                        /*var

                        return '<div class="btn-group" role="group">' +
                            '<button type="button" class="btn btn-warning" onclick="editSpu(\''+data+'\')"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '<button type="button" class="btn btn-danger" onclick="del(\''+data+'\')"><span class="glyphicon glyphicon-trash"></span>删除</button>' +
                            '</div>';*/
                    }
                },
            ]
        });
        $('#spuTable tbody').on('click', 'td.details-control', function () {
            var tr = $(this).closest('tr');
            var row = v_spuTable.row( tr );

            if ( row.child.isShown() ) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');
            }
            else {
                // Open this row
                row.child( format(row.data()) ).show();
                tr.addClass('shown');
            }
        } );
    }

    function tuiYes(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateTuiYes.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function tuiNo(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateTuiNo.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function sj(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateSJ.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function xj(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateXJ.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function newYes(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateNewYes.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function newNo(id) {
        $.ajax({
            type:"post",
            url:"/spu/updateNewNo.jhtml",
            data:{"id":id},
            success:function (res) {
                if(res.code == 200){
                    search();
                }
            }
        });
    }

    function format ( d ) {

        console.log(d.skuList);

        var v_skuList = d.skuList;

        var v_html = '<div class="row">';

        for (let sku of v_skuList) {

            v_html += '<div class="col-md-6"><table class="table table-striped table-bordered" style="width:100%">';

            v_html += '<tr><td>图片:</td><td><img src="'+sku.image+'" width="100px" height="100px"/></td></tr>';

            v_html += '<tr><td>SKU名字</td><td>'+sku.skuName+'</td></tr>';

            v_html += '<tr><td>价格</td><td>'+sku.price+'</td></tr>';

            v_html += '<tr><td>库存</td><td>'+sku.stock+'</td></tr>';

            v_html += '</table></div>';
        }

        v_html += '</div>';

        return v_html;
    }

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
                        url:"/spu/del.jhtml",
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
                            url:"/spu/deleteBatch.jhtml",
                            data:{"ids":v_ids},
                            success:function (result) {
                                if(result.code == 200){
                                    //删除成功，刷新页面,重新查询
                                    search();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

</script>
</body>
</html>
