package com.shtd.josh.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shtd.josh.dao.WeixinUserDao;
import com.shtd.josh.domain.WeixinUser;

@Service
public class WeixinUserServer {

	@Autowired
	private WeixinUserDao weixinUserDao;
	
	public int saveWeixinUser(WeixinUser customer){
		return weixinUserDao.insertSelective(customer);
	}
	
	public int updateWeixinUser(WeixinUser customer){
		return weixinUserDao.updateByPrimaryKeySelective(customer);
	}
	
	public int deleteWeixinUser(Integer id){
		return weixinUserDao.deleteByPrimaryKey(id);
	}
	
	public WeixinUser selectById(Integer id){
		return weixinUserDao.selectByPrimaryKey(id);
	}
	
	public WeixinUser getWeixinUserByOpenId(String openId){
		return weixinUserDao.getWeixinUserByOpenId(openId);
	}
	
	public int deleteWeixinUserByOpenId(String openId){
		return weixinUserDao.deleteWeixinUserByOpenId(openId);
	}
}
