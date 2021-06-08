<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>分类列表</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12" id="cateTableDiv">
            <%--属性表格组件--%>
            <table id="cateTable" class="table table-striped table-bordered" style="width:100%">
                <thead>
                    <tr>
                        <th>分类名<button type="button" class="btn btn-primary" onclick="addFatherCate();"><span class="glyphicon glyphicon-plus"></span>增加父级</button></th>
                        <th>类型名</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>
<div id="add_cateDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">分类名:</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="add_cateName" placeholder="请输入分类名">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">上级分类:</label>
            <div class="col-sm-5" id="add_cateSelectDiv">

            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">类型:</label>
            <div class="col-sm-5" id="add_typeDiv">

            </div>
        </div>
    </form>
</div>
<div id="add_cateFatherDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">分类名:</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="addFather_cateName" placeholder="请输入分类名">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">类型:</label>
            <div class="col-sm-5" id="addFather_typeDiv">

            </div>
        </div>
    </form>
</div>
<div id="edit_cateDiv" style="display: none">
    <form class="form-horizontal">
        <div class="form-group">
            <label  class="col-sm-2 control-label">分类名:</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="edit_cateName" placeholder="请输入分类名">
            </div>
        </div>
        <div class="form-group" id="edit_selectDiv">
            <label  class="col-sm-2 control-label">上级分类:</label>
            <div class="col-sm-5" id="edit_cateSelectDiv">

            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">类型:</label>
            <div class="col-sm-5" id="edit_typeDiv">

            </div>
        </div>
    </form>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>
    $(function () {
        //备份
        v_htmls = $("#cateTableDiv").html();
        initTreeTable();
    })

    var v_sortList = [];
    
    function initTreeTable() {
        //还原
        $("#cateTableDiv").html(v_htmls);
        $.ajax({
            type:"get",
            url:"/cate/findList.jhtml",
            success:function (result) {
                console.log(result);
                if (result.code == 200) {
                    var v_cateList = result.data;
                    v_sortList = [];
                    sortCateList(v_cateList,v_sortList,0,0);
                    var v_html = "";
                    for (let cate of v_sortList) {
                        v_html += '<tr data-tt-id="'+cate.id+'" data-tt-parent-id="'+cate.fatherId+'">\n' +
                            '    <td>'+cate.cateName+'<button type="button" class="btn btn-primary" onclick="addCate(\''+cate.id+'\');"><span class="glyphicon glyphicon-plus"></span>增加子级</button></td>\n' +
                            '    <td>'+cate.typeName+'</td>\n' +
                            '    <td>' +
                            '        <button type="button" class="btn btn-warning" onclick="editCate(\''+cate.id+'\');"><span class="glyphicon glyphicon-pencil"></span>编辑</button>' +
                            '        <button type="button" class="btn btn-danger" onclick="delCate(\''+cate.id+'\');"><span class="glyphicon glyphicon-trash"></span>删除</button>' +
                            '    </td>\n' +
                            '</tr>';
                    }
                    $("#cateTable tbody").html(v_html);
                    // 渲染
                    $("#cateTable").treetable({expandable: true,initialState:'expanded'})
                }
            }
        })
    }

    function editCate(id) {
        $.post({
            url:"/cate/findCate.jhtml",
            data:{"id":id},
            success:function (result) {
                if(result.code == 200){
                    console.log(result);
                    var v_cate = result.data.cate;
                    var v_typeList = result.data.typeList;
                    //备份
                    var v_html = $("#edit_cateDiv").html();
                    //回填值
                    $("#edit_cateName").val(v_cate.cateName);
                    //构建下拉列表并回填
                    if(v_cate.fatherId == 0){
                        $("#edit_selectDiv").css("display","none");
                    }else {
                        buildCateSelect("edit_cateSelect","edit_cateSelectDiv",v_cate.fatherId);
                    }
                    //构建单选按钮
                    buildTypeRadio("edit_typeDiv",v_typeList);
                    $("input[name='typeId']:radio").each(function () {
                        this.checked = this.value == v_cate.typeId;
                    })
                    //弹出对话框
                    var v_cateDlg = bootbox.dialog({
                        title:'修改分类',
                        message:$("#edit_cateDiv form"),
                        size:"large",
                        buttons:{
                            confirm:{
                                label:'<span class="glyphicon-exclamation-ok"></span>确认',
                                className:'btn-primary',
                                callback:function () {
                                    //获取信息
                                    var v_param = {};
                                    v_param["cate.cateName"] = $("#edit_cateName",v_cateDlg).val();
                                    v_param["cate.fatherId"] = $("#edit_cateSelect",v_cateDlg).val();
                                    v_param["cate.typeId"] = $("input[name='typeId']:radio:checked",v_cateDlg).val();
                                    v_param["cate.typeName"] = $("input[name='typeId']:radio:checked",v_cateDlg).attr("fhName");
                                    v_param["cate.id"] = id;
                                    var v_subIdList = [];
                                    getSubTree(id,v_subIdList);
                                    v_subIdList.push(id);
                                    v_param.ids = v_subIdList.join(",");
                                    console.log(v_param);
                                    $.post({
                                        url:"/cate/updateCate.jhtml",
                                        data:v_param,
                                        success:function (result) {
                                            if(result.code == 200){
                                                initTreeTable();
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
                    $("#edit_cateDiv").html(v_html);
                }
            }
        })
    }
    
    function buildTypeRadio(typeDivId,v_typeList) {
        var v_typeHtml = "";
        for (let v_type of v_typeList) {
            v_typeHtml += '<input type="radio" name="typeId" fhName="'+v_type.typeName+'" value="'+v_type.id+'" />'+v_type.typeName;
        }
        $("#"+typeDivId).html(v_typeHtml);
    }
    
    function buildCateSelect(selectId,selectDivId,cateId) {
        var v_cateHtml = "<select class=\"form-control\" id='"+selectId+"'>";
        for (let cate of v_sortList) {
            var str = "";
            for (let i = 0; i < cate.level; i++) {
                str += "--";
            }
            v_cateHtml += '<option value="'+cate.id+'">'+str+cate.cateName+'</option>';
        }
        v_cateHtml += "</select>";
        $("#"+selectDivId).html(v_cateHtml);
        $("#"+selectId).val(cateId);
    }
    
    function delCate(id) {
        bootbox.confirm({
            message:"将会删除当前节点以及下面的所有子子孙孙节点，你确定删除吗？",
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
                    //获取节点下的子子孙孙
                    var subIdList = [];
                    getSubTree(id,subIdList);
                    subIdList.push(id);
                    console.log(subIdList);
                    $.post({
                        url:"/cate/deleteCate.jhtml",
                        data:{
                            "ids":subIdList.join(",")
                        },
                        success:function (result) {
                            if(result.code == 200){
                                initTreeTable();
                            }
                        }
                    })
                }
            }
        });
    }
    
    function getSubTree(id,subIdList) {
        for (let cate of subIdList) {
            if(id == cate.fatherId){
                subIdList.push(cate.id);
                getSubTree(cate.id,subIdList);
            }
        }
    }

    function addCate(cateId) {
        //备份
        var v_html = $("#add_cateDiv").html();
        //渲染
        $.post({
            url:"/type/findAllType.jhtml",
            success:function (result) {
                if(result.code == 200){
                    var v_typeList = result.data;
                    //构建单选按钮
                    buildTypeRadio("add_typeDiv",v_typeList);
                    //构建下拉列表并回填
                    buildCateSelect("add_cateSelect","add_cateSelectDiv",cateId);
                    //弹框
                    var v_cateDlg = bootbox.dialog({
                        title:'增加分类',
                        message:$("#add_cateDiv form"),
                        size:"large",
                        buttons:{
                            confirm:{
                                label:'<span class="glyphicon-exclamation-ok"></span>确认',
                                className:'btn-primary',
                                callback:function () {
                                    //获取信息
                                    var v_param = {};
                                    v_param.cateName = $("#add_cateName",v_cateDlg).val();
                                    v_param.fatherId = $("#add_cateSelect",v_cateDlg).val();
                                    v_param.typeId = $("input[name='typeId']:radio:checked",v_cateDlg).val();
                                    v_param.typeName = $("input[name='typeId']:radio:checked",v_cateDlg).attr("fhName");
                                    console.log(v_param);
                                    $.post({
                                       url:"/cate/addCate.jhtml",
                                        data:v_param,
                                        success:function (result) {
                                            if(result.code == 200){
                                                initTreeTable();
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
                    $("#add_cateDiv").html(v_html);
                }
            }
        })
    }

    function addFatherCate() {
        //备份
        var v_html = $("#add_cateFatherDiv").html();
        //渲染
        $.post({
            url:"/type/findAllType.jhtml",
            success:function (result) {
                if(result.code == 200){
                    var v_typeList = result.data;
                    //构建单选按钮
                    buildTypeRadio("addFather_typeDiv",v_typeList);
                    //弹框
                    var v_cateDlg = bootbox.dialog({
                        title:'增加分类',
                        message:$("#add_cateFatherDiv form"),
                        size:"large",
                        buttons:{
                            confirm:{
                                label:'<span class="glyphicon-exclamation-ok"></span>确认',
                                className:'btn-primary',
                                callback:function () {
                                    //获取信息
                                    var v_param = {};
                                    v_param.cateName = $("#addFather_cateName",v_cateDlg).val();
                                    v_param.typeId = $("input[name='typeId']:radio:checked",v_cateDlg).val();
                                    v_param.typeName = $("input[name='typeId']:radio:checked",v_cateDlg).attr("fhName");
                                    console.log(v_param);
                                    $.post({
                                        url:"/cate/addFatherCate.jhtml",
                                        data:v_param,
                                        success:function (result) {
                                            if(result.code == 200){
                                                initTreeTable();
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
                    $("#add_cateFatherDiv").html(v_html);
                }
            }
        })
    }
    
    function sortCateList(cateList,sortList,id,level) {
        //根据当前id作为父id，从而找它下面的所有的孩子
        let childs = getChilds(cateList,id);
        for (let c of childs) {
            //给cate动态添加一个level属性
            c.level = level;
            sortList.push(c);
            sortCateList(cateList,sortList,c.id,level+1);
        }
    }
    
    function getChilds(cateList,id) {
        var childs = [];
        for (let cate of cateList) {
            if(cate.fatherId == id){
                childs.push(cate);
            }
        }
        return childs;
    }
    
</script>
</body>
</html>
