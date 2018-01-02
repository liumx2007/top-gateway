package com.trasen.top.cache;

import com.trasen.top.modle.AccessToken;
import com.trasen.top.modle.JsapiTicket;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GlobalCache {

    protected Logger logger = Logger.getLogger(getClass());


    private static GlobalCache globalCache = new GlobalCache();


    private Map<String,AccessToken> accessTokenMap;

    private Map<String,JsapiTicket> jsapiTicketMap;

    private GlobalCache() {
        accessTokenMap = new HashMap<>();
        jsapiTicketMap = new HashMap<>();
    }


    public static GlobalCache getGlobalCache() {
        return globalCache;
    }


    public Map<String, AccessToken> getAccessTokenMap() {
        return accessTokenMap;
    }

    public void setAccessTokenMap(Map<String, AccessToken> accessTokenMap) {
        this.accessTokenMap = accessTokenMap;
    }

    public Map<String, JsapiTicket> getJsapiTicketMap() {
        return jsapiTicketMap;
    }

    public void setJsapiTicketMap(Map<String, JsapiTicket> jsapiTicketMap) {
        this.jsapiTicketMap = jsapiTicketMap;
    }
}
