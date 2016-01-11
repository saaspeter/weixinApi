package com.tomylearn.weixinapi.openapi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by xuefan on 2015/10/1.
 * result of weixin api: api_query_auth?component_access_token=xxxx
 * @see <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318587&lang=zh_CN">代公众号调用接口</a>
 * need store the result into DB
 */
public class OpenAuthResult {

    private String authorizerAppid;
    private String authorizerAccessToken;
    private String authorizerRefreshToken;
    private String[] funcInfo;

    private Date createDate;
    private int expireIn;

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String[] getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String[] funcInfo) {
        this.funcInfo = funcInfo;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public boolean isLiveAccessToken(){
        if(createDate!=null && expireIn>0){
            Date currDate = new Date();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(createDate);
            gc.add(Calendar.SECOND, expireIn);
            return gc.getTime().after(currDate);
        }else {
            return false;
        }
    }
}
