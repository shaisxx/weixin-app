package com.shtd.josh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shtd.josh.domain.WeixinUser;
import com.shtd.josh.pojo.WeixinOauth2Token;
import com.shtd.josh.pojo.WeixinUserInfo;
import com.shtd.josh.server.WeixinUserServer;
import com.shtd.josh.thread.TokenThread;
import com.shtd.josh.util.AdvancedUtil;
import com.shtd.josh.util.CommonUtils;

/**
 * 授权后的回调请求处理 Controller
 * @author Josh
 * @date 2017-09-07
 */
@Controller
@RequestMapping("/")
public class OAuthController extends BaseController{
	
	@Autowired
	private WeixinUserServer weixinUserServer;
	
	/**
	 * 授权后处理方法
	 * 处理逻辑：
	 * 1、通过code获取openid
	 * 2、拿到openid后，我们去数据库通过openid查询用户信息，如果返回有结果说明该用户已经登录过，将用户信息放置session中，跳转到登录后的页面
	 * 3、拿到openid后，如果我们去数据库没有查询到用户信息，说明该用户没有登录过，跳转到登录页面，让用户登录，登录完将openid与用户信息插入到数据库，并放置session中，跳转到登录后的页面。
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * 
	 * @author Josh
	 * @date 2017-09-07
	 */
	@RequestMapping("/oauth")
	public String oauth(Model model, 
			RedirectAttributes redirectAttributes, 
			HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("code") String code) throws ServletException, IOException{

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			
			String APPID  = TokenThread.appid;
			String SECRET = TokenThread.appsecret;
			
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(APPID, SECRET, code);

			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			
			WeixinUser weixinUser = weixinUserServer.getWeixinUserByOpenId(openId);
			if(weixinUser != null){
				
				WeixinUserInfo SessionWeixinUserInfo = (WeixinUserInfo) getSession().getAttribute(CommonUtils.SESSION_USER);
				if(SessionWeixinUserInfo == null){
					// 网页授权接口访问凭证
					//String accessToken = weixinOauth2Token.getAccessToken();
					String accessToken = TokenThread.accessToken.getToken();
					
					// 获取用户信息
					WeixinUserInfo weixinUserInfo = AdvancedUtil.getUserInfo(accessToken, openId);
					if(weixinUser != null){
						getSession().setAttribute(CommonUtils.SESSION_USER, weixinUserInfo);
					}
				}
				
				model.addAttribute("user", weixinUser);
				
				return "user/index";
				
			}else {
				redirectAttributes.addAttribute("openId", openId);
				return "redirect:user/login";
			}
		}
		return "";
	}
}
