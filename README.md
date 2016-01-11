# weixinApi
weixin api for its MP platform part

       本API主要针对微信公众号第三方平台API, 官方文档在: https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318587&lang=zh_CN, 
由于需要授权处理，因此需要预先了解下OAuth协议。
	Package: com.tomylearn.weixinapi.openapi是参照官方文档的实现，主要的实现类是：com.tomylearn.weixinapi.openapi.MpOauthApiOpen，使用jersey做restful call.
	Package: com.tomylearn.weixinapi.demoWebapp 是用Spring MVC演示了调用MpOauthApiOpen实现微信第三方平台授权流程
	基本的调用流程为：a)获取第三方平台access_token b)获取预授权码 c)使用授权码换取公众号的授权信息 d)获取（刷新）授权公众号的令牌 e)获取授权方信息
	
How To Use it:
    1)在微信开放平台上，创建公众号第三方平台. https://open.weixin.qq.com
    2)把您的application配置信息填写到weixin-conf.properties文件
    3)参见类MpOauthApiOpen的实现
    4)参见: OauthApiController类，该类演示了如何调用MpOauthApiOpen来实现微信第三方认证，其中重要的api是: 
            /weixin/receivesysmsg : weixin每隔10分钟call这个地址把component_access_token传过来，我们的系统需要缓存这个值，这里的demo是把这个值保存在文件中的
            /weixin/tooauth  : 引导用户进入授权说明页面
            /weixin/oauth/callback  :  用户授权结束后，weixin server会call这个api返回信息，这个地址是自己配置的
    5)在需要调用微信MP api的时候，使用OauthTokenCache.getInstance().getUserOauthlink(userID) ，然后getAuthorizerAccessToken() 得到AccessToken