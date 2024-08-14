package com.fly.auth.config;

import com.fly.auth.granter.CaptchaTokenGranter;
import com.fly.auth.granter.SmsCodeTokenGranter;
import com.fly.auth.granter.SocialTokenGranter;
import com.fly.auth.service.SingleLoginTokenServices;
import com.fly.auth.service.impl.ClientDetailsServiceImpl;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.annotation.Resource;
import java.util.*;

/**
 * 认证中心配置
 *
 * @author lxs
 * @date 2023/5/16
 */
@Order(2)
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    private final ClientDetailsServiceImpl clientService;

    private final RedisConnectionFactory redisConnectionFactory;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RedisUtils redisService;

    private final AuthRequestFactory factory;

    // 开关：同应用账号互踢
    boolean isSingleLogin = false;


    /**
     * todo OAuth2-客户端配置
     * <p>redisService
     * 包括client的id/secret
     * 合法的授权方法
     *
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

//        clients.inMemory()// 使用in-memory存储
//                .withClient("flycloud-system")// client_id
//                .secret(new BCryptPasswordEncoder().encode("wW@@@257007"))//客户端密钥
////                .resourceIds("xuecheng-plus")//资源列表
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
//                .scopes("all")// 允许的授权范围
//                .autoApprove(false)//false跳转到授权页面
//                .redirectUris("http://localhost/123");//客户端接收授权码的重定向地址

        clientService.setSelectClientDetailsSql(Oauth2Constants.SELECT_CLIENT_DETAIL_SQL);
        clientService.setFindClientDetailsSql(Oauth2Constants.FIND_CLIENT_DETAIL_SQL);
        clients.withClientDetails(clientService);
    }


    /**
     * todo 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     * <p>
     * 生成什么样的token
     * 审批策略
     * token增强器
     * 授权方式
     * <p>
     * authenticationManager：               指定认证管理器，用于验证用户的身份（如用户名/密码是否匹配）。一般要自定义实现，或者使用默认的 ProviderManager 认证管理器。
     * tokenServices：                       指定令牌管理服务，用于管理令牌。Spring Security OAuth2 框架提供了多种实现，例如 DefaultTokenServices 或者 JwtTokenServices。
     * allowedTokenEndpointRequestMethods：  指定允许的请求方法获取令牌。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        DefaultTokenServices tokenServices = this.createDefaultTokenServices();
        // token增强链
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.tokenEnhancer(), this.jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // 配置tokenServices参数
        this.addUserDetailsService(tokenServices);

        endpoints
                .tokenGranter(tokenGranter(endpoints, tokenServices))
                .tokenServices(tokenServices)
                .accessTokenConverter(jwtAccessTokenConverter());
    }


    /**
     * todo 令牌端点（Token Endpoint）的安全配置/策略
     * <p>
     * oauth2服务的放行
     * oauth2服务的过滤器
     * oauth2端点放行策略
     *
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {

//        security
//                .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开
//                .checkTokenAccess("permitAll()")                  //oauth/check_token公开
//                .allowFormAuthenticationForClients()                //表单认证（申请令牌）
//        ;

        security
				.tokenKeyAccess("isAuthenticated()")   // spel表达式 访问公钥端点（/auth/token_key）需要认证
                .checkTokenAccess("isAuthenticated()") // spel表达式 访问令牌解析端点（/auth/check_token）需要认证
                .allowFormAuthenticationForClients();  // 允许表单认证请求
    }



    // ============================================================================================================

    /**
     * 用于生成和签名JWT/验证和解码JWT
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(Oauth2Constants.SIGN_KEY);
        return jwtAccessTokenConverter;
    }

    /**
     * 配置token存储到redis中
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }



    // ============================================================================================================

    /**
     * 创建默认的tokenServices
     *
     */
    private DefaultTokenServices createDefaultTokenServices() {

        DefaultTokenServices tokenServices = new SingleLoginTokenServices(isSingleLogin);
        tokenServices.setTokenStore(redisTokenStore());
        // 支持刷新Token
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        tokenServices.setReuseRefreshToken(Boolean.FALSE);
        tokenServices.setClientDetailsService(clientService);
        this.addUserDetailsService(tokenServices);
        return tokenServices;
    }


    /**
     * 配置添加tokenServices参数
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices) {

        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }


    /**
     * 重点
     * 先获取已经有的五种授权，然后添加我们自己的进去
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints, DefaultTokenServices tokenServices) {

        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 短信验证码模式
        granters.add(new SmsCodeTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 社交登录模式
        granters.add(new SocialTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService, factory));
        // 增加密码模式
        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granters);
    }


    /**
     * jwt token增强，添加额外信息
     *
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {

        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

                // 添加额外信息的map
                final Map<String, Object> additionMessage = new HashMap<>(2);
                // 对于客户端鉴权模式，直接返回token
                if (oAuth2Authentication.getUserAuthentication() == null) {
                    return oAuth2AccessToken;
                }
                // 获取当前登录的用户
                FlyUser user = (FlyUser) oAuth2Authentication.getUserAuthentication().getPrincipal();

                // 如果用户不为空 则把id放入jwt token中
                if (user != null) {
                    additionMessage.put(Oauth2Constants.USER_ID, String.valueOf(user.getId()));
                    additionMessage.put(Oauth2Constants.USER_NAME, user.getUsername());
                    additionMessage.put(Oauth2Constants.AVATAR, user.getAvatar());
                    additionMessage.put(Oauth2Constants.ROLE_ID, String.valueOf(user.getRoleId()));
                    additionMessage.put(Oauth2Constants.TYPE, user.getType());
                    additionMessage.put(Oauth2Constants.TENANT_ID, user.getTenantId());
                }
                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
                return oAuth2AccessToken;
            }
        };
    }

}
