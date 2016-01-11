package com.tomylearn.weixinapi.demoWebapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 */
@Configuration
@PropertySource("classpath:weixin-conf.properties")
public class WeixinConstant {

	@Autowired
	private Environment env;

	/** open.appId **/
    public String getOpenAppId(){
        return env.getProperty("open.appId");
    }

    /** open.encrypt.key **/
    public String getOpenEncryptKey(){
        return env.getProperty("open.encrypt.key");
    }

    /** open.receiveMsg.token **/
    public String getOpenReceiveMsgToken(){
        return env.getProperty("open.receiveMsg.token");
    }

    /** open.appSecret **/
    public String getOpenAppSecret(){
        return env.getProperty("open.appSecret");
    }

}
