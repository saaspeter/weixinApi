package com.tomylearn.weixinapi.openapi;

/**
 * Created by xuefan on 2015/10/1.
 */
public class MpAccount {

    private String nickName;
    private String headImg;
    private String serviceTypeInfo;
    private String verifyTypeInfo;
    private String userName;
    private String alias;
    private String qrcodeUrl;
    private String authorizationInfo;
    private String appid;
    private String[] funcInfo;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(String verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getAuthorizationInfo() {
        return authorizationInfo;
    }

    public void setAuthorizationInfo(String authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String[] getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String[] funcInfo) {
        this.funcInfo = funcInfo;
    }
}
