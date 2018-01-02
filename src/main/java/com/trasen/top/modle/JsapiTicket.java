package com.trasen.top.modle;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/6/17.
 */
@Setter
@Getter
public class JsapiTicket {
    /**
     * 微信公众号AppID
     * */
    private String appid;

    /**
     * 微信公众号secret
     * */
    private String secret;

    /**
     * 公众号用于调用微信JS接口的临时票据
     * */
    private String ticket;

    /**
     * 凭证有效时间，单位：秒
     * */
    private Integer expiresIn;

    /**
     * 凭证过期时间
     * */
    private Date expiresTime;
}
