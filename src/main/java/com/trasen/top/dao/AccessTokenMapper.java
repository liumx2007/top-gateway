package com.trasen.top.dao;

import com.trasen.top.modle.AccessToken;

/**
 * Created by zhangxiahui on 17/6/17.
 */
public interface AccessTokenMapper {

    AccessToken getAccessTokenForUpdate(String appid);

    void updateAccessToken(AccessToken accessToken);
}
