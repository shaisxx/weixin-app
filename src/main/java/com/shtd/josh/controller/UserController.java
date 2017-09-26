package com.shtd.josh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shtd.josh.domain.WeixinUser;
import com.shtd.josh.pojo.AccessToken;
import com.shtd.josh.pojo.SignatureInfo;
import com.shtd.josh.pojo.Ticket;
import com.shtd.josh.pojo.WeixinUserInfo;
import com.shtd.josh.server.WeixinUserServer;
import com.shtd.josh.thread.TokenThread;
import com.shtd.josh.util.AdvancedUtil;
import com.shtd.josh.util.CommonUtils;
import com.shtd.josh.util.WeixinUtil;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	public final static String USER_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7872670352c05f96&redirect_uri=http://lanyan.ngrok.cc/weixin-app/oauth&response_type=code&scope=snsapi_userinfo&state=state#wechat_redirect";
	
	@Autowired
	private WeixinUserServer weixinUserServer;

	/**
	 * 用户登录
	 * @param model
	 * @param request
	 * @param loginname
	 * @param password
	 * @return
	 * @throws Exception
	 * 
	 * @author Josh
	 * @date 2017-09-07
	 */
	@RequestMapping("login")
	public String login(Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes, 
			@RequestParam(value = "openId", required = false) String openId, 
			@RequestParam(value = "loginname", required = false) String loginname, 
			@RequestParam(value = "password", required = false) String password) throws Exception {
		
		WeixinUserInfo sessionWeixinUserInfo =  (WeixinUserInfo) getSession().getAttribute(CommonUtils.SESSION_USER);
		if(sessionWeixinUserInfo == null){
			if(StringUtils.isNotEmpty(openId)){
				model.addAttribute("openId", openId);
			}else {
				return "redirect:" + USER_AUTHORIZE_URL;
			}
			
	        if (StringUtils.isEmpty(loginname) || StringUtils.isEmpty(password)) {
	        	return "user/login";
	        }
	        
	        //TODO 用户登录成功处理逻辑, 这里只判断账号密码相同即登录成功
	        if(loginname.equals(password)){
	        	
				// 获取用户信息
				WeixinUserInfo weixinUserInfo = AdvancedUtil.getUserInfo(TokenThread.accessToken.getToken(), openId);
				
				WeixinUser weixinUser = new WeixinUser();
				weixinUser.setLoginname(loginname);
				weixinUser.setWeixinOpenId(openId);
				weixinUser.setWeixinNickname(weixinUserInfo.getNickname());
				weixinUser.setWeixinSex(weixinUserInfo.getSex());
				weixinUser.setWeixinCountry(weixinUserInfo.getCountry());
				weixinUser.setWeixinProvince(weixinUserInfo.getProvince());
				weixinUser.setWeixinCity(weixinUserInfo.getCity());
				weixinUser.setWeixinLanguage(weixinUserInfo.getLanguage());
				weixinUser.setWeixinHeadImgUrl(weixinUserInfo.getHeadImgUrl());
				if(weixinUserServer.saveWeixinUser(weixinUser) > 0){
					getSession().setAttribute(CommonUtils.SESSION_USER, weixinUserInfo);
				}
	        	
				return "redirect:/user/index";
	        }else{
	        	return "user/login";
	        }
		}else {
			return "redirect:/user/index";
		}
	}
	
	/** 
	 * 个人中心 
	 * @param request 
	 * @param response 
	 * @return 
	 * @author Josh
	 * @date 2017-09-07
	 */  
	@RequestMapping("/index")
	public String gotoUserIndex(Model model, HttpServletRequest request,HttpServletResponse response){
	    //判断是否授权过，授权通过时，会保存session“WeixinUserInfo”，这样下次访问时，如果WeixinUserInfo存在，说明已经授权过，用户信息已经存在  
	    WeixinUserInfo weixinUserInfo =  (WeixinUserInfo) getSession().getAttribute(CommonUtils.SESSION_USER);
	    
	    if(weixinUserInfo == null){//没有授权过，跳转授权页面，如果你不需要授权，则scope为snsapi_base，这是不会弹出授权页面  
	        String url  = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + TokenThread.appid + "&redirect_uri=user/login&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";  
	        return "redirect:"+url;
	    }else{  
	    	
	    	WeixinUser weixinUser = weixinUserServer.getWeixinUserByOpenId(weixinUserInfo.getOpenId());
	    	model.addAttribute("user", weixinUser);
	    	
	        return "user/index";
	    }  
	}
	
	/**
	 * 地图位置
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author Josh
	 * @date 2017-09-06
	 */
	@RequestMapping("/location")
	public String location(Model model, HttpServletRequest request, HttpServletResponse response) {

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

		return "jssdk/location";
	}
}
