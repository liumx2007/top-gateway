package com.trasen.top.modle;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/6/17.
 */
@Setter
@Getter
public class AccessToken {
    /**
     * 微信公众号AppID
     * */
    private String appid;

    /**
     * 微信公众号secret
     * */
    private String secret;

    /**
     * 获取到的凭证
     * */
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒
     * */
    private Integer expiresIn;

    /**
     * 凭证过期时间
     * */
    private Date expiresTime;
}
