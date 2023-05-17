package com.fly.auth.extend.services;

import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 重写 DefaultTokenServices，实现登录同应用同账号互踢
 *
 * @author lxs
 * @date 2023/5/3
 */
public class SingleLoginTokenServices extends DefaultTokenServices {

    private TokenStore tokenStore;
    private TokenEnhancer accessTokenEnhancer;

    /**
     * 是否登录同应用同账号互踢
     */
    private boolean isSingleLogin;

    public SingleLoginTokenServices(boolean isSingleLogin) {
        this.isSingleLogin = isSingleLogin;
    }

}
