package com.fly.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.exception.TokenException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 自定义client表，并将数据缓存到redis，处理缓存优化
 * 需要在管理平台修改client数据时，同步至redis
 *
 */

@Slf4j
@Setter
@Service
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

	@Resource
	private DataSource dataSource;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	public ClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Bean
	@Primary
	public ClientDetailsServiceImpl clientDetailService() {
		ClientDetailsServiceImpl clientDetailsService = new ClientDetailsServiceImpl(dataSource);
		clientDetailsService.setRedisTemplate(redisTemplate);
		return clientDetailsService;
	}

	/**
	 * 从redis里读取ClientDetails
	 *
	 * @param clientId 客户端ID
	 * @return ClientDetails
	 * @throws InvalidClientException 非法客户端异常
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {

		ClientDetails clientDetails = (ClientDetails) redisTemplate.opsForValue().get(clientKey(clientId));
		if (ObjectUtil.isEmpty(clientDetails)) {
			clientDetails = getCacheClient(clientId);
		}
		clientDetails.getAuthorizedGrantTypes().add(Oauth2Constants.CLIENT_CREDENTIALS);
		return clientDetails;
	}


	/**
	 * 自定义语句查询，并将数据同步至redis
	 *
	 * @param clientId 客户端ID
	 * @return ClientDetails
	 */
	private ClientDetails getCacheClient(String clientId) {

		ClientDetails clientDetails = null;
		try {

			clientDetails = super.loadClientByClientId(clientId);
			if (ObjectUtil.isNotEmpty(clientDetails)) {
				redisTemplate.opsForValue().set(clientKey(clientId), clientDetails);
				log.debug("Cache clientId:{}, clientDetails:{}", clientId, clientDetails);
			}
		} catch (Exception e) {

			log.error("Exception for clientId:{}, message:{}", clientId, e.getMessage());
			log.error("客户端key或secret不存在!", e);
			throw new TokenException("客户端key或secret不存在!");
		}
		return clientDetails;
	}


	/**
	 * 缓存key
	 */
	private String clientKey(String clientId) {
		return Oauth2Constants.CLIENT_TABLE + ":" + clientId;
	}
}
