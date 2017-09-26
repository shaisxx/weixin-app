package com.shtd.josh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shtd.josh.pojo.AccessToken;
import com.shtd.josh.pojo.SignatureInfo;
import com.shtd.josh.pojo.Ticket;
import com.shtd.josh.thread.TokenThread;
import com.shtd.josh.util.CommonUtils;
import com.shtd.josh.util.WeixinUtil;

@Controller
@RequestMapping("/jssdk")
public class JsSdkTestController extends BaseController {

	/**
	 * JS-SDK Demo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author Josh
	 * @date 2017-09-06
	 */
	@RequestMapping("/demo")
	public String demo(Model model, HttpServletRequest request, HttpServletResponse response) {

	    String strBackUrl = CommonUtils.getUrlByRequest(request);
	    AccessToken token = null;
	    if(TokenThread.accessToken==null || TokenThread.accessToken.getToken()==""){//token失效，重新获取，获取方法参考第二篇博文，在这由于篇幅问题暂不列出  
	        token = WeixinUtil.getAccessToken(TokenThread.appid, TokenThread.appsecret);
	    }else{  
	        token = TokenThread.accessToken;  
	    }  
	    Ticket ticket = null;  
	    if(TokenThread.ticket ==null || TokenThread.ticket.getTicket()==""){  
	        ticket = WeixinUtil.getTicket(token.getToken());//获取ticket  
	    }else{  
	        ticket = TokenThread.ticket;
	    }  
	    SignatureInfo siInfo = new SignatureInfo();  
	    siInfo.setNoncestr(RandomStringUtils.randomAlphanumeric(20));//随机字符串  
	    siInfo.setTimestamp(String.valueOf(System.currentTimeMillis()));//随机时间截  
	    siInfo.setUrl(strBackUrl);  
	    siInfo = WeixinUtil.getSignature(siInfo, ticket);  
	    request.setAttribute("siInfo",siInfo);  

		return "jssdk/demo";
	}
}