package com.shtd.josh.dao;

import com.shtd.josh.domain.WeixinUser;

public interface WeixinUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WeixinUser record);

    int insertSelective(WeixinUser record);

    WeixinUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeixinUser record);

    int updateByPrimaryKey(WeixinUser record);
    
    WeixinUser getWeixinUserByOpenId(String openId);
    
    int deleteWeixinUserByOpenId(String openId);
}