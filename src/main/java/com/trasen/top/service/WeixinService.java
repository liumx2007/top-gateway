package com.trasen.top.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.trasen.top.cache.GlobalCache;
import com.trasen.top.dao.AccessTokenMapper;
import com.trasen.top.dao.JsapiTicketMapper;
import com.trasen.top.modle.AccessToken;
import com.trasen.top.modle.JsapiSignature;
import com.trasen.top.modle.JsapiTicket;
import com.trasen.top.modle.UserToken;
import com.trasen.top.modle.template.Template;
import com.trasen.top.util.HttpUtil;
import com.trasen.top.util.PropertiesUtils;
import com.trasen.top.util.SignConvertUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangxiahui on 17/6/17.
 */
@Component
public class WeixinService {

    @Autowired
    AccessTokenMapper accessTokenMapper;

    @Autowired
    JsapiTicketMapper jsapiTicketMapper;

    Logger logger = Logger.getLogger(WeixinService.class);

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    public AccessToken getAccessToken(String appid){
        AccessToken accessToken = null;
        if(!StringUtil.isEmpty(appid)){
            accessToken = globalCache.getAccessTokenMap().get(appid);
            if(accessToken!=null){
                Date expiresTime = accessToken.getExpiresTime();
                //token还未过期,直接返回
                if(new Date().before(expiresTime)){
                    return accessToken;
                }
            }
            accessToken = accessTokenMapper.getAccessTokenForUpdate(appid);
            if(accessToken==null||accessToken.getSecret()==null){
                return null;
            }
            if(accessToken.getExpiresTime()==null||new Date().after(accessToken.getExpiresTime())){
                accessToken = getAccessTokenFromApi(accessToken.getAppid(),accessToken.getSecret());
                accessTokenMapper.updateAccessToken(accessToken);
            }
            if(accessToken.getExpiresTime()!=null){
                Map<String,AccessToken> map = globalCache.getAccessTokenMap();
                map.put(appid,accessToken);
                globalCache.setAccessTokenMap(map);
                return accessToken;
            }
        }
        return null;

    }

