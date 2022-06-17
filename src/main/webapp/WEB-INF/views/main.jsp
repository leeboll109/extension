<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<script type="text/javascript">
var action = false;

$(document).ready(function(){
	extFixLists();
	$("#btnUpload").on("click", function(e){
		fileUpload();
	});
	
	$("#textAddBtn").on("click", function(e){
		addExt();			
	});
	
	$("input:checkbox[name='extCheck']").on("click", function(){
		extUse(this);
	});
	
})

function extUse(data){
	console.log(data);
	var is = $(data).is(':checked');
	if(is == true){
		var text = $(data).val();
		$.ajax({
			url : '/updateExtUse',
			data : { extName : text, extUse : "1" },
			type : "POST",
			success : function(result){
				console.log(result);
			}
		});
	} else if(is == false){
		var text = $(data).val();
		$.ajax({
			url : '/updateExtUse',
			data : { extName : text, extUse : "0" },
			type : "POST",
			success : function(result){
				console.log(result);
			}
		});
	}
}
function fileUpload(){
	var formData = new FormData();
	var inputFile = $("input[name='uploadFile']");
	var file = inputFile[0].files;
	
	if(file.length < 1){
		alert("파일을 선택하세요.");
		return;
	}
	console.log(file);
	
	formData.append("uploadFile", file[0]);
	
	$.ajax({
		url: '/upload',
		processData : false,
		contentType : false,
		data : formData,
		type : "POST",
		success : function(result){
			if(result == -2){
				alert("확장자가 200개를 넘었습니다.");
				return;	
			}
			if(result < 0){
				alert("업로드를 지원하지 않는 확장자입니다.");
				return;
			}
			location.reload();
		}
	});
}

function addExt(){
	var text = $("#textAdd").val();
	if(text.length > 20) {
		alert("20자를 넘길 수 없습니다.");
		$("#textAdd").val("");
		return;
	}
	if(text.length < 1) {
		alert("확장자를 입력하세요.");
		return;
	}
	
	if(text == "bat" || text == "cmd" || text == "com" || text == "cpl" || text == "exe" || text == "scr" || text == "js" ) {
		alert("확장자가 중복되었습니다.");
		return;
	}
	
	$.ajax({
		url : '/extName',
		data : { extName : text },
		type : "POST",
		success : function(data){
			console.log(data);
			if(data < 0){
				alert("확장자가 중복되었습니다.");
				return;
			}
			location.reload();
		}
	});
}

function extFixLists() {

	$.ajax({
		url : '/extFixList',
		type : "POST",
		success : function(data){
			console.log(data);
			data.forEach(function(el, index){
				var name = el.extName + "Check";
				$("#" + name).prop("checked", true);
			});
		}
	});
}

function extDelete(data) {
	$.ajax({
		url : '/extDelete',
		type : "POST",
		data : { extName : data },
		success : function(result){
			console.log(result);
			location.reload();
		}
	});
}

function fileDelete(data){
	$.ajax({
		url : '/fileDelete',
		type : "POST",
		data : { fileName : data },
		success : function(result){
			console.log(result);
			location.reload();
		}
	});
}
</script>
<body>
<div class="content">
	<br/>
	<h3>파일 업로드</h3>
	<table>
	  <tr>
	    <td>Select File</td>
	    <td><input type="file" name="uploadFile" /></td>
	    <td>
	      <input type="button" id="btnUpload" value="Upload"></button>
	    </td>
	  </tr>
	</table>
</div>
<br/>
<div>
	<table>
		<tr>
			<td>고정 확장자</td>
			<td style="width:80px;"><input type="checkbox" id="batCheck" name="extCheck" value="bat">bat</td>
			<td style="width:80px;"><input type="checkbox" id="cmdCheck" name="extCheck" value="cmd">cmd</td>
			<td style="width:80px;"><input type="checkbox" id="comCheck" name="extCheck" value="com">com</td>
			<td style="width:80px;"><input type="checkbox" id="cplCheck" name="extCheck" value="cpl">cpl</td>
			<td style="width:80px;"><input type="checkbox" id="exeCheck" name="extCheck" value="exe">exe</td>
			<td style="width:80px;"><input type="checkbox" id="scrCheck" name="extCheck" value="scr">scr</td>
			<td style="width:80px;"><input type="checkbox" id="jsCheck" name="extCheck" value="js">js</td>
		</tr>
		<tr>
			<td>커스텀 확장자</td>
			<td colspan="6"><input type="text" id="textAdd" placeholder="확장자 입력" style="width:100%;"></td>
			<td><input type="button" id="textAddBtn" value="+추가"></td>
		</tr>
		<tr>
			<td style="height:100px;" rowspan="${extSize + 1}">커스텀 목록</td>
			<td align="center" colspan="6">확장자</td>
			<td>삭제</td>
		</tr>
		<c:forEach var="list" items="${extList}" varStatus="idx">
			<tr>
			<td align="center" colspan="6" id="listExt${idx.count}"><c:out value="${list.extName}"></c:out></td>
			<td><input type="button" id="listBtn${idx.count}" name="listBtn" value="삭제" onclick="extDelete('${list.extName}')"></td>
			</tr>
		</c:forEach>
	</table>
</div>
<br/><br/>

<div>
	파일 리스트	
	<table>
		<tr>
			<th style="width:50px;">No.</th>
			<th style="width:200px;">파일명</th>
			<th style="width:200px;">파일크기</th>
			<th style="width:200px;">등록날짜</th>
			<th style="width:200px;">삭제</th>
		</tr>
		
		<c:forEach var="list" items="${fileList}" varStatus="idx">
		<tr>
			<td align="center"><c:out value="${idx.count}"/></td>
			<td align="center"><c:out value="${list.fileOriName}"/></td>
			<td align="center"><fmt:formatNumber value="${list.fileSize / 1024}" pattern="0.0"/> KB</td>
			<td align="center"><c:out value="${list.fileTime}"/></td>
			<td align="center"><input type="button" id="listBtn${idx.count}" name="fileListBtn" value="삭제" onclick="fileDelete('${list.fileName}')"></td>
		</tr>
		</c:forEach>
	</table>

</div>
</body>
</html>