package com.shtd.josh.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtd.josh.pojo.AccessToken;
import com.shtd.josh.pojo.OpenIdResult;
import com.shtd.josh.pojo.SignatureInfo;
import com.shtd.josh.pojo.Ticket;
import com.shtd.josh.pojo.WeixinUserInfo;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class WeixinUtil {

	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public final static String getOpen_id_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";  
    
    public final static String getNewAccess_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	/**
	 * 获取access_token
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
    /** 
     * 检验授权凭证（access_token）是否有效 
     * @param accessToken 凭证 
     * @param openid id 
     * @return 
     */  
    public static int checkAccessToken(String accessToken, String openid) {  
        String requestUrl = "https://api.weixin.qq.com/sns/auth?access_token="+accessToken+"&openid="+openid;  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        int result = 1;  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                result = jsonObject.getInt("errcode");  
            } catch (JSONException e) {  
                accessToken = null;  
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return result;  
    }  
    
    /** 
     * 用户授权，使用refresh_token刷新access_token 
     * @return 
     */  
    public static OpenIdResult getNewAccess_Token(OpenIdResult open, String refresh_token, String openId) {  
        String requestUrl = getNewAccess_token.replace("REFRESH_TOKEN", refresh_token).replace("APPID", openId);  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                open.setAccess_token(jsonObject.getString("access_token"));  
            } catch (JSONException e) {  
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return open;  
    } 
    
    /** 
     * 获得用户基本信息 
     * @param request  
     * @param code 
     * @param appid 
     * @param appsecret 
     * @return 
     */  
    public static OpenIdResult getOpenId(HttpServletRequest request, String code,String appid, String appsecret) {  
        String requestURI = request.getRequestURI();  
        String param = request.getQueryString();  
        if(param!=null){  
            requestURI = requestURI+"?"+param;  
        }  
        String url = getOpen_id_url.replace("APPID",appid).replace("SECRET",appsecret).replace("CODE",code);  
        JSONObject jsonObject = httpRequest(url, "POST", null);  
        OpenIdResult result = new OpenIdResult();  
        if (null != jsonObject) {  
            Object obj = jsonObject.get("errcode");  
            if (obj == null) {  
                result.setAccess_token(jsonObject.getString("access_token"));  
                result.setExpires_in(jsonObject.getString("expires_in"));  
                result.setOpenid(jsonObject.getString("openid"));  
                result.setRefresh_token(jsonObject.getString("refresh_token"));  
                result.setScope(jsonObject.getString("scope"));  
            }else{  
                System.out.println("获取openId回执："+jsonObject.toString()+"访问路径："+requestURI);  
                log.error("访问路径:"+requestURI);  
                log.error("获取openId失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return result;  
    }  
    
    /** 
     * 通过网页授权获取用户信息 
     * @param accessToken 网页授权接口调用凭证 
     * @param openId 用户标识 
     * @return WeixinUserInfo 
     */  
	public static WeixinUserInfo getWeixinUserInfo(String accessToken, String openId) {  
        WeixinUserInfo user = null;  
        // 拼接请求地址  
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";  
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);  
        // 通过网页授权获取用户信息  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        if (null != jsonObject) {  
            try {  
                user = new WeixinUserInfo();
                // 用户的标识  
                user.setOpenId(jsonObject.getString("openid"));  
                // 昵称  
                user.setNickname(jsonObject.getString("nickname"));  
                // 性别（1是男性，2是女性，0是未知）  
                user.setSex(jsonObject.getInt("sex"));  
                // 用户所在国家  
                user.setCountry(jsonObject.getString("country"));  
                // 用户所在省份  
                user.setProvince(jsonObject.getString("province"));  
                // 用户所在城市  
                user.setCity(jsonObject.getString("city"));  
                // 用户头像  
                user.setHeadImgUrl(jsonObject.getString("headimgurl"));  
                // 用户特权信息  
//                user.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));  
            } catch (Exception e) {  
                user = null;  
                int errorCode = jsonObject.getInt("errcode");  
                String errorMsg = jsonObject.getString("errmsg");  
                log.error("获取用户信息失败 errcode:{} errmsg:{}，reqUrl{}", errorCode, errorMsg);  
            }  
        }  
        return user;  
    } 
    
    /** 
     * 签名算法 
     * @param ticket 
     * @return 
     */  
    public static SignatureInfo getSignature(SignatureInfo sign,Ticket ticket){
        String data = "jsapi_ticket="+ticket.getTicket()+"&noncestr="+sign.getNoncestr()+"&timestamp="+sign.getTimestamp()+"&url="+sign.getUrl();  
        String signature =  new SHA1().getDigestOfString(data.getBytes());   
        sign.setSignature(signature);  
        log.info("signature="+sign.getSignature());  
        return sign;  
    }  
    
	/**
	 * 获取ticket
	 * @param accessToken
	 * @return
	 */
	public static Ticket getTicket(String accessToken) {
		Ticket ticket = new Ticket();
		String getTicket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		String url = getTicket.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = httpRequest(url, "POST", null);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				log.error("获取ticket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			} else {
				ticket.setErrcode(jsonObject.getString("errcode"));
				ticket.setErrmsg(jsonObject.getString("errmsg"));
				ticket.setExpires_in(jsonObject.getString("expires_in"));
				ticket.setTicket(jsonObject.getString("ticket"));
			}
		}
		return ticket;
	}
}
