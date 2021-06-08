<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改页面</title>
    <jsp:include page="/common/head.jsp"/>
</head>
<body>
<jsp:include page="/common/nav.jsp"/>
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
                    <label  class="col-sm-2 control-label">分类:</label>
                    <div class="col-sm-4" id="cateDivInfo">
                    </div>
                </div>

            </form>
        </div>
    </div>
    <div style="text-align: center;">
        <button type="button" class="btn btn-primary" onclick="submit(v_id)">提交</button>
        <button type="reset" class="btn btn-default">重置</button>
    </div>
</div>

<jsp:include page="/common/xiabu.jsp"/>
<script>
    $(function () {
        initInfo();
    })
    var v_id = '${param.id}';
    var v_spu;
    var v_skuList = [];
    var v_skuImageVoList = [];
    function initInfo(){
        $.ajax({
            type:"get",
            url:"/spu/findSpu.jhtml?spuId="+v_id,
            success:function (result) {
                if(result.code == 200){
                console.log(result.data);
                v_spu = result.data.spu;
                var v_typeId = result.data.typeId;
                v_skuList = result.data.skuList;
                v_skuImageVoList = result.data.skuImageVoList;
                $("#spuName").val(v_spu.spuName);
                $("#price").val(v_spu.price);
                $("#stock").val(v_spu.stock);
                $("#cateDivInfo").html(v_spu.cateName+'<button type="button" class="btn btn-warning" onclick="editCate()"><span class="glyphicon glyphicon-pencil">编辑</button>');
                initSpuInfo(v_typeId , 1);
                }
            }
        })
    }
    function submit(id){
        //spu相关的信息
        var v_param={};
        v_param["spu.id"] = id;
        v_param["spu.spuName"] = $("#spuName").val();
        v_param["spu.brandId"] = $("#brandSelect").val();
        v_param["spu.brandName"] = $("#brandSelect").find("option:selected").data("brand-name");
        v_param["spu.price"] = $("#price").val();
        v_param["spu.stock"] = $("#stock").val();
        if(flag == 0){
            v_param["spu.cate1"] =v_spu.cate1;
            v_param["spu.cate2"] =v_spu.cate2;
            v_param["spu.cate3"] =v_spu.cate3;
            v_param["spu.cateName"] =v_spu.cateName;

        }else if(flag == 1){
            v_param["spu.cate1"] = $($("select[name='cateSelect']")[0]).val();
            v_param["spu.cate2"] = $($("select[name='cateSelect']")[1]).val();
            v_param["spu.cate3"] = $($("select[name='cateSelect']")[2]).val();
            v_param["spu.cateName"] = $($("select[name='cateSelect']")[0]).find("option:selected").data("cate-name")+"-"+
                $($("select[name='cateSelect']")[1]).find("option:selected").data("cate-name")+"-"+
                $($("select[name='cateSelect']")[2]).find("option:selected").data("cate-name");
        }


        //16:型号,8:晓龙439;20:屏幕尺寸,11:6.0-6.24英寸;
        //组装
        var v_attrInfoArr=[];
        $("select[name='attrSelect']").each(function () {
            v_attrInfoArr.push($(this).data("attr-id")+":"+$(this).data("attr-name")+","+$(this).val()+":"+$(this).find("option:selected").data("attr-value"));
        });
        v_param["spu.attrInfo"] = v_attrInfoArr.join(";");
        var v_specArr = [];
        //16:颜色,8:绿色,9:红色;22:内存,11:32G;
        for(let v_specVo  of v_spuSpecVoList){
            var v_id = v_specVo.id;
            var v_specValueArr=[];
            //获取当前规格下所有的被选中的复选框，并将其组装成 ["8:绿色","9:红色"]
            $("input[name='specValueCheck_"+v_id+"']:checkbox:checked").each(function () {
                v_specValueArr.push($(this).val()+":"+$(this).data("spec-value"));
            });
            if(v_specValueArr.length>0){
                //证明后面的复习框被选中了; 潜台词就应该获取当前规格,进行组装
                var v_specInfo = v_id+":"+v_specVo.specName+","+v_specValueArr.join(",");
                v_specArr.push(v_specInfo);
            }
        }
        v_param["spu.specInfo"] = v_specArr.join(";");

        var v_skuPriceArr=[];
        $("input[name='skuPrice']").each(function () {
            v_skuPriceArr.push(this.value);
        });
        v_param["prices"]=v_skuPriceArr.join(",");

        var v_skuStockArr=[];
        $("input[name='skuStock']").each(function () {
            v_skuStockArr.push(this.value);
        });
        v_param["stocks"]=v_skuStockArr.join(",");
        //["16:黑色,27:8G,22:标准版":"13,22","16:黑色,28:16G,22:标准版":"66,44"]
        //"16:黑色,27:8G,22:标准版"
        // v_param["specInfos"]=v_skuArr.join(";");
        //如果v_skuArr中的某个元素和v_skuList中的某个v_sku的specInfo相等,则该v_skuArr中的某个元素钱应该加上skuId_xxx；
        //如果v_skuArr中的某个元素和v_skuList中的某个v_sku的specInfo都不相等,则该v_skuArr中的某个元素钱应该加上-1_xxx；
        var res = [];
        for(let v_sku of v_skuArr){
            res.push(buildSku(v_sku));
        }
        v_param["specInfos"]=res.join(";");
        //sku图片的获取
        var v_skuImageArr = [];

        for (let v_color of v_colorArr){
            var v_colorId = v_color.colorId;
            v_skuImageArr.push(v_colorId+"="+$("#logo_"+v_colorId).val().substring(1));
        }
        console.log(v_skuImageArr);
        v_param["skuImages"]=v_skuImageArr.join(";");

        console.log(v_colorArr
        );
        console.log(v_param);
        $.post({
            url:"/spu/updateSpu.jhtml",
            data:v_param,
            success:function (reslut) {
               // alert("0.0");
                if(reslut.code == 200){
                    location.href="/spu/toList.jhtml";
                }
            }
        });
    }

    function buildSku(v_sku) {
        for(let v_skuDB of v_skuList){
            if(v_skuDB.specInfo ==v_sku){
                return v_skuDB.id+"_"+v_sku;
            }
        }
        return "-1_"+v_sku;
    }

    var v_spuSpecVoList;
    function initSpuInfo(v_typeId , flag) {
        $.get({
            url:"/spu/findSpuInfo.jhtml?typeId="+v_typeId,
            success:function (result) {
                console.log(result);
                if(result.code==200)
                var v_brandList = result.data.brandList;
                var v_spuAttrVoList=result.data.spuAttrVoList;
                v_spuSpecVoList=result.data.spuSpecVoList;
                //先删除
                $("#brandDiv").remove();
                $("#attrDiv").remove();
                $("#specDiv").remove();
                $("#skuDiv").remove();
                $("div[name='colorDiv']").remove();
                initBrand(v_brandList);
                initAttr(v_spuAttrVoList);
                initSpec(v_spuSpecVoList);
                //回填
                if(flag == 1){
                    selectInfo();
                }
            }
        });
    }
    function selectInfo(){
        //回填品牌
        $("#brandSelect").val(v_spu.brandId);
        //回填属性
        //108:机身存储,218:8G;109:处理器,222:I6;110:颜色,225:天空蓝;111:尺寸,227:5.8英寸
        var v_attrArr = v_spu.attrInfo.split(";");
        var count = 0;
        $("select[name='attrSelect']").each(function () {
            $(this).val(v_attrArr[count].split(",")[1].split(":")[0]);
            count++;
        });
        //回填规格
        //20:颜色,30:红色;32:机身储存,46:4G+128G;21:套装,52:原厂套装
        var v_specInfoArr = v_spu.specInfo.split(";");
        var v_headArr = [];
        for(let v_specInfo of v_specInfoArr){
            var v_tempArr = v_specInfo.split(",");
            var v_specId = v_tempArr[0].split(":")[0];
            v_headArr.push(v_tempArr[0].split(":")[1]);
            var v_specValueIdArr = [];
            for (let i = 1; i < v_tempArr.length; i++) {
                v_specValueIdArr.push(v_tempArr[i].split(":")[0]);
            };
            $("input[name='specValueCheck_"+v_specId+"']:checkbox").each(function () {
                for (let v_specValueId of v_specValueIdArr) {
                    if(this.value == v_specValueId){
                        this.checked = true;
                        break;
                    }
                }
            });
        }
        //回填sku
        var v_html = '<div class="form-group" id="skuDiv">\n' +
            '                    <label  class="col-sm-2 control-label">SKU:</label>\n' +
            '                    <div class="col-sm-10">\n' +
            '                        <table id="skuTable" class="table table-striped table-bordered" style="width:100%">\n' +
            '                        </table>\n' +
            '                    </div>\n' +
            '                </div>';
        $("#specDiv").after(v_html);
        //生成表格头
        var v_headHtml = '<tr>';

        for (let v_head of v_headArr) {
            v_headHtml += '<td>' + v_head + '</td>';
        }
        v_headHtml += '<td>价格</td><td>库存</td>';
        v_headHtml += '</tr>';
        $("#skuTable").append(v_headHtml);
        //生成表格内容
        for (let v_sku of v_skuList){
            var v_trHtml = '<tr>';
            var tempArr = v_sku.specInfo.split(",");
            //向v_skuArr里面添加数据 v_skuArr的格式 ["16:黑色,27:8G,22:标准版","16:黑色,28:16G,22:标准版"]
            v_skuArr.push(v_sku.specInfo);
            for (let temp of tempArr) {
                v_trHtml += ' <td>' + temp.split(":")[1] + '</td>';
            }
            // var v_price="";
            // var v_stock="";
            // if(v_res[v_sku]){
            //     v_price = v_res[v_sku].split(",")[0];
            //     v_stock = v_res[v_sku].split(",")[1];
            // }
            v_trHtml += ' <td><input type="text" class="form-control" name="skuPrice" value="'+v_sku.price+'" placeholder="请输入价格"></td>';
            v_trHtml += ' <td><input type="text" class="form-control" name="skuStock" value="'+v_sku.stock+'" placeholder="请输入库存"></td>';
            v_trHtml += '</tr>';
            $("#skuTable").append(v_trHtml);
        }
        //回填image
        // v_color 格式[{"colorId":"12","colorName":"黑色"},{"colorId":"20","colorName":"红色"}]
        v_colorArr = [];
        for (let v_skuImageVo of v_skuImageVoList) {
            v_colorArr.push({["colorId"]:v_skuImageVo.colorId,"colorName":v_skuImageVo.colorName});
            initImage({["colorId"]:v_skuImageVo.colorId,"colorName":v_skuImageVo.colorName},v_skuImageVo.skuImageList);
        }
        //再追加
        // for (let v_color of v_colorArr){
        //     initImage(v_color,v_skuImageVo.);
        // }
    }
    var v_colorArr = [];
    var v_skuArr = [];//初始化为 空数组
    var v_allColorId = [];
    function buildTable() {
        //每次在删除表格前,先把原有的数据存起来,存的格式为:
        //{"16:黑色,27:8G,22:标准版":"13,22","16:黑色,28:16G,22:标准版":"66,44"}
        //{"sku的规格信息":"规格,库存"}
        //价格数组
        var v_priceArr = [];
        $("input[name='skuPrice']").each(function () {
            v_priceArr.push(this.value);
        })
        //库存数组
        var v_stockArr = [];
        $("input[name='skuStock']").each(function () {
            v_stockArr.push(this.value);
        })
        // 储存
        var v_res = {};
        var i = 0;
        for(let v_skuSpecInfo of v_skuArr){
            v_res[v_skuSpecInfo]= v_priceArr[i]+","+v_stockArr[i];
            i++;
        }
        console.log("==============");
        console.log(v_res);
        console.log("==============");
        //删除
        $("#skuDiv").remove();
        var v_html = '<div class="form-group" id="skuDiv">\n' +
            '                    <label  class="col-sm-2 control-label">SKU:</label>\n' +
            '                    <div class="col-sm-10">\n' +
            '                        <table id="skuTable" class="table table-striped table-bordered" style="width:100%">\n' +
            '                        </table>\n' +
            '                    </div>\n' +
            '                </div>';
        $("#specDiv").after(v_html);
        //表格头
        var v_headArr = [];
        var v_rowArr = [];
        var count = 0;
        v_colorArr = [];
        var v_colorDeleteArr = [];
        for (let v_specVo  of v_spuSpecVoList) {
            var v_id = v_specVo.id;
            var v_specValueArr = [];
            //获取当前规格下所有的被选中的复选框，并将其组装成 ["8:绿色","9:红色"]
            $("input[name='specValueCheck_" + v_id + "']:checkbox:checked").each(function () {
                v_specValueArr.push($(this).val() + ":" + $(this).data("spec-value"));
                //count等于0,则代表是颜色这组复选框,并且是被选中的
                if (count == 0) {
                    // v_color 格式[{"colorId":"12","colorName":"黑色"},{"colorId":"20","colorName":"红色"}]
                    v_colorArr.push({"colorId": $(this).val(), "colorName": $(this).data("spec-value")});
                }
            });
            $("input[name='specValueCheck_" + v_id + "']:checkbox").each(function () {
                //v_specValueArr.push($(this).val()+":"+$(this).data("spec-value"));
                //count等于0,则代表是颜色这组复选框,并且是被选中的
                if (count == 0 && !this.checked) {
                    v_colorDeleteArr.push($(this).val());
                }
            });
            //一定要特别注意
            count++;

            if (v_specValueArr.length > 0) {
                //证明后面的复习框被选中了; 潜台词就应该获取当前规格,进行组装
                v_headArr.push(v_specVo.specName);
                v_rowArr.push(v_specValueArr);
            }
        }
        var v_hendHtml = '<tr>';

        for (let v_head of v_headArr) {
            v_hendHtml += '<td>' + v_head + '</td>';
        }
        v_hendHtml += '<td>价格</td><td>库存</td>';
        v_hendHtml += '</tr>';
        $("#skuTable").append(v_hendHtml);
        //表格内容
        v_skuArr = buildData(v_rowArr);
        console.log(v_skuArr);
        if(v_skuArr) {
            for (let v_sku of v_skuArr) {
                var v_trHtml = '<tr>';
                var tempArr = v_sku.split(",");
                for (let temp of tempArr) {
                    v_trHtml += ' <td>' + temp.split(":")[1] + '</td>';
                }

                var v_price="";
                var v_stock="";
                if(v_res[v_sku]){
                    v_price = v_res[v_sku].split(",")[0];
                    v_stock = v_res[v_sku].split(",")[1];
                }
                v_trHtml += ' <td><input type="text" class="form-control" name="skuPrice" value="'+v_price+'" placeholder="请输入价格"></td>';
                v_trHtml += ' <td><input type="text" class="form-control" name="skuStock" value="'+v_stock+'" placeholder="请输入库存"></td>';
                v_trHtml += '</tr>';
                $("#skuTable").append(v_trHtml);
            }
        }
        //先删除
        // $("div[name='colorDiv']").remove();
        //再追加
        for (let v_color of v_colorArr){
            initImage(v_color);
        }
        if(!v_skuArr){
            $("#skuDiv").remove();
        }
        //要删除的
        for(let colorId of v_colorDeleteArr){
            $("#colorDiv_"+colorId).remove();
        }
    }
    function initImage(color,skuImages) {
        var v_colorName = color.colorName;
        var v_colorId = color.colorId;
        //如果颜色不存在则追加到页面
        if($("#colorDiv_"+v_colorId).size()==0){
            //追加不同的颜色的图片上传组件
            var v_html = '<div class="form-group" name="colorDiv" id="colorDiv_'+v_colorId+'">\n' +
                '                    <label  class="col-sm-2 control-label">'+v_colorName+':</label>\n' +
                '                    <div class="col-sm-8">\n' +
                '                        <input type="file" id="logoFile_'+v_colorId+'" name="imageFiles" multiple>\n' +
                '                        <input type="text" id="logo_'+v_colorId+'" name="logo">\n' +
                '                    </div>\n' +
                '                </div>';
            //$("#spuForm").append(v_html);
            $("#skuDiv").after(v_html);
            var imageArr = [];
            //[{"url"："后台的删除路径","key":"传递的参数"},{"url"："后台的删除路径","key":"传递的参数"}]
            var imageDeleteArr = [];

            //如果skuImages不为空
            if(skuImages){
                for(let v_skuImage of skuImages){
                    imageArr.push(v_skuImage.image);
                    imageDeleteArr.push({"url":"/spu/deleteSkuImage.jhtml","key":v_skuImage.id})
                }
                //回填图片路径的数据 ,a,b,c
                $("#logo_"+v_colorId).val(","+imageArr.join(","));
            }
                console.log(imageDeleteArr)
            // 配置信息
            var setting = {
                language: 'zh',
                uploadUrl: "/file/uploadImages.jhtml", // 后台上传文件的url地址
                //showUpload : false,
                //showRemove : false,
                initialPreview:imageArr,
                initialPreviewAsData:true,
                dropZoneEnabled:false,//是否显示拖拽区域
                uploadAsync:false,
                initialPreviewConfig:imageDeleteArr,
                overwriteInitial:false, //不覆盖已存在的图片
            };
            // 渲染组件
            let fileinput = $("#logoFile_"+v_colorId).fileinput(setting);
            fileinput.on("filebatchuploadsuccess", function (event, r, previewId, index) {
                console.log(r.response.data);
                $("#logo_"+v_colorId).val($("#logo_"+v_colorId).val()+r.response.data);
            });
            //$("#logoFile_"+v_colorId).fileinput('_initFileActions');//这行代码就是多张照片的单张删除照片的核心代码
            fileinput.on("filepredelete",function (event, r, jqXHR, data) {
                    //如果用户没有点击“是”【那就是点击了否】,取消删除操作
                    if(!confirm("您确定要删除吗？")){
                        return false;
                    }
            });
            fileinput.on("filedeleted",function (event, r, jqXHR, data) {
                    //这张图片在隐藏域中对应的路径也应该删除了
                let request = jqXHR.responseJSON;
                if(request.code==200){
                    //删除
                    var res = $("#logo_"+v_colorId).val().replace(","+request.data,"")
                    $("#logo_"+v_colorId).val(res);
                }
                    //如何获取服务端响应给客户端的数据

            });
        }
    }
    function buildData(arr){
        if (arr.length > 1) {
            var result=[];
            var v_a=arr[0];
            arr.splice(0,1);
            var v_next = buildData(arr);
            for (var i = 0; i < v_a.length; i++) {
                for (var j = 0; j < v_next.length; j++) {
                    result.push(v_a[i]+","+v_next[j]);
                }
            }
            return result;
        }else{
            return arr[0];
        }
    }
    var flag = 0;
    function editCate() {
        //先删
        $("#cateDivInfo").remove();
        //再追加
        initCate(0);
        flag = 1;
    }
    function initCate(id,obj){
        if(obj){
            $(obj).parent().nextAll().remove();
        }
        $.get({
            url:"/cate/findCateChilds.jhtml?id="+id,
            success:function (result) {
                console.log(result);
                if(result.code==200) {
                    var v_cateList = result.data;
                    if(v_cateList.length == 0){
                        //最后一级的分类
                        var v_typeId = $(obj).find("option:checked").data("type-id");
                        initSpuInfo(v_typeId);
                        return ;
                    }
                    var v_html = '<div class="col-sm-2"> <select class="form-control" name="cateSelect" onchange="initCate(this.value,this)"><option value="-1">===请选择===</option>';
                    for (let v_cate of v_cateList) {
                        v_html+='<option value="'+v_cate.id+'" data-type-id="'+v_cate.typeId+'" data-cate-name="'+v_cate.cateName+'">'+v_cate.cateName+'</option>';
                    }
                    v_html += '</select></div>';
                    $("#cateDiv").append(v_html);
                }
            }
        })
    }
    function initSpec(v_spuSpecVoList){
        var v_html=' <div class="form-group" id="specDiv">\n' +
            '                    <label  class="col-sm-2 control-label">规格:</label>\n' +
            '\n' +
            '                    <div class="col-sm-8"><table id="specTable" class="table table-striped table-bordered" style="width:100%">\n' +
            '                    </table></div>\n' +
            '                </div>';
        $("#spuForm").append(v_html);
        for (let v_specVo of v_spuSpecVoList){
            var v_specHtml = '<tr><td>'+v_specVo.specName+'</td><td>';
            let specValueList = v_specVo.specValueList;
            for(let v_specValue of specValueList){
                v_specHtml+='&nbsp;&nbsp;&nbsp;<input type="checkbox" onclick="buildTable()" name="specValueCheck_'+v_specVo.id+'" value="'+v_specValue.id+'" data-spec-value="'+v_specValue.specValue+'"/>'+v_specValue.specValue;
            }
            v_specHtml += '</td></tr>';
            $("#specTable").append(v_specHtml);
        }
    }
    function initBrand(v_brandList){
        $("#brandDiv").remove();
        var v_html = '<div class="form-group" id="brandDiv">\n' +
            '                    <label  class="col-sm-2 control-label">品牌:</label>';
        v_html+='<div class="col-sm-2"><select class="form-control" id="brandSelect"><option value="-1">===请选择===</option>';
        for(let brand of v_brandList){
            v_html+='<option value="'+brand.id+'" data-brand-name="'+brand.brandName+'">'+brand.brandName+'</option>';
        }
        v_html+='</select></div></div>';
        $("#spuForm").append(v_html);
    }
    function initAttr(v_spuAttrVoList){
        var v_html = '  <div class="form-group" id="attrDiv">\n' +
            '                    <label  class="col-sm-2 control-label">属性:</label>\n' +
            '\n' +
            '\n' +
            '                </div>';
        $("#spuForm").append(v_html);
        var count = 1;
        for(let v_attrVo of v_spuAttrVoList){
            var s = count>1&&count%3 == 1?"col-md-offset-2":"";
            var v_html1 ='<div class="col-sm-3 '+s+'" style="margin-top:5px"><div class="input-group">' +
                '<span class="input-group-addon" id="basic-addon1">'+v_attrVo.attrName+'</span>' +
                '<select class="form-control" name="attrSelect" data-attr-id="'+v_attrVo.id+'" data-attr-name="'+v_attrVo.attrName+'"><option value="-1">===请选择===</option>';

            let attrValueList = v_attrVo.attrValueList;
            for(let v_attrValue of attrValueList){
                v_html1 += '<option value="'+v_attrValue.id+'" data-attr-value="'+v_attrValue.attrValue+'">'+v_attrValue.attrValue+'</option>';
            }
            v_html1 += '</select></div></div>';
            $("#attrDiv").append(v_html1);
            count++;
        }
    }
</script>
</body>
</html>
