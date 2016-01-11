package com.tomylearn.weixinapi.demoWebapp.util;

import javax.inject.Inject;

import com.tomylearn.weixinapi.demoWebapp.domain.Oauthlink;
import com.tomylearn.weixinapi.openapi.CommonToken;
import com.tomylearn.weixinapi.openapi.MpOauthApiOpen;
import com.tomylearn.weixinapi.openapi.OpenAuthResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuefan on 2015/10/12.
 */
public class OauthTokenCache {

    private static OauthTokenCache instance;

    private static MpOauthApiOpen weixinApiOpen = new MpOauthApiOpen();

//    @Inject
//    private OauthlinkRepository oauthlinkDao;
    @Inject
    private WeixinConstant wexinConst;

    private static final String PersistentFile = "/opt/appdata/beacon/oauthcache.properties";
    private static final String CommonToken_Key = "weixin.component_access_token";

    private OauthTokenCache(){}

    public static OauthTokenCache getInstance(){
        if(instance==null){
            synchronized(OauthTokenCache.class){
                if(instance==null){
                    instance = new OauthTokenCache();
                    PropertiesFileUtil.checkAndCreateFile(PersistentFile);
                }
            }
        }
        return instance;
    }

    // component_access_token
    private CommonToken commonToken;
    // pre_auth_code
    private CommonToken preAuthCode;
    // map key: userName, value: this user's oauth result
    private Map<Long, Oauthlink> map = new HashMap<Long, Oauthlink>();

    public CommonToken getCommonToken() {
        if(this.commonToken==null){
            String commonTokenStr = PropertiesFileUtil.readValue(PersistentFile, CommonToken_Key);
            if(commonTokenStr!=null&&!"".equals(commonTokenStr)){
                String[] tokenArr = commonTokenStr.split(",");
                if(tokenArr!=null&&tokenArr.length==3){
                    this.commonToken = new CommonToken(tokenArr[0], Integer.parseInt(tokenArr[1]),
                                           new Date(Long.parseLong(tokenArr[2])) );
                }
            }
        }
        return this.commonToken;
    }

    // set commonToken and save it to file
    public void setCommonToken(CommonToken commonToken) {
        if(commonToken!=null) {
            this.commonToken = commonToken;
            PropertiesFileUtil.writePropertiesFile(PersistentFile, CommonToken_Key,
                    commonToken.getToken() + "," + commonToken.getExpireIn() + "," + commonToken.getCreateDate().getTime());
        }
    }

    public CommonToken getPreAuthCode() {
        return this.preAuthCode;
    }

    public void setPreAuthCode(CommonToken preAuthCode) {
        this.preAuthCode = preAuthCode;
    }

    public void putUserOauthlink(Long userID, Oauthlink oauthlink){
        this.map.put(userID, oauthlink);
    }

    // get user auth link object according to userID, include accessToken
    public Oauthlink getUserOauthlink(Long userID){
        Oauthlink result = this.map.get(userID);
        if(result==null || !result.isAliveAccessToken()){
            this.map.remove(userID);
            // get accessToken from refreshToken
            OpenAuthResult authResult = weixinApiOpen.refreshAccessToken(getCommonToken().getToken(),
                        wexinConst.getOpenAppId(), result.getAuthorizerAppid(), result.getAuthorizerRefreshToken());
            result.setExpireIn(authResult.getExpireIn());
            result.setAuthorizerAccessToken(authResult.getAuthorizerAccessToken());
            result.setCreateTime_access(new Date());
            //TODO save the oauthlink object
            //oauthlinkDao.save(result); 
            this.map.put(userID, result);
            
        }
        return result;
    }
}
