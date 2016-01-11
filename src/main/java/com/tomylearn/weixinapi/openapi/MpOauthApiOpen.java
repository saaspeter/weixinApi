package com.tomylearn.weixinapi.openapi;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.Date;

/**
 * Created by xuefan on 2015/10/1.
 * @see <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318587&lang=zh_CN">代公众号调用接口</a>
 */
public class MpOauthApiOpen {

    //static Logger log = Logger.getLogger(MpOauthApiOpen.class.getName());

    private static ClientConfig config = new DefaultClientConfig();
    private static Client client = Client.create(config);

    static{
        client.setConnectTimeout(4000); // set read time 4 second
        client.setReadTimeout(4000);
    }

    /**
     * get access token
     * see 官方文档中: 获取第三方平台access_token
     * @return the vaule should be cached
     */
    public CommonToken getComponentTokenOpen(String appid, String appsecret, String verifyTicket){
        CommonToken vo = null;
        WebResource r = client.resource("https://api.weixin.qq.com/cgi-bin/component/api_component_token");
        String inputParam = "{\"component_appid\":\""+appid+"\","
                            +"\"component_appsecret\":\""+appsecret+"\","
                            +"\"component_verify_ticket\":\""+verifyTicket+"\"}";
        String response = r.post(String.class, inputParam);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        if(jsonObj != null && jsonObj.get("component_access_token") != null) {
            Date curDate = new Date();
            String token = jsonObj.get("component_access_token").toString();
            if(token!=null){
                int expireTime = 7200;
                if (jsonObj.get("expires_in") != null) {
                    String expiresStr = jsonObj.get("expires_in").toString();
                    if(expiresStr!=null && expiresStr.trim().length()>0){
                        expireTime = Integer.parseInt(expiresStr.trim());
                    }
                }
                vo = new CommonToken(token, expireTime, curDate);
            }
        }
        return vo;
    }

    /**
     * get preAuth code for page oauth call
     * see 官方文档: 获取预授权码
     * @param appid
     * @param componentAccessToken: value of method getComponentTokenOpen, but Application should pass the parameter from the cache
     * @return
     */
    public CommonToken getPreAuthCode(String appid, String componentAccessToken){
        CommonToken vo = null;
        WebResource r = client.resource("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+componentAccessToken);
        String inputParam = "{\"component_appid\":\""+appid+"\"}";
        String response = r.post(String.class, inputParam);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        if (jsonObj!=null && jsonObj.get("pre_auth_code") != null) {
            Date curDate = new Date();
            String preAuthCode = jsonObj.get("pre_auth_code").toString();
            if(preAuthCode!=null){
                int expireTime = 600;
                if (jsonObj.get("expires_in") != null) {
                    String expiresStr = jsonObj.get("expires_in").toString();
                    if(expiresStr!=null && expiresStr.trim().length()>0){
                        expireTime = Integer.parseInt(expiresStr.trim());
                    }
                }
                vo = new CommonToken(preAuthCode, expireTime, curDate);
            }
        }
        return vo;
    }

    /**
     * get mp user's authorization
     * see 官方文档: 使用授权码换取公众号的授权信息
     * @param componentToken
     * @param appid
     * @param authorizationCode: mp user authorization code returned in page
     * @return
     */
    public OpenAuthResult queryAuth(String componentToken, String appid, String authorizationCode){
        OpenAuthResult result = null;
        WebResource r = client.resource("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+componentToken);
        String inputParam = "{\"component_appid\":\""+appid+"\","
                            +"\"authorization_code\":\""+authorizationCode+"\"}";
        String response = r.post(String.class, inputParam);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        if (jsonObj!=null && jsonObj.get("authorization_info") != null) {
            result = new OpenAuthResult();
            JSONObject authjob = (JSONObject) jsonObj.get("authorization_info");
            String authorizer_appid = authjob.get("authorizer_appid").toString();
            String authorizer_access_token = authjob.get("authorizer_access_token").toString();
            result.setAuthorizerAppid(authorizer_appid);
            String authorizer_refresh_token = authjob.get("authorizer_refresh_token").toString();
            result.setAuthorizerAccessToken(authorizer_access_token);
            result.setAuthorizerRefreshToken(authorizer_refresh_token);
            int expireTime = 600;
            if (jsonObj.get("expires_in") != null) {
                String expiresStr = jsonObj.get("expires_in").toString();
                if(expiresStr!=null && expiresStr.trim().length()>0){
                    expireTime = Integer.parseInt(expiresStr.trim());
                }
                result.setExpireIn(expireTime);
            }
        }
        return result;
    }

    /**
     * get access token from refresh token
     * see weixin api: /cgi-bin/component/api_authorizer_token 获取（刷新）授权公众号的令牌
     * @param componentAccessToken
     * @param componentAppid
     * @param authorizerAppid
     * @param refreashToken
     * @return
     */
    public OpenAuthResult refreshAccessToken(String componentAccessToken, String componentAppid,
                                     String authorizerAppid, String refreashToken){
        OpenAuthResult result = null;
        WebResource r = client.resource("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+componentAccessToken);
        String inputParam = "{\"component_appid\":\""+componentAppid+"\","
                            +"\"authorizer_appid\":\""+authorizerAppid+"\","
                            +"\"authorizer_refresh_token\":\""+refreashToken+"\"}";
        String response = r.post(String.class, inputParam);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        if (jsonObj!=null) {
            result = new OpenAuthResult();
            String authorizer_access_token = jsonObj.get("authorizer_access_token").toString();
            String authorizer_refresh_token = jsonObj.get("authorizer_refresh_token").toString();
            result.setAuthorizerAccessToken(authorizer_access_token);
            result.setAuthorizerRefreshToken(authorizer_refresh_token);
            int expireTime = 600;
            if (jsonObj.get("expires_in") != null) {
                String expiresStr = jsonObj.get("expires_in").toString();
                if(expiresStr!=null && expiresStr.trim().length()>0){
                    expireTime = Integer.parseInt(expiresStr.trim());
                }
                result.setExpireIn(expireTime);
            }
        }
        return result;
    }

    /**
     * get weixin MP account information
     * see 获取授权方的账户信息
     * @param componentAccessToken
     * @param componentAppid
     * @param authorizerAppid
     * @return
     */
    public MpAccount getMpAccountInfo(String componentAccessToken, String componentAppid, String authorizerAppid){
        MpAccount result = null;
        WebResource r = client.resource("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="+componentAccessToken);
        String inputParam = "{\"component_appid\":\""+componentAppid+"\","
                            +"\"authorizer_appid\":\""+authorizerAppid+"\"}";
        String response = r.post(String.class, inputParam);
        JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
        if (jsonObj!=null && jsonObj.get("authorizer_info") != null) {
            result = new MpAccount();
            JSONObject authjob = (JSONObject) jsonObj.get("authorizer_info");
            String nick_name = authjob.get("nick_name").toString();
            String head_img = authjob.get("head_img").toString();
            String service_type_info = authjob.get("service_type_info").toString();
            String verify_type_info = authjob.get("verify_type_info").toString();
            String user_name = authjob.get("user_name").toString();
            String alias = authjob.get("alias").toString();
            String qrcode_url = authjob.get("qrcode_url").toString();
            result.setUserName(user_name);
            result.setNickName(nick_name);
            result.setHeadImg(head_img);
            result.setServiceTypeInfo(service_type_info);
            result.setAlias(alias);
        }
        return result;
    }
}
