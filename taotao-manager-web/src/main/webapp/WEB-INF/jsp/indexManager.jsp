<%--
  Created by IntelliJ IDEA.
  User: cfbber
  Date: 2019/1/25
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="importAll()">导入商品</a>
</div>

<script type="text/javascript">
    function importAll() {
        $.get("/index/importAll", function (data) {
            if (data.status == 200){
                alert("success");
            }else {
                alert("failed");
            }


        });
    }
</script>