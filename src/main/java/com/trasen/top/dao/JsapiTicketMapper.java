package com.trasen.top.dao;

import com.trasen.top.modle.JsapiTicket;

/**
 * Created by zhangxiahui on 17/6/18.
 */
public interface JsapiTicketMapper {

    JsapiTicket getJsapiTicketForUpdate(String appid);

    void updateJsapiTicket(JsapiTicket jsapiTicket);

}
