<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="/js/jquery.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="/js/bootstrap3/js/bootstrap.min.js"></script>
<script src="/js/DataTables/DataTables-1.10.20/js/jquery.dataTables.min.js"></script>
<script src="/js/DataTables/DataTables-1.10.20/js/dataTables.bootstrap.min.js"></script>
<script src="/js/bootbox/bootbox.min.js"></script>
<script src="/js/bootbox/bootbox.locales.min.js"></script>
<script src="/js/fileinput5/js/fileinput.js"></script>
<script src="/js/fileinput5/js/locales/zh.js"></script>
<script src="/js/treetable/jquery.treetable.js"></script>
<script src="/js/bootstrap-datetimepicker/js/moment-with-locales.js"></script>
<script src="/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script>
    $.ajaxSetup({
        complete:function(xhr,x){
            //console.log(xhr);
            //console.log(x);
            var responseHeader = xhr.getResponseHeader("Fh-Timeout");
            //console.log(responseHeader);
            if(responseHeader && responseHeader == "sessionTimeout"){
                //ajax请求，session超时了
                location.href="/login2.jsp";
            }
            var result = xhr.responseJSON;
            if (result.code == -1){
                bootbox.alert({
                    message: "<span class='glyphicon glyphicon-exclamation-sign'></span>o(╥﹏╥)o失败，系统异常",
                    size: 'small',
                    title: "提示信息"
                });
            }
        }
    })
</script>
