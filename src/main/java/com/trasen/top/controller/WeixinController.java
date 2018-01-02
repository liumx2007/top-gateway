package com.trasen.top.controller;

import com.google.common.collect.Maps;
import com.trasen.top.modle.AccessToken;
import com.trasen.top.modle.JsapiSignature;
import com.trasen.top.modle.JsapiTicket;
import com.trasen.top.modle.UserToken;
import com.trasen.top.modle.template.Template;
import com.trasen.top.modle.template.TemplateParam;
import com.trasen.top.service.WeixinService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by zhangxiahui on 17/6/18.
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController {

    private static final Logger logger = Logger.getLogger(WeixinController.class);

    @Autowired
    WeixinService weixinService;

    @ResponseBody
    @RequestMapping(value = "/fetchAccessToken", method = RequestMethod.POST)
    public Map<String,Object> fetchAccessToken(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String appid = MapUtils.getString(params, "appid");
            AccessToken accessToken = weixinService.getAccessToken(appid);
            if(accessToken!=null){
                accessToken.setSecret("");
                result.put("accessToken",accessToken);
            }else{
                result.put("status", 0);
                result.put("msg", "appid错误");
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取微信Token参数对象异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取微信Token异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/fetchJsapiTicket", method = RequestMethod.POST)
    public Map<String,Object> fetchJsapiTicket(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String appid = MapUtils.getString(params, "appid");
            JsapiTicket jsapiTicket = weixinService.getfetchJsapiTicket(appid);
            if(jsapiTicket!=null){
                jsapiTicket.setSecret("");
                result.put("jsapiTicket",jsapiTicket);
            }else{
                result.put("status", 0);
                result.put("msg", "appid错误");
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取微信Ticket参数对象异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取微信Ticket异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/fetchJsapiSignature", method = RequestMethod.POST)
    public Map<String,Object> getJsapiSignature(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String appid = MapUtils.getString(params, "appid");
            String url = MapUtils.getString(params, "url");
            JsapiSignature jsapiSignature = weixinService.getJsapiSignature(appid,url);
            if(jsapiSignature!=null){
                result.put("jsapiSignature",jsapiSignature);
            }else{
                result.put("status", 0);
                result.put("msg", "appid错误");
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取微信Signature参数对象异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取微信Signature异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/message/template/send", method = RequestMethod.POST)
    public Map<String,Object> sendTemplateMessage(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String appid = MapUtils.getString(params, "appid");
            String touser = MapUtils.getString(params, "touser");
            String templateId = MapUtils.getString(params, "templateId");
            String first = MapUtils.getString(params, "first");
            String remark = MapUtils.getString(params, "remark");
            String keyword1 = MapUtils.getString(params, "keyword1");
            String keyword2 = MapUtils.getString(params, "keyword2");
            String keyword3 = MapUtils.getString(params, "keyword3");
            String keyword4 = MapUtils.getString(params, "keyword4");
            String keyword5 = MapUtils.getString(params, "keyword5");

            Template tem=new Template();
            tem.setTemplateId(templateId);
            tem.setToUser(touser);
            List<TemplateParam> paras=new ArrayList<TemplateParam>();
            if(first!=null){
                paras.add(new TemplateParam("first",first,"#FF3333"));
            }
            if(remark!=null){
                paras.add(new TemplateParam("remark",remark,"#0044BB"));
            }
            if(keyword1!=null){
                paras.add(new TemplateParam("keyword1",keyword1,"#0044BB"));
            }
            if(keyword2!=null){
                paras.add(new TemplateParam("keyword2",keyword2,"#0044BB"));
            }
            if(keyword3!=null){
                paras.add(new TemplateParam("keyword3",keyword3,"#0044BB"));
            }
            if(keyword4!=null){
                paras.add(new TemplateParam("keyword4",keyword4,"#0044BB"));
            }
            if(keyword5!=null){
                paras.add(new TemplateParam("keyword5",keyword5,"#0044BB"));
            }

            tem.setTemplateParamList(paras);
            boolean boo = weixinService.sendTemplateMessage(appid,touser,templateId,tem);
            if(!boo){
                result.put("status", 0);
                result.put("msg", "发送错误");
            }
        } catch (IllegalArgumentException e) {
            logger.error("发送模版消息参数对象异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("发送模版消息异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/oauth2/access_token", method = RequestMethod.POST)
    public Map<String,Object> getUserToken(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String appid = MapUtils.getString(params, "appid");
            String code = MapUtils.getString(params, "code");
            UserToken userToken = weixinService.getUserTokenApi(appid,code);
            if(userToken!=null){
                result.put("userToken",userToken);
            }else{
                result.put("status", 0);
                result.put("msg", "appid错误");
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取微信userToken参数对象异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取微信userToken异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }




}
