<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>测试导出</title>
    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
  </head>
  <body>
    <script>
        $("#add").click(function(){
            console.log("==============");
            var aa = {
                   metadataid:"2070",
                   filename:"aaa"
                };
            $.ajax({
                type:"POST",
                url:"http://192.168.1.123:8080/MT/metadataManagement/exportMetadataToExcel11",
                contentType: "application/json;charset=utf-8;",
                data:JSON.stringify(aa),
                method: "post",
            	/**重点在于这一行，设置返回类型，否则浏览器将会以奇怪的方式解析zi'ji**/
            	dataType: 'blob',
            	success: function(data){
            		alert("qq");
                	var blob = new Blob([data], {type: "application/vnd.ms-excel"});
                	saveAs(blob, "file.xlsx");
           		}
            })
        })
		
		function downloadFileByForm() {
        //console.log("ajaxDownloadSynchronized");
        var url = "http://192.168.1.123:8080/MT/metadataManagement/exportMetadataToExcel";
        var fileName = "testAjaxDownload.txt";
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        form.append($("<input></input>").attr("metadataid", 2070).attr("filename", "测试aaa"));
        form.appendTo('body').submit();
    }
    
	function downloadJSONFile() {
    	var url = 'http://localhost:8080/MT/metadataManagement/exportProtocolMetadataToJSONFile?protocolId=jojio&filename=测试';
    	window.location.href = url;
	}
    </script>
	
	<div>
    <a href="<%=request.getContextPath()%>/ajaxDownloadServlet.do?fileName=testAjaxDownload.txt">同步下载文件</a><br />
    <a href="#" onclick="downloadFilebyAjax()">ajax下载文件</a> <br />
    <a href="#" onclick="downloadFileByForm()">模拟表单提交下载文件</a><br />
    <a href="#" onclick="downloadJSONFile()">downloadJSONFile</a>
</div>

  </body>
</html>