package com.shtd.josh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtd.josh.menu.Button;
import com.shtd.josh.menu.ClickButton;
import com.shtd.josh.menu.ComplexButton;
import com.shtd.josh.menu.Menu;
import com.shtd.josh.menu.ViewButton;
import com.shtd.josh.pojo.Token;

/**
 * 菜单管理器类
 * 
 * @author liufeng
 * @date 2013-10-17
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	/**
	 * 定义菜单结构
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		ViewButton btn11 = new ViewButton();
		btn11.setName("授权登录");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7872670352c05f96&redirect_uri=http://lanyan.ngrok.cc/weixin-app/oauth&response_type=code&scope=snsapi_userinfo&state=state#wechat_redirect");
		
		ClickButton btn12 = new ClickButton();  
        btn12.setName("发送位置");  
        btn12.setType("location_select");  
        btn12.setKey("rselfmenu_3_2");
        
//		ViewButton btn21 = new ViewButton();
//		btn21.setName("资源库系统");
//		btn21.setType("view");
//		btn21.setUrl("http://192.168.1.225/scms2");
		
		ViewButton btn21 = new ViewButton();
		btn21.setName("资源库系统");
		btn21.setType("view");
//		btn21.setUrl("http://lanyan.ngrok.cc/weixin-app/core");
		btn21.setUrl("http://192.168.1.225/scms2");

		ViewButton btn31 = new ViewButton();
		btn31.setName("JSSDK-DEMO");
		btn31.setType("view");
		btn31.setUrl("http://lanyan.ngrok.cc/weixin-app/jssdk/demo");

//		ViewButton btn32 = new ViewButton();
//		btn32.setName("微信授权");
//		btn32.setType("view");
//		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7872670352c05f96&redirect_uri=http://lanyan.ngrok.cc/weixin-app/oauthServlet&response_type=code&scope=snsapi_userinfo&state=state#wechat_redirect");
		
		ViewButton btn33 = new ViewButton();
		btn33.setName("地图位置");
		btn33.setType("view");
		btn33.setUrl("http://lanyan.ngrok.cc/weixin-app/user/location");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("个人中心");
		mainBtn1.setSub_button(new Button[] { btn11, btn12});

//		ComplexButton mainBtn2 = new ComplexButton();
//		mainBtn2.setName("资源库系统");
//		mainBtn2.setSub_button(new Button[] { btn21});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("功能");
		mainBtn3.setSub_button(new Button[] {btn31, btn33});

		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, btn21, mainBtn3 });
		
		return menu;
	}

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId 		= "wx7872670352c05f96";
		// 第三方用户唯一凭证密钥
		String appSecret 	= "f639a5eaae6dfce60f129a31b2868c1b";

		// 调用接口获取凭证
		Token token = CommonUtil.getToken(appId, appSecret);

		if (null != token) {
			// 创建菜单
			boolean result = MenuUtil.createMenu(getMenu(), token.getAccessToken());

			// 判断菜单创建结果
			if (result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败！");
		}
	}
}
