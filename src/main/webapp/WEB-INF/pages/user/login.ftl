<!DOCTYPE html>
<html class="ui-page-login">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>用户登录</title>
	<!--标准mui.css-->
	<link rel="stylesheet" href="${context}/mui/css/mui.min.css">
	<link href="${context}/mui/css/mui.picker.css" rel="stylesheet" />
	<link href="${context}/mui/css/mui.poppicker.css" rel="stylesheet" />	
	<!--App自定义的css-->
	<link rel="stylesheet" type="text/css" href="${context}/mui/css/app.css" />		
</head>

<body>
	<header class="mui-bar mui-bar-nav">
		<h1 class="mui-title">用户登录</h1>			
	</header>

	<div class="mui-content  mui-content-padded">
		<p class="mui-text-center"></p>
		<div class="mui-content-padded margin-bottom-0">				
			<div class="mui-row">
				<div class="mui-col-sm-12">						
					<form method="post" name="fmedit" id="fmedit" action="${context}/user/login" onsubmit="return check();" class="mui-input-group">
						<input type="hidden" name="openId" value="${openId?if_exists}"/>
						<div class="mui-input-row margin-right-15">
							<label class="font-black mui-h5">账号</label>
							<input type="text" style="margin-top:1px;" name="loginname" id="loginname" class="mui-input-clear mui-input mui-h5" placeholder="请输入账号">
						</div>
						<div class="mui-input-row margin-right-15">
							<label class="font-black mui-h5">密码</label>
							<input type="password" style="margin-top:1px;" name="password" id="password" class="mui-input-clear mui-input mui-h5" placeholder="请输入密码">
						</div>
					</form>
					<div class="margin-left-0">
						<button class="mui-btn-block mui-btn-sheetblue" onclick="javascript:$('#fmedit').submit();">登录</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="${context}/mui/js/mui.min.js"></script>
	<script type="text/javascript" src="${context}/mui/js/mui.picker.js"></script>
	<script type="text/javascript" src="${context}/mui/js/mui.poppicker.js"></script>
	<script type="text/javascript" src="${context}/js/jquery.min.js"></script>
	
	<script type="text/javascript" charset="utf-8">
		function check() {
			var loginname = jQuery("#loginname");
			if (jQuery.trim(loginname.val()) == ''){
				mui.toast("请输入账号!");
				loginname.focus();
				return false;
			}
			
			var pwd = jQuery("#password");
			if (jQuery.trim(pwd.val()) == ''){
				mui.toast("请输入密码!");
				pwd.focus();
				return false;
			}
			
			return true;
		}
	</script>
</body>
</html>