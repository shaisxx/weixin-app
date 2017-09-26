package com.shtd.josh.util;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {
	
	public final static String SESSION_USER = "WeixinUserInfo";
	
	public static String getUrlByRequest(HttpServletRequest request){
		
		String backUrl = "";
		
		String param = request.getQueryString();// 获取请求参数
		String url = request.getServletPath();// 获取请求路径（不带参数）
		if (param != null) {
			url = url + "?" + param;// 组合成完整请求URL
		}
		String projectnameP = request.getContextPath();
		String projectName = projectnameP.substring(projectnameP.lastIndexOf('/') + 1, projectnameP.length()); // 获取工程名,如stms_app
		if (!"".equals(projectName)) {
			projectName = "/" + projectName;
		}
		String port = String.valueOf(request.getServerPort());// 获取端口号
		if (!"80".equals(port)) {// 不是80端口时需加端口号
			port = ":" + port;
		} else {
			port = "";
		}

	    backUrl = "http://" + request.getServerName() + port + projectName + url;//完整的请求路径http://192.168.1.141/weixin-app/+路径  
	    
	    return backUrl;
	}
	
	public static String getWebsiteAndProject(HttpServletRequest request){
		
		String projectnameP = request.getContextPath();
		String projectName = projectnameP.substring(projectnameP.lastIndexOf('/') + 1, projectnameP.length());
		if (!"".equals(projectName)) {
			projectName = "/" + projectName;
		}
		String port = String.valueOf(request.getServerPort());// 获取端口号
		if (!"80".equals(port)) {// 不是80端口时需加端口号
			port = ":" + port;
		} else {
			port = "";
		}

	    return "http://" + request.getServerName() + port + projectName;
	}
}