    /**
     * 获取access_token
     *
     * @author zhangxiahui
     * @date 17/6/17.
     */
    public AccessToken getAccessTokenFromApi(String appid, String appSecret) {
        AccessToken at = new AccessToken();

        String requestUrl = PropertiesUtils.getProperty("access_token_url").replace("APPID", appid).replace("APPSECRET", appSecret);
        logger.info("获取AccessTokenURL:"+requestUrl);
        JSONObject object = HttpUtil.handleRequest(requestUrl, "GET", null);
        logger.info("获取AccessToken出参:"+object.toJSONString());
        String access_token = object.getString("access_token");
        int expires_in = object.getInteger("expires_in");
        if(access_token!=null){
            at.setAppid(appid);
            at.setSecret(appSecret);
            at.setAccessToken(access_token);
            at.setExpiresIn(expires_in);
            int hour = expires_in/3600;
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, hour-1);
            Date expiresTime = c.getTime();
            at.setExpiresTime(expiresTime);
            return at;
        }
        return null;
    }

    public JsapiTicket getfetchJsapiTicket(String appid){
        JsapiTicket ticket = null;
        if(!StringUtil.isEmpty(appid)){
            ticket = globalCache.getJsapiTicketMap().get(appid);
            if(ticket!=null){
                Date expiresTime = ticket.getExpiresTime();
                //token还未过期,直接返回
                if(new Date().before(expiresTime)){
                    return ticket;
                }
            }
            ticket = jsapiTicketMapper.getJsapiTicketForUpdate(appid);
            if(ticket==null||ticket.getSecret()==null){
                return null;
            }
            if(ticket.getExpiresTime()==null||new Date().after(ticket.getExpiresTime())){
                ticket = getfetchJsapiTicketFormApi(ticket.getAppid(),ticket.getSecret());
                jsapiTicketMapper.updateJsapiTicket(ticket);
            }
            if(ticket.getExpiresTime()!=null){
                Map<String,JsapiTicket> map = globalCache.getJsapiTicketMap();
                map.put(appid,ticket);
                globalCache.setJsapiTicketMap(map);
                return ticket;
            }
        }
        return null;
    }

    public JsapiTicket getfetchJsapiTicketFormApi(String appid,String appSecret){
        JsapiTicket ticket = new JsapiTicket();
        AccessToken token = getAccessToken(appid);
        if(token!=null&&token.getAccessToken()!=null){
            String accessToken = token.getAccessToken();
            String requestUrl = PropertiesUtils.getProperty("jsapi_ticket_url").replace("ACCESS_TOKEN", accessToken);
            logger.info("获取JsapiTicketURL:"+requestUrl);
            JSONObject object = HttpUtil.handleRequest(requestUrl, "GET", null);
            logger.info("获取JsapiTicket出参:"+object.toJSONString());
            //errmsg
            String errmsg = object.getString("errmsg");
            if(errmsg!=null&&"ok".equals(errmsg)){
                String jsapi_ticket = object.getString("ticket");
                int expires_in = object.getInteger("expires_in");
                if(jsapi_ticket!=null){
                    ticket.setAppid(appid);
                    ticket.setSecret(appSecret);
                    ticket.setTicket(jsapi_ticket);
                    ticket.setExpiresIn(expires_in);
                    int hour = expires_in/3600;
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.HOUR_OF_DAY, hour-1);
                    Date expiresTime = c.getTime();
                    ticket.setExpiresTime(expiresTime);
                    return ticket;
                }
            }
        }
        return null;
    }

    public JsapiSignature getJsapiSignature(String appid,String url){
        JsapiSignature signature = null;
        if(!StringUtil.isEmpty(appid)&&!StringUtil.isEmpty(url)){
            //获取签名signature
            String nonceStr = UUID.randomUUID().toString();
            String timestamp = Long.toString(System.currentTimeMillis() / 1000);
            JsapiTicket jsapiTicket = getfetchJsapiTicket(appid);
            if(jsapiTicket!=null&&!StringUtil.isEmpty(jsapiTicket.getTicket())){
                String ticket = jsapiTicket.getTicket();
                String signatureURL = "jsapi_ticket=" + ticket +
                        "&noncestr=" + nonceStr +
                        "&timestamp=" + timestamp +
                        "&url=" + url;
                String signatureStr = SignConvertUtil.SHA1(signatureURL);
                signature = new JsapiSignature();
                signature.setJsapiTicket(ticket);
                signature.setNoncestr(nonceStr);
                signature.setTimestamp(timestamp);
                signature.setUrl(url);
                signature.setSignature(signatureStr);
                return signature;
            }
        }
        return null;
    }

    public boolean sendTemplateMessage(String appid, String touser, String templateId, Template template){
        boolean flag=false;
        if(!StringUtil.isEmpty(appid)&&!StringUtil.isEmpty(touser)&&!StringUtil.isEmpty(templateId)){
            AccessToken token = getAccessToken(appid);
            if(token!=null&&!StringUtil.isEmpty(token.getAccessToken())){
                String requestUrl = PropertiesUtils.getProperty("send_template_message").replace("ACCESS_TOKEN", token.getAccessToken());
                JSONObject jsonResult=HttpUtil.handleRequest(requestUrl, "POST", template.toJSON());
                if(jsonResult!=null){
                    int errorCode=jsonResult.getInteger("errcode");
                    String errorMessage=jsonResult.getString("errmsg");
                    if(errorCode==0){
                        flag=true;
                    }else{
                        logger.info("模板消息发送失败:"+errorCode+","+errorMessage);
                        flag=false;
                    }
                }
            }
        }
        return flag;
    }

    public UserToken getUserTokenApi(String appid,String code){
        UserToken userToken = null;
        if(appid!=null&&code!=null){
            AccessToken accessToken = accessTokenMapper.getAccessTokenForUpdate(appid);
            if(accessToken==null||accessToken.getSecret()==null){
                return null;
            }
            String requestUrl = PropertiesUtils.getProperty("user_token_url")
                    .replace("APPID", appid).replace("SECRET", accessToken.getSecret()).replace("CODE",code);
            logger.info("用户信息授权URL:"+requestUrl);
            JSONObject object = HttpUtil.handleRequest(requestUrl, "GET", null);
            logger.info("用户信息授权出参:"+object.toJSONString());
            if(object!=null&&object.getString("openid")!=null){
                userToken = new UserToken();
                userToken.setAccessToken(object.getString("access_token"));
                userToken.setExpiresIn(object.getInteger("expires_in"));
                userToken.setOpenid(object.getString("openid"));
                userToken.setScope(object.getString("scope"));
                userToken.setRefreshToken(object.getString("refresh_token"));
            }
        }
        return userToken;

    }
}
