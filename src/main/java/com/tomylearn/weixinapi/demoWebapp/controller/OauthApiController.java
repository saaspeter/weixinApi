package com.tomylearn.weixinapi.demoWebapp.controller;

import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tomylearn.weixinapi.demoWebapp.domain.Oauthlink;
import com.tomylearn.weixinapi.demoWebapp.util.OauthTokenCache;
import com.tomylearn.weixinapi.demoWebapp.util.WeixinConstant;
import com.tomylearn.weixinapi.openapi.CommonToken;
import com.tomylearn.weixinapi.openapi.MpOauthApiOpen;
import com.tomylearn.weixinapi.openapi.OpenAuthResult;
import com.tomylearn.weixinapi.openapi.encode.WXBizMsgCrypt;

@Controller
public class OauthApiController {
	
	Logger log = Logger.getLogger(OauthApiController.class);

	@Inject
	private WeixinConstant wexinConst;

	private static MpOauthApiOpen weixinApiOpen = new MpOauthApiOpen();

	/**
	 * to oauth page, this page has a link to Weixin MP account login page. 
	 * so in this method, need load the preAuthCode which will be used to weixin MP login page
	 * @return
	 */
	@RequestMapping("/weixin/tooauth")
	public ModelAndView weixin_tooauth() {
		CommonToken preAuthCode = OauthTokenCache.getInstance().getPreAuthCode();
		if(preAuthCode==null||!preAuthCode.isLive()){
			String commonToken = null;
			if(OauthTokenCache.getInstance().getCommonToken()!=null){
				commonToken = OauthTokenCache.getInstance().getCommonToken().getToken();
			}
			preAuthCode = weixinApiOpen.getPreAuthCode(wexinConst.getOpenAppId(), commonToken);
		}
		String preAuthCodeStr = "";
		if(preAuthCode!=null){
			preAuthCodeStr = preAuthCode.getToken();
		}
        ModelAndView mav = new ModelAndView("weixinoauthpage.html");
		mav.addObject("preAuthCode", preAuthCodeStr);
		mav.addObject("appId", wexinConst.getOpenAppId());
		return mav;
	}

	/**
	 * when user login MP account and authorized to third part app, weixin will redirect the registered callback url
	 * @param auth_code
	 * @return
	 */
	@RequestMapping("/weixin/oauth/callback")
	public ModelAndView weixinCallback(@RequestParam("auth_code") String auth_code){
		OpenAuthResult result = weixinApiOpen.queryAuth(OauthTokenCache.getInstance().getCommonToken().getToken()
                , wexinConst.getOpenAppId(), auth_code);
        if(result!=null){
        	Long userID = null; // userID is from user session
			//Long userID = SecurityUtils.getCurrentUserID();
            // check if already exist, if exists then delete
            //Oauthlink oauthlink = oauthlinkDao.findOne(userID);
        	// TODO here get oauthlink from DB accroding to userID
            Oauthlink oauthlink = null;
            if(oauthlink!=null){
            	// TODO delete oauthlink
                //oauthlinkDao.delete(oauthlink);
            }
            oauthlink = new Oauthlink();
            oauthlink.setAuthorizerAccessToken(result.getAuthorizerAccessToken());
            oauthlink.setAuthorizerRefreshToken(result.getAuthorizerRefreshToken());
            oauthlink.setAuthorizerAppid(result.getAuthorizerAppid());
            Date curDate = new Date();
            oauthlink.setCreateTime_access(curDate);
            oauthlink.setCreateTime_refresh(curDate);
            oauthlink.setExpireIn(result.getExpireIn());
            oauthlink.setUserId(userID);
            // TODO save oauthlink to DB
            //oauthlinkDao.save(oauthlink);

			if(userID!=null){
				OauthTokenCache.getInstance().putUserOauthlink(userID, oauthlink);
			}
        }

		ModelAndView mav = new ModelAndView("weixinoauthpage.html");
		mav.addObject("userAccessToken", result.getAuthorizerAccessToken());
		mav.addObject("expireIn", result.getExpireIn());
		return mav;
	}

	@RequestMapping(value="/weixin/receivesysmsg", method = RequestMethod.POST)
	@ResponseBody
	//according to document, weixin will call this api every 10 minutes
	public String receivesysmsg(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
								@RequestParam("nonce") String nonce, @RequestParam("encrypt_type") String encrypt_type,
								@RequestParam("msg_signature") String msg_signature,
								@RequestBody String postData) throws Exception{
		log.info("signature:"+signature+" | timestamp:"+timestamp+" | nonce:"+nonce
				 +" | encrypt_type:"+encrypt_type+" msg_signature:"+msg_signature+" | postData:"+postData);

		try {
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(wexinConst.getOpenReceiveMsgToken(),
										wexinConst.getOpenEncryptKey(), wexinConst.getOpenAppId());
			String afterDecrypt = msgCrypt.decryptMsgForComponentVerifyTicket(msg_signature, timestamp, nonce, postData);
			// here every time weixin call this api, we will call weixin api to get component_access_token
			// but maybe we don't need do, because expire time may be 7200 second, we can cache it.
			// here for simple, we don't cache it
			if(afterDecrypt!=null && afterDecrypt.length()>0) {
				CommonToken commonToken = weixinApiOpen.getComponentTokenOpen(wexinConst.getOpenAppId(), wexinConst.getOpenAppSecret(), afterDecrypt);
				OauthTokenCache.getInstance().setCommonToken(commonToken);
			}else {
				log.error("decrypt has errors, decrypt string is empty. timestamp:"+timestamp);
			}
		} catch (Exception e) {
			log.error("error when decrypt, parameters, signature:"+signature
					+" | timestamp:"+timestamp+" | nonce:"+nonce
					+" | encrypt_type:"+encrypt_type+" msg_signature:"
					+msg_signature+" | postData:"+postData ,e);
			throw e;
		}
		// return pure string
		return "success";
	}

}
