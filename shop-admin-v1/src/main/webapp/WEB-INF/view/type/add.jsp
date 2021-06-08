<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>增加类型</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <form class="form-horizontal">
                <div class="form-group">
                    <label  class="col-sm-2 control-label">类型名:</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="typeName" placeholder="请输入类型名">
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <%--品牌列表--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">品牌列表</h3>
                </div>
                <table id="brandTable" class="table table-striped table-bordered" style="width:100%">
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-md-6">
            <%--规格列表--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">规格列表</h3>
                </div>
                <table id="specTable" class="table table-striped table-bordered" style="width:100%">
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">属性列表<button onclick="addAttr()" class="btn btn-primary">增加属性</button></h3>
                </div>
                <table id="attrTable" class="table table-striped table-bordered" style="width:100%">
                    <thead>
                        <tr>
                            <th>属性名</th>
                            <th>属性值</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><input type="text" class="form-control" name="attrName" placeholder="请输入属性名"></td>
                            <td><input type="text" class="form-control" name="attrValue" placeholder="请输入属性值"></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: center;">
    <button onclick="sumbit()" class="btn btn-primary">提交</button>
    <button type="reset" class="btn btn-default">重置</button>
</div>
<textarea id="attrDiv" style="display: none">
    <tr>
        <td><input type="text" class="form-control" name="attrName" placeholder="请输入属性名"></td>
        <td><input type="text" class="form-control" name="attrValue" placeholder="请输入属性值"></td>
        <td><button onclick="delAttr(this)" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>删除</button></td>
    </tr>
</textarea>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    $(function () {
        initInfo();
    })

    function addAttr() {
        $("#attrTable tbody").append($("#attrDiv").val());
    }

    function delAttr(obj) {
        $(obj).parents("tr").remove();
    }
    
    function initInfo() {
        $.get({
            url:"/type/findInfo.jhtml",
            success:function (result) {
                console.log(result);
                var v_brandList = result.data.brandList;
                var v_specList = result.data.specList;
                var v_td_count = 5;//每行有多少条记录
                var v_tr_count = Math.ceil(v_brandList.length/v_td_count);
                var v_spec_tr_count = Math.ceil(v_specList.length/v_td_count);
                var v_brandHtml = "";
                for (let i = 0; i < v_tr_count; i++) {
                    v_brandHtml += "<tr>";
                    var v_start = i * v_td_count;//每行开始位置的下标
                    for (let j = 0; j < v_td_count; j++) {
                        if(v_brandList[v_start+j]){
                            v_brandHtml += '<td><input type="checkbox" name="brandIds" value="'+v_brandList[v_start+j].id+'"/>'+v_brandList[v_start+j].brandName+'</td>';
                        } else {
                            v_brandHtml += '<td></td>';
                        }
                    }
                    v_brandHtml += "</tr>";
                }
                $("#brandTable tbody").html(v_brandHtml);
                var v_specHtml = "";
                for (let i = 0; i < v_spec_tr_count; i++) {
                    v_specHtml += "<tr>";
                    var v_start = i * v_td_count;//每行开始位置的下标
                    for (let j = 0; j < v_td_count; j++) {
                        if(v_specList[v_start+j]){
                            v_specHtml += '<td><input type="checkbox" name="specIds" value="'+v_specList[v_start+j].id+'"/>'+v_specList[v_start+j].specName+'</td>';
                        } else {
                            v_specHtml += '<td></td>';
                        }
                    }
                    v_specHtml += "</tr>";
                }
                $("#specTable tbody").html(v_specHtml);
            }
        })
    }

    function sumbit() {
        //获取新增的参数
        var v_typeName = $("#typeName").val();
        var v_brandIds = [];
        var v_specIds = [];
        var v_attrNameArr = [];
        var v_attrValueArr = [];
        $("#brandTable input[name='brandIds']:checkbox:checked").each(function () {
            v_brandIds.push(this.value);
        });
        $("#specTable input[name='specIds']:checkbox:checked").each(function () {
            v_specIds.push(this.value);
        });
        $("#attrTable input[name='attrName']").each(function () {
            v_attrNameArr.push(this.value);
        });
        $("#attrTable input[name='attrValue']").each(function () {
            v_attrValueArr.push(this.value);
        });
        let v_brand = v_brandIds.join(",");
        let v_spec = v_specIds.join(",");
        let v_attrName = v_attrNameArr.join(",");
        let v_attrValue = v_attrValueArr.join(";");
        $.post({
            url:"/type/addType.jhtml",
            data:{
                "typeName":v_typeName,
                "brandList":v_brand,
                "typeList":v_spec,
                "attrNames":v_attrName,
                "attrValues":v_attrValue
            },
            success:function (result) {
                console.log(result);
                location.href="/type/toList.jhtml";
            }
        });
    }

</script>
</body>
</html>
