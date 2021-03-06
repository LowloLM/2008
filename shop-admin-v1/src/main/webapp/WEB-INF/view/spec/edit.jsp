<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改列表</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12" id="specInfoDiv">
            <button type="button" class="btn btn-primary" onclick="sumbit();"><span class="glyphicon glyphicon-plus"></span>提交</button>
            <table class="table table-striped table-bordered" style="width:100%">
                <tr>
                    <td>规格名:</td>
                    <td><input type="text" class="form-control" id="specName" name="specName" placeholder="请输入规格名"></td>
                    <td>规格排序:</td>
                    <td><input type="text" class="form-control" id="specNameSort" name="specNameSort" placeholder="请输入排序"></td>
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
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>

    var v_id = '${param.id}';

    //初始化函数
    $(function () {
        initSpec();
    })
    
    function initSpec() {
        $.post({
            url:"/spec/findSpec.jhtml?id="+v_id,
            success:function (result) {
                console.log(result);
                if(result.code == 200){
                    var spec = result.data.spec;
                    $("#specName").val(spec.specName);
                    $("#specNameSort").val(spec.sort);
                    var specValueList = result.data.specValueList;
                    if(specValueList.length > 0){
                        for (var i = 0; i < specValueList.length; i++) {
                            $("tbody").append($("#specValueDiv").val());
                        }
                        var specValueArr = document.getElementsByName("specValue");
                        for (var i = 0; i < specValueArr.length; i++) {
                            specValueArr[i].value = specValueList[i].specValue;
                        }
                        var specValueSortArr = document.getElementsByName("specValueSort");
                        for (var i = 0; i < specValueSortArr.length; i++) {
                            specValueSortArr[i].value = specValueList[i].sort;
                        }
                    }
                }
            }
        });
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
        $.post({
            url:"/spec/updateSpec.jhtml",
            data:{
                "id":v_id,
                "specNames":v_specNameArr[0],
                "specNameSorts":v_specNameSortArr[0],
                "specValueInfos":v_specValueInfoArr[0]
            },
            success:function (result) {
                if(result.code == 200){
                    location.href="/spec/toList.jhtml";
                }
            }
        });
    }

    function addSpecValue(obj) {
        $(obj).parents("tbody").append($("#specValueDiv").val());
    }

    function deleteSpecValue(obj) {
        $(obj).parents("tr").remove();
    }
    
</script>
</body>
</html>
