package com.shtd.josh.domain;

import java.util.Date;

public class WeixinUser {
	
    private Integer id;

    private String loginname;

    private String weixinOpenId;

    private String weixinNickname;

    private Integer weixinSex;

    private String weixinCountry;

    private String weixinProvince;

    private String weixinCity;

    private String weixinLanguage;

    private String weixinHeadImgUrl;

    private Date createDate;

    private Date modifyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId == null ? null : weixinOpenId.trim();
    }

    public String getWeixinNickname() {
        return weixinNickname;
    }

    public void setWeixinNickname(String weixinNickname) {
        this.weixinNickname = weixinNickname == null ? null : weixinNickname.trim();
    }

    public Integer getWeixinSex() {
        return weixinSex;
    }

    public void setWeixinSex(Integer weixinSex) {
        this.weixinSex = weixinSex;
    }

    public String getWeixinCountry() {
        return weixinCountry;
    }

    public void setWeixinCountry(String weixinCountry) {
        this.weixinCountry = weixinCountry == null ? null : weixinCountry.trim();
    }

    public String getWeixinProvince() {
        return weixinProvince;
    }

    public void setWeixinProvince(String weixinProvince) {
        this.weixinProvince = weixinProvince == null ? null : weixinProvince.trim();
    }

    public String getWeixinCity() {
        return weixinCity;
    }

    public void setWeixinCity(String weixinCity) {
        this.weixinCity = weixinCity == null ? null : weixinCity.trim();
    }

    public String getWeixinLanguage() {
        return weixinLanguage;
    }

    public void setWeixinLanguage(String weixinLanguage) {
        this.weixinLanguage = weixinLanguage == null ? null : weixinLanguage.trim();
    }

    public String getWeixinHeadImgUrl() {
        return weixinHeadImgUrl;
    }

    public void setWeixinHeadImgUrl(String weixinHeadImgUrl) {
        this.weixinHeadImgUrl = weixinHeadImgUrl == null ? null : weixinHeadImgUrl.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}