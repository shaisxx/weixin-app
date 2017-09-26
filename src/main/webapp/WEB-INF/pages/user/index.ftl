<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,user-scalable=0">
	<title>个人中心</title>
	<style type="text/css">
		*{margin:0; padding:0}
		table{border:1px dashed #B9B9DD;font-size:12pt}
		td{border:1px dashed #B9B9DD;word-break:break-all; word-wrap:break-word;}
	</style>
</head>

<body>
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr><td width="20%">登录账号</td><td><#if user??>${user.loginname?if_exists}</#if></td></tr>
		<tr><td width="20%">OpenID</td><td><#if user??>${user.weixinOpenId?if_exists}</#if></td></tr>
		<tr><td width="20%">昵称</td><td><#if user??>${user.weixinNickname?if_exists}</#if></td></tr>
		<tr><td width="20%">性别</td><td><#if user??>${user.weixinSex?if_exists}</#if></td></tr>
		<tr><td width="20%">国家</td><td><#if user??>${user.weixinCountry?if_exists}</#if></td></tr>
		<tr><td width="20%">省份</td><td><#if user??>${user.weixinProvince?if_exists}</#if></td></tr>
		<tr><td width="20%">城市</td><td><#if user??>${user.weixinCity?if_exists}</#if></td></tr>
		<tr><td width="20%">头像</br>地址</td><td><#if user??>${user.weixinHeadImgUrl?if_exists}</#if></td></tr>
		<tr><td width="20%">头像</td><td>
			<#if user?? && user.weixinHeadImgUrl??>
				<img src="${user.weixinHeadImgUrl?if_exists}" width="66" height="66"/>
			</#if>			
		</td></tr>
	</table>
	
	<div id="upload-container" class="col-xs-12 text-center">
		</br>上传实习照片：<input accept="image/*" type="file" id="file"/>
	</div>	
	
	<script src="${context}/localResizeIMG/lrz.bundle.js"></script>
	<script src="${context}/localResizeIMG/index.js"></script>
</body>
</html>