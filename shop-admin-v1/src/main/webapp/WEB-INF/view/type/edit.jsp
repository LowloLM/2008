
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改类型</title>
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
            <%--属性列表--%>
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
                            <td><input style="border: hidden;" disabled="true" type="text" class="form-control" name="attrValue" placeholder="请输入属性值"></td>
                            <td>
                                <button onclick="delAttr(this)" class="btn btn-danger">删除属性</button>
                                <button onclick="editAttr()" class="btn btn-warning">编辑属性</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: center;">
    <button type="submit" onclick="submit()" class="btn btn-primary">提交</button>
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

    var v_id = '${param.id}';

    $(function () {
        initInfo();
    })

    function addAttr() {
        $("#attrTable tbody").append($("#attrDiv").val());
    }

    function delAttr(obj) {
        $(obj).parents("tr").remove();
    }

    function submit() {
        //获取参数
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
            url:"/type/updateType.jhtml",
            data:{
                "id":v_id,
                "typeName":v_typeName,
                "attrNames":v_attrName,
                "attrValues":v_attrValue,
                "brandList":v_brand,
                "typeList":v_spec
            },
            success:function (result) {
                console.log(result);
                location.href="/type/toList.jhtml";
            }
        });
    }

    function initInfo() {
        $.post({
            url:"/type/findType.jhtml",
            data:{"id":v_id},
            success:function (result) {
                console.log(result);
                var v_type = result.data.type;
                var v_brandList = result.data.brandList;
                var v_brandIdList = result.data.brandIdList;
                var v_specList = result.data.specList;
                var v_specIdList = result.data.specIdList;
                var v_attrVoList = result.data.attrVoList;
                //回填类型信息
                $("#typeName").val(v_type.typeName);
                //渲染品牌列表
                //渲染规格列表
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
                //回填品牌列表
                selected("brandIds",v_brandIdList);
                //回填规格列表
                selected("specIds",v_specIdList);
                //动态渲染属性以及属性值
                var v_html = "";
                for (let attrVo of v_attrVoList) {
                    v_html += '<tr>\n' +
                        '    <td><input type="text" class="form-control" name="attrName" value="'+attrVo.attrName+'"></td>\n' +
                        '    <td align="center">'+attrVo.attrValues+'</td>\n' +
                        '    <td>' +
                        '        <button onclick="delAttr(this)" class="btn btn-danger">删除属性</button>' +
                        '        <input type="hidden" class="form-control" name="attrValue" value="'+attrVo.attrValues+'">' +
                        '        <button onclick="editAttr(this)" class="btn btn-warning">编辑属性</button>' +
                        '    </td>\n' +
                        '</tr>';
                }
                $("#attrTable tbody").html(v_html);
            }
        })
    }

    function editAttr(obj) {
        var v_attrValues = $(obj).parent().prev().html();
        var v_attrValueArr = v_attrValues.split(",");
        var v_html = '<div><button onclick="addAttrValue()" class="btn btn-primary">增加属性</button></div>' +
            '<table id="attrValueTable" class="table table-striped table-bordered" style="width:100%">';
        for (var attrVal of v_attrValueArr) {
            v_html += '<tr>' +
                '<td><input type="text" class="form-control" name="attrValueInfo" value="'+attrVal+'"></td>' +
                '<td><button onclick="delAttrValue(this)" class="btn btn-danger">删除属性</button></td>' +
                '</tr>'
        }
        v_html += '</table>';
        var v_attrValueDlg = bootbox.dialog({
            title:'修改属性值',
            message:v_html,
            size:"large",
            buttons:{
                confirm:{
                    label:'<span class="glyphicon-exclamation-ok"></span>确认',
                    className:'btn-primary',
                    callback:function () {
                        var v_attrValArr = [];
                        $("input[name='attrValueInfo']").each(function () {
                            v_attrValArr.push(this.value);
                        })
                        $(obj).parent().prev().html(v_attrValArr.join(","));
                        $(obj).parent().find("input[name='attrValue']").val(v_attrValArr.join(","));
                    }
                },
                cancel:{
                    label:'<span class="glyphicon-exclamation-remove"></span>取消',
                    className:'btn-danger'
                }
            }
        });
    }

    function delAttrValue(obj) {
        $(obj).parents("tr").remove();
    }
    
    function addAttrValue() {
        $("#attrValueTable").append('<tr>' +
            '<td><input type="text" class="form-control" name="attrValueInfo"></td>' +
            '<td><button onclick="delAttrValue(this)" class="btn btn-danger">删除属性</button></td>' +
            '</tr>');
    }
    
    function selected(name,idList) {
        $("input[name='"+name+"']:checkbox").each(function () {
            this.checked = isChecked(this.value,idList)
        })
    }
    
    function isChecked(id,idList) {
        for (let t of idList) {
            if(t == id){
                return true;
            }
        }
        return false;
    }

</script>
</body>
</html>
