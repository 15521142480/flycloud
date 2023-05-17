//package com.fly.auth.config;
//
//import com.fly.auth.domain.AuthClientUser;
//import com.fly.auth.extend.services.PreAuthenticatedUserDetailsServices;
//import com.fly.auth.extend.services.SysUserDetailsServices;
//import com.fly.common.constant.Oauth2Constants;
//import com.fly.common.constant.SecurityConstants;
//import com.fly.common.security.config.properties.SecurityAuthorizationProperties;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.CompositeTokenGranter;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.TokenGranter;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//import org.springframework.security.oauth2.provider.token.*;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import java.security.KeyPair;
//import java.util.*;
//
///**
// * 认证中心服务配置
// *
// * @author lxs
// * @date 2023/4/28
// */
//@Configuration
//@EnableAuthorizationServer // 开启授权服务器
////@RequiredArgsConstructor
//public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//
//    // 是否登录同应用同账号互踢
////    @Value("${mate.uaa.isSingleLogin:false}")
//    private boolean isSingleLogin = false;
//
//    // jwt 密钥
//    private static final String SIGNING_KEY = "fly_oauth_123456789";
//
//    //jwt令牌转换器
//    @Lazy
//    @Resource
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    // 数据源
//    @Lazy
//    @Resource
//    private DataSource dataSource;
//
//    // 认证管理对象
//    @Lazy
//    @Resource
//    private AuthenticationManager authenticationManager;
//
//    // 密码编码器
//    @Lazy
//    @Resource
//    private PasswordEncoder passwordEncoder;
//
//    // 客户端配置类
////    private final SecurityAuthorizationProperties securityAuthorizationProperties;
//
//    // 系统登录校验
//    @Lazy
//    @Resource
//    private SysUserDetailsServices sysUserDetailsServices;
//
//    //
//    @Lazy
//    @Resource
//    private TokenEnhancer tokenEnhancer;
//
//    @Lazy
//    @Resource
//    private TokenStore tokenStore;
//
//
//    /**
//     * oauth2客户端配置; 既可以通过授权码获取令牌，也可以通过密码获取令牌(四种方式)
//     *
//     * @param clients
//     */
//    @Override
//    @SneakyThrows
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        System.out.println("password:" + passwordEncoder.encode("123456"));
//
//        // 方式1：从数据库加载客户端信息 (表需按照其字段建立)
//        clients.withClientDetails(jdbcClientDetailsService());
//
//        // 方式2：使用内存方式管理客户端
////        clients.inMemory() // 内存中
////                .withClient("flycloud-system") // 客户端ID
////                .secret(passwordEncoder.encode("w2570078967")) // 秘钥
////                .authorizedGrantTypes("password", "refresh_token") // 授权类型
////                .redirectUris("https://www.baidu.com") // 重定向到的地址
////                .scopes("all"); // 授权范围
//////                .autoApprove(true); // 自动授权，返回验证码
//    }
//
//
//    /**
//     * oauth2数据库源配置
//     *
//     */
//    @Bean
//    public JdbcClientDetailsService jdbcClientDetailsService() {
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//
//
//    /**
//     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        // Token增强
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
//        tokenEnhancers.add(tokenEnhancer);
//        tokenEnhancers.add(jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//
//        //token存储模式设定 默认为InMemoryTokenStore模式存储到内存中
//        endpoints.tokenStore(jwtTokenStore());
//
//        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
//        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
//
//        // 添加验证码授权模式授权者
////        granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
////                endpoints.getOAuth2RequestFactory(), authenticationManager, stringRedisTemplate
////        ));
////
////        // 添加手机短信验证码授权模式的授权者
////        granterList.add(new SmsCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
////                endpoints.getOAuth2RequestFactory(), authenticationManager
////        ));
////
////        // 添加微信授权模式的授权者
////        granterList.add(new WechatTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
////                endpoints.getOAuth2RequestFactory(), authenticationManager
////        ));
//
//        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
//        endpoints
//                .authenticationManager(authenticationManager)
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenEnhancer(tokenEnhancerChain)
//                .tokenGranter(compositeTokenGranter)
//                .tokenServices(tokenServices(endpoints))
//        ;
//    }
//
//    /**
//     * jwt token存储模式
//     */
//    @Bean
//    public JwtTokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
//
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
//        tokenEnhancers.add(tokenEnhancer);
//        tokenEnhancers.add(jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setClientDetailsService(jdbcClientDetailsService());
//        tokenServices.setTokenEnhancer(tokenEnhancerChain);
//
//        // 多用户体系下，刷新token再次认证客户端ID和 UserDetailService 的映射Map
//        Map<String, UserDetailsService> clientUserDetailsServiceMap = new HashMap<>();
//        clientUserDetailsServiceMap.put(SecurityConstants.ADMIN_CLIENT_ID, sysUserDetailsServices); // 系统管理客户端
////        clientUserDetailsServiceMap.put(SecurityConstants.APP_CLIENT_ID, memberUserDetailsService); // Android、IOS、H5 移动客户端
////        clientUserDetailsServiceMap.put(SecurityConstants.WEAPP_CLIENT_ID, memberUserDetailsService); // 微信小程序客户端
//
//        // 刷新token模式下，重写预认证提供者替换其AuthenticationManager，可自定义根据客户端ID和认证方式区分用户体系获取认证用户信息
//        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
//        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsServices<>(clientUserDetailsServiceMap));
//        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
//
//        /**
//         * refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
//         *  1 重复使用：access_token过期刷新时， refresh_token过期时间未改变，仍以初次生成的时间为准
//         *  2 非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新便永不失效达到无需再次登录的目的
//         */
//        tokenServices.setReuseRefreshToken(true);
//
//        return tokenServices;
//
//    }
//
//    /**
//     * 使用非对称加密算法对token签名
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setKeyPair(keyPair());
//        return converter;
//    }
//
//    /**
//     * 密钥库中获取密钥对(公钥+私钥)
//     */
//    @Bean
//    public KeyPair keyPair() {
//        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "123456".toCharArray());
//        KeyPair keyPair = factory.getKeyPair("jwt", "123456".toCharArray());
//        return keyPair;
//    }
//
//    /**
//     * jwt token增强，添加额外信息
//     *
//     * @return TokenEnhancer
//     */
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return new TokenEnhancer() {
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//
//                // 添加额外信息的map
//                final Map<String, Object> additionMessage = new HashMap<>(2);
//                // 对于客户端鉴权模式，直接返回token
//                if (oAuth2Authentication.getUserAuthentication() == null) {
//                    return oAuth2AccessToken;
//                }
//                // 获取当前登录的用户
//                AuthClientUser user = (AuthClientUser) oAuth2Authentication.getUserAuthentication().getPrincipal();
//
//                // 如果用户不为空 则把id放入jwt token中
//                if (user != null) {
//                    additionMessage.put(Oauth2Constants.MATE_USER_ID, String.valueOf(user.getId()));
//                    additionMessage.put(Oauth2Constants.MATE_USER_NAME, user.getUsername());
//                    additionMessage.put(Oauth2Constants.MATE_AVATAR, user.getAvatar());
//                    additionMessage.put(Oauth2Constants.MATE_ROLE_ID, String.valueOf(user.getRoleId()));
//                    additionMessage.put(Oauth2Constants.MATE_TYPE, user.getType());
//                    additionMessage.put(Oauth2Constants.MATE_TENANT_ID, user.getTenantId());
//                }
//                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
//                return oAuth2AccessToken;
//            }
//        };
//    }
//
//
//}
