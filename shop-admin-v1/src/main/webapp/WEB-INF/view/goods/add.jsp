<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品添加</title>
    <jsp:include page="/common/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <form class="form-horizontal" id="spuForm">
                <div class="form-group">
                    <label  class="col-sm-2 control-label">商品名:</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="spuName" placeholder="请输入商品名">
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label">价格:</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="price" placeholder="请输入价格">
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label">库存:</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="stock" placeholder="请输入库存">
                    </div>
                </div>
                <div class="form-group" id="cateDiv">
                    <label class="col-sm-2 control-label">分类:</label>

                </div>
            </form>
        </div>
    </div>
    <div style="text-align: center;">
        <span type="button" class="btn btn-primary" onclick="addSpu()">提交</span>
        <button type="reset" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span>重置</button>
    </div>
</div>
<jsp:include page="/common/xiabu.jsp"></jsp:include>
<script>
    
    $(function () {
        initCate(0);
    });
    
    function addSpu() {
        var v_param = {};
        v_param["spu.spuName"] = $("#spuName").val();
        v_param["spu.brandId"] = $("#brandSelect").val();
        v_param["spu.price"] = $("#price").val();
        v_param["spu.stock"] = $("#stock").val();
        v_param["spu.cate1"] = $($("select[name='cateSelect']")[0]).val();
        v_param["spu.cate2"] = $($("select[name='cateSelect']")[1]).val();
        v_param["spu.cate3"] = $($("select[name='cateSelect']")[2]).val();
        v_param["spu.cateName"] = $($("select[name='cateSelect']")[0]).find("option:selected").data("cate-name")+"/"+
            $($("select[name='cateSelect']")[1]).find("option:selected").data("cate-name")+"/"+
            $($("select[name='cateSelect']")[2]).find("option:selected").data("cate-name");
        v_param["spu.brandName"] = $("#brandSelect").find("option:selected").data("brand-name");
        //16:cpu型号，8：骁龙439；20：屏幕尺寸，11：6.0-6.24英寸
        //组装attrInfo
        var v_attrInfoArr = [];
        $("select[name='attrSelect']").each(function () {
            var str = $(this).data("attr-id")+":"+$(this).data("attr-name")+","+$(this).val()+":"+$(this).find("option:selected").data("attr-value");
            v_attrInfoArr.push(str);
        });
        v_param["spu.attrInfo"] = v_attrInfoArr.join(";");
        var v_specArr = [];
        //16:颜色,8:绿色，9：红色；22：内存，11:32G
        for (let specVo of v_spuSpecVoList){
            var v_id = specVo.id;
            var v_specValueArr = [];
            $("input[name='specValueCheck_"+v_id+"']:checkbox:checked").each(function () {
                var str = $(this).val()+":"+$(this).data("spec-value");
                v_specValueArr.push(str);
            });
            if(v_specValueArr.length > 0){
                //证明后面复选框是被选中的
                var v_specInfo = v_id+":"+specVo.specName+","+v_specValueArr.join(",");
                v_specArr.push(v_specInfo);
            }
        }
        v_param["spu.specInfo"] = v_specArr.join(";");
        var v_skuPriceArr = [];
        $("input[name='skuPrice']").each(function () {
            v_skuPriceArr.push(this.value);
        });
        v_param["prices"] = v_skuPriceArr.join(",");
        var v_skuStockArr = [];
        $("input[name='skuStock']").each(function () {
            v_skuStockArr.push(this.value);
        });
        v_param["stocks"] = v_skuStockArr.join(",");
        v_param["specInfos"] = v_skuArr.join(";");
        //sku图片的获取
        var v_skuImageArr = [];

        for (let v_color of v_colorArr){
            var v_colorId = v_color.colorId;
            console.log(v_colorId)
            //var arr = v_colorId.split("=");
            var arrq =$("#logo_"+v_colorId).val();
            arrq=arrq.substring(1);
            console.log("==========")
            console.log(arrq)
            console.log("========")
            v_skuImageArr.push(v_colorId+"="+arrq);
        }
        console.log(v_skuImageArr);
        v_param["skuImages"]=v_skuImageArr.join(";");
        console.log(v_param);
        $.post({
            url:"/spu/addSpu.jhtml",
            data:v_param,
            success:function (result) {
                if(result.code == 200){
                    location.href="/spu/toList.jhtml";
                }
            }
        })
    }

    function initCate(id,obj) {
        if(obj){
            $(obj).parent().nextAll().remove();
        }
        $.get({
            url:"/cate/findCateChilds.jhtml?id="+id,
            success:function (result) {
                console.log(result);
                if(result.code == 200){
                    var v_cateList = result.data;
                    if(v_cateList.length == 0){
                        //最后一级分类
                        var v_typeId = $(obj).find("option:selected").data("type-id");
                        initSpuInfo(v_typeId);
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

    var v_spuSpecVoList;
    function initSpuInfo(typeId) {
        $.get({
            url:"/spu/findSpuInfo.jhtml?typeId="+typeId,
            success:function (result) {
                console.log(result);
                if(result.code == 200){
                    var v_brandList = result.data.brandList;
                    var v_spuAttrVoList = result.data.spuAttrVoList;
                    v_spuSpecVoList = result.data.spuSpecVoList;
                    //先删除
                    $("#brandDiv").remove();
                    $("#attrDiv").remove();
                    $("#specDiv").remove();
                    $("#skuDiv").remove();
                    $("div[name='colorDiv']").remove();
                    initBrand(v_brandList);
                    initSpuAttr(v_spuAttrVoList);
                    initSpuSpec(v_spuSpecVoList);
                }
            }
        });
    }

    function initSpuSpec(v_spuSpecVoList) {
        var v_html = '<div class="form-group" id="specDiv">\n' +
            '                    <label class="col-sm-2 control-label">规格:</label>\n' +
            '                    <div class="col-sm-10"><table id="specTable" class="table table-striped table-bordered" style="width:100%">\n' +
            '                    </table></div>\n' +
            '                </div>';
        $("#spuForm").append(v_html);
        for (let v_specVo of v_spuSpecVoList){
            var trHtml = '<tr><td>'+v_specVo.specName+'</td><td>';
            let v_specValueList = v_specVo.specValueList;
            for (let v_specValue of v_specValueList) {
                trHtml += '&nbsp;&nbsp;&nbsp;<input type="checkbox" onclick="buildTable()" name="specValueCheck_'+v_specVo.id+'" value="'+v_specValue.id+'" data-spec-value="'+v_specValue.specValue+'"/>'+v_specValue.specValue;
            }
            trHtml += '</td></tr>'
            $("#specTable").append(trHtml);
        }
    }

    var v_skuArr = []; //初始化为 空数组
    var v_colorArr = [];
    function buildTable() {
        //每次在删除表格前，先把原有的数据存起来，存储的格式为：
        //{"16:黑色,27:8G,22:标准版":"12,33","16:黑色,28:16G,22:标准版":"66,44"}
        //{"sku的规格信息":"价格,库存"}
        //在每次生成表格的时候，根据唯一标识key，进行还原
        //价格数组
        var v_priceArr = [];
        $("input[name='skuPrice']").each(function () {
            v_priceArr.push(this.value);
        });
        //库存数组
        var v_stockArr = [];
        $("input[name='skuStock']").each(function () {
            v_stockArr.push(this.value);
        });
        //存储
        var v_res = {};
        var i = 0;
        //v_skuArr的格式["16:黑色,27:8G,22:标准版","16:黑色,28:16G,22:标准版"]
        for (let v_skuSpecInfo of v_skuArr) {
            v_res[v_skuSpecInfo] = v_priceArr[i]+","+v_stockArr[i];
            i++;
        }
        //删除
        $("#skuDiv").remove();
        var v_html = '<div class="form-group" id="skuDiv">\n' +
            '                    <label class="col-sm-2 control-label">SKU表格:</label>\n' +
            '                    <div class="col-sm-10"><table id="skuTable" class="table table-striped table-bordered" style="width:100%">\n' +
            '                    </table></div>\n' +
            '                </div>';
        $("#specDiv").after(v_html);
        //表格头的数组[颜色,内存]
        //表格内容的数组[["16:红色","21:黑色","22:绿色"],["33:16G","64:32G"]] 参数
        var v_headArr = [];
        var v_rowArr = [];
        var count = 0;
        v_colorArr = [];
        var v_colorDeleteArr = [];
        for (let specVo of v_spuSpecVoList){
            var v_id = specVo.id;
            var v_specValueArr = [];
            //获取当前规格下所有的被选中的复选框，并将其组装成["8:绿色","9:红色"]
            $("input[name='specValueCheck_"+v_id+"']:checkbox:checked").each(function () {
                var str = $(this).val()+":"+$(this).data("spec-value");
                v_specValueArr.push(str);
                // count等于0，则代表是颜色这组复选框，并且是被选中的
                if(count == 0){
                    v_colorArr.push({"colorId":$(this).val(),"colorName":$(this).data("spec-value")});
                }
            });

            $("input[name='specValueCheck_"+v_id+"']:checkbox").each(function () {
                // count等于0，则代表是颜色这组复选框，并且是没被选中的
                if(count == 0 && !this.checked){
                    v_colorDeleteArr.push($(this).val());
                }
            })
            count++;
            if(v_specValueArr.length > 0){
                //证明后面的复选框被选中了，潜台词就应该获取当前规格，进行组装
                v_headArr.push(specVo.specName);
                v_rowArr.push(v_specValueArr);
            }
        }
        console.log(v_headArr);
        console.log(v_rowArr);
        //表格头【列动态变】
        var v_headHtml = '<tr>';
        for (let v_head of v_headArr) {
            v_headHtml += '<td>'+v_head+'</td>';
        }
        v_headHtml += '<td>价格</td><td>库存</td>';
        v_headHtml += '</tr>';
        $("#skuTable").append(v_headHtml);
        //表格内容【行动态变】
        v_skuArr = buildData(v_rowArr);
        console.log(v_skuArr);
        if(v_skuArr){
            for (let v_sku of v_skuArr) {
                var v_trHtml = '<tr>';
                var tempArr = v_sku.split(",");
                for (let temp of tempArr) {
                    v_trHtml += '<td>'+temp.split(":")[1]+'</td>';
                }
                var v_price = "";
                var v_stock = "";
                if(v_res[v_sku]){
                    v_price = v_res[v_sku].split(",")[0];
                    v_stock = v_res[v_sku].split(",")[1];
                }
                v_trHtml += '<td><input type="text" class="form-control" name="skuPrice" value="'+v_price+'" placeholder="请输入价格"></td>';
                v_trHtml += '<td><input type="text" class="form-control" name="skuStock" value="'+v_stock+'" placeholder="请输入库存"></td>';
                v_trHtml += '</tr>';
                $("#skuTable").append(v_trHtml);
            }
        }
        //再追加
        for (let v_color of v_colorArr) {
            initImage(v_color);
        }

        //要删除的
        for(let colorId of v_colorDeleteArr){
            $("#colorDiv_"+colorId).remove();
        }

        if(!v_skuArr){
            $("#skuDiv").remove();
        }

    }

    function initImage(color) {

        var v_colorName = color.colorName;
        var v_colorId = color.colorId;
        if($("#colorDiv_"+v_colorId).size() == 0){
            //追加不同颜色的图片上传组件
            var v_html = '<div class="form-group" name="colorDiv" id="colorDiv_'+v_colorId+'">\n' +
                '                    <label  class="col-sm-2 control-label">'+v_colorName+':</label>\n' +
                '                    <div class="col-sm-8">\n' +
                '                        <input type="file" id="logoFile_'+v_colorId+'" name="imageFiles" multiple>\n' +
                '                        <input type="text" id="logo_'+v_colorId+'" name="logo">\n' +
                '                    </div>\n' +
                '                </div>';
            $("#skuDiv").after(v_html);
        }

        //配置信息
        var setting = {
            language: 'zh',
            uploadUrl: "/file/uploadImages.jhtml", // 后台上传文件的url地址
            dropZoneEnabled:false,//是否显示拖拽区域
            uploadAsync:false
        };
        //渲染组件
        $("#logoFile_"+v_colorId).fileinput(setting).on("filebatchuploadsuccess", function (event, r, previewId, index) {
            console.log(r.response.data);
            $("#logo_"+v_colorId).val(r.response.data);
        });
    }

    function buildData (arr) {
        if(arr.length > 1){
            var result = [];
            var v_base = arr[0];
            arr.splice(0,1);
            var v_next = buildData(arr);
            for(var i = 0; i < v_base.length; i++){
                for(var j = 0; j < v_next.length; j++){
                    result.push(v_base[i]+","+v_next[j]);
                }
            }
            return result;
        }else{
            return arr[0];
        }
    }

    function initBrand(brandList) {
        $("#brandDiv").remove();
        var v_html = '<div class="form-group" id="brandDiv">\n' +
            '                    <label class="col-sm-2 control-label">品牌:</label>';
        v_html += '<div class="col-sm-2"><select class="form-control" id="brandSelect"><option value="-1">===请选择===</option> ';
        for (let brand of brandList){
            v_html += '<option value="'+brand.id+'" data-brand-name="'+brand.brandName+'">'+brand.brandName+'</option>';
        }
        v_html += '</select></div></div>';
        $("#spuForm").append(v_html);
    }

    function initSpuAttr(spuAttrVoList) {
        var v_html = '<div class="form-group" id="attrDiv">\n' +
            '                    <label class="col-sm-2 control-label">分类:</label>\n' +
            '\n' +
            '                </div>';
        $("#spuForm").append(v_html);
        var count = 1;
        for (let v_attrVo of spuAttrVoList){
            var s = count>1&&count%3==1?"col-md-offset-2":"";
            var v_attrHtml = '<div class="col-sm-3 '+s+'" style="margin-top:5px;"><div class="input-group">' +
                '<span class="input-group-addon" id="basic-addon1">'+v_attrVo.attrName+'</span>' +
                '<select class="form-control" name="attrSelect" data-attr-id="'+v_attrVo.id+'" data-attr-name="'+v_attrVo.attrName+'"><option value="-1">===请选择===</option>';
            let attrValueList = v_attrVo.attrValueList;
            for (let v_attrValue of attrValueList){
                v_attrHtml += '<option value="'+v_attrValue.id+'" data-attr-value="'+v_attrValue.attrValue+'">'+v_attrValue.attrValue+'</option>';
            }
            v_attrHtml += '</select></div></div>';
            $("#attrDiv").append(v_attrHtml);
            count++;
        }
    }
    
</script>
</body>
</html>
