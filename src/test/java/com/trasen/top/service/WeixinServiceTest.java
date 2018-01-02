package com.trasen.top.service;

import com.trasen.top.modle.AccessToken;
import com.trasen.top.modle.JsapiTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangxiahui on 17/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class WeixinServiceTest {

    @Autowired
    WeixinService weixinService;

    @Test
    @Rollback(false)
    public void getAccessToken(){
        AccessToken accessToken = weixinService.getAccessToken("wx883815fb0da06f3d");
        System.out.println("=======AppID:"+accessToken.getAppid()+"===过期时间:"+accessToken.getExpiresTime());
    }

    @Test
    @Rollback(false)
    public void getfetchJsapiTicket(){
        JsapiTicket ticket = weixinService.getfetchJsapiTicket("wx883815fb0da06f3d");
        System.out.println("=======AppID:"+ticket.getAppid()+"===过期时间:"+ticket.getExpiresTime());
    }


}
