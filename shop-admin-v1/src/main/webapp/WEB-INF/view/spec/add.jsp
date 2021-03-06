<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>增加规格</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12" id="specInfoDiv">
            <button type="button" class="btn btn-primary" onclick="addSpec();"><span class="glyphicon glyphicon-plus"></span>增加规格</button>
            <button type="button" class="btn btn-primary" onclick="sumbit();"><span class="glyphicon glyphicon-plus"></span>提交</button>
            <table class="table table-striped table-bordered" style="width:100%">
                <tr>
                    <td>规格名:</td>
                    <td><input type="text" class="form-control" name="specName" placeholder="请输入规格名"></td>
                    <td>规格排序:</td>
                    <td><input type="text" class="form-control" name="specNameSort" placeholder="请输入排序"></td>
                    <td><button type="button" class="btn btn-primary" onclick="addSpecValue(this);"><span class="glyphicon glyphicon-plus"></span>增加规格值</button></td>
                </tr>
            </table>
        </div>
    </div>
</div>
<textarea id="specValueDiv" style="display: none">
    <tr>
        <td>规格值:</td>
        <td><input type="text" class="form-control" name="specValue" placeholder="请输入规格值"></td>
        <td>规格值排序:</td>
        <td><input type="text" class="form-control" name="specValueSort" placeholder="请输入排序"></td>
        <td><button type="button" class="btn btn-danger" onclick="deleteSpecValue(this);"><span class="glyphicon glyphicon-trash"></span>删除规格值</button></td>
    </tr>
</textarea>
<textarea id="specDiv" style="display: none">
    <table class="table table-striped table-bordered" style="width:100%">
        <tr>
            <td><button type="button" class="btn btn-danger" onclick="deleteSpec(this);"><span class="glyphicon glyphicon-trash"></span>删除规格</button></td>
        </tr>
        <tr>
            <td>规格名:</td>
            <td><input type="text" class="form-control" name="specName" placeholder="请输入规格名"></td>
            <td>规格排序:</td>
            <td><input type="text" class="form-control" name="specNameSort" placeholder="请输入排序"></td>
            <td><button type="button" class="btn btn-primary" onclick="addSpecValue(this);"><span class="glyphicon glyphicon-plus"></span>增加规格值</button></td>
        </tr>
    </table>
</textarea>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    function addSpecValue(obj) {
        $(obj).parents("tbody").append($("#specValueDiv").val());
    }

    function deleteSpecValue(obj) {
        $(obj).parents("tr").remove();
    }
    
    function addSpec() {
        $("#specInfoDiv").append($("#specDiv").val());
    }

    function deleteSpec(obj) {
        $(obj).parents("table").remove();
    }
    
    function sumbit() {
        //获取规格名的集合
        var v_specNameArr = [];
        $("input[name='specName']").each(function () {
            v_specNameArr.push(this.value);
        })
        //获取规格名排序的集合
        var v_specNameSortArr = [];
        $("input[name='specNameSort']").each(function () {
            v_specNameSortArr.push(this.value);
        })
        //转换为，分割的字符串
        var v_specNames = v_specNameArr.join(",");
        var v_specNameSorts = v_specNameSortArr.join(",");
        //组装规格值信息集合 如：specValueInfos:黑色=1,红色=2;8G=3,16G=2,32G=1;15=2,30=1,50=3
        var v_specValueInfoArr = [];
        $("table").each(function () {
            var v_specValueArr = [];
            var v_specValueSortArr = [];
            //[黑色,红色]
            $(this).find("input[name='specValue']").each(function () {
                v_specValueArr.push(this.value);
            })
            //[1,2]
            $(this).find("input[name='specValueSort']").each(function () {
                v_specValueSortArr.push(this.value);
            })
            var temp = "";
            for (var i = 0; i < v_specValueArr.length; i++) {
                temp += "," + v_specValueArr[i] + "=" + v_specValueSortArr[i];
            }
            if (temp.length > 0) {
                temp = temp.substring(1);
            }
            v_specValueInfoArr.push(temp);
        })
        //组装最终规格值信息的字符串
        var v_specValueInfos = v_specValueInfoArr.join(";");
        console.log(v_specNames);
        console.log(v_specNameSorts);
        console.log(v_specValueInfos);
        $.post({
            url:"/spec/add.jhtml",
            data:{
                "specNames":v_specNames,
                "specNameSorts":v_specNameSorts,
                "specValueInfos":v_specValueInfos
            },
            success:function (result) {
                if(result.code == 200){
                    location.href="/spec/toList.jhtml";
                }
            }
        });
    }

</script>
</body>
</html>
