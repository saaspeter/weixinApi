package com.tomylearn.weixinapi.openapi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by xuefan on 2015/10/1.
 * common token concept. so many api will return different code with expire time, so here introduce common token
 * the token could be AccessToken, RefreashToken, AuthorizedCode etc.
 */
public class CommonToken {

    private String token;
    private int expireIn;
    private Date createDate;

    public CommonToken(String token, int expireIn, Date createDate) {
        this.token = token;
        this.expireIn = expireIn;
        this.createDate = createDate;
    }

    public String getToken() {
        return token;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public boolean isLive(){
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
