package com.tomylearn.weixinapi.demoWebapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * A Oauthlink will store user's weixin oauth verified information.
 * here use Spring JPA to persistent
 */
@Entity
@Table(name = "T_OAUTHLINK")
public class Oauthlink implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "authorizer_appid")
    private String authorizerAppid;

    @Column(name = "oauth_nickName")
    private String oauthNickName;

    @Column(name = "oauth_headImg")
    private String oauthHeadImg;

    @Column(name = "oauth_userName")
    private String oauthUserName;

    @Column(name = "authorizer_access_token")
    private String authorizerAccessToken;

    @Column(name = "expire_in")
    private Integer expireIn;

    @Column(name = "create_time_access")
    private Date createTime_access;

    @Column(name = "authorizer_refresh_token")
    private String authorizerRefreshToken;

    @Column(name = "expire_in_refresh")
    private Date expireIn_refresh;

    @Column(name = "create_time_refresh")
    private Date createTime_refresh;

    @Column(name = "linked_time")
    private Date linkedTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public Date getCreateTime_access() {
        return createTime_access;
    }

    public void setCreateTime_access(Date createTime_access) {
        this.createTime_access = createTime_access;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public Date getExpireIn_refresh() {
        return expireIn_refresh;
    }

    public void setExpireIn_refresh(Date expireIn_refresh) {
        this.expireIn_refresh = expireIn_refresh;
    }

    public Date getCreateTime_refresh() {
        return createTime_refresh;
    }

    public void setCreateTime_refresh(Date createTime_refresh) {
        this.createTime_refresh = createTime_refresh;
    }

    public Date getLinkedTime() {
        return linkedTime;
    }

    public void setLinkedTime(Date linkedTime) {
        this.linkedTime = linkedTime;
    }

    public String getOauthNickName() {
        return oauthNickName;
    }

    public void setOauthNickName(String oauthNickName) {
        this.oauthNickName = oauthNickName;
    }

    public String getOauthHeadImg() {
        return oauthHeadImg;
    }

    public void setOauthHeadImg(String oauthHeadImg) {
        this.oauthHeadImg = oauthHeadImg;
    }

    public String getOauthUserName() {
        return oauthUserName;
    }

    public void setOauthUserName(String oauthUserName) {
        this.oauthUserName = oauthUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Oauthlink oauthlink = (Oauthlink) o;

        if ( !Objects.equals(userId, oauthlink.userId) ||
                !Objects.equals(authorizerAppid, oauthlink.authorizerAppid))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId+","+authorizerAppid);
    }

    @Override
    public String toString() {
        return "Oauthlink{" +
                "userId=" + userId +
                ", authorizerAppid='" + authorizerAppid + "'" +
                ", oauthNickName='" + oauthNickName + "'" +
                ", authorizerAccessToken='" + authorizerAccessToken + "'" +
                ", expireIn='" + expireIn + "'" +
                ", createTime_access='" + createTime_access + "'" +
                ", authorizerRefreshToken='" + authorizerRefreshToken + "'" +
                ", expireIn_refresh='" + expireIn_refresh + "'" +
                ", createTime_refresh='" + createTime_refresh + "'" +
                ", linkedTime='" + linkedTime + "'" +
                '}';
    }

    /**
     * whether accessToken is still alive
     * @return
     */
    public boolean isAliveAccessToken(){
        if(authorizerAccessToken!=null&&!"".equals(authorizerAccessToken)&&expireIn>0&&createTime_access!=null){
            return dateAddUnits(createTime_access,expireIn).before(new Date());
        }else {
            return false;
        }
    }
    
    private static Date dateAddUnits(Date date, int number){
		if(date!=null){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			gc.add(Calendar.SECOND, number);
			return gc.getTime();
		}else {
			throw new RuntimeException("invalid Calendar unit!");
		}
	}
}
