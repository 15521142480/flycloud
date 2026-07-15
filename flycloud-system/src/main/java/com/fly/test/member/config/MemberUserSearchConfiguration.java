package com.fly.test.member.config;

import com.fly.test.member.repository.MemberUserSearchRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/** 仅在 ES 启用时注册会员搜索 Repository。 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "flycloud.elasticsearch", name = "enabled", havingValue = "true")
@EnableElasticsearchRepositories(basePackageClasses = MemberUserSearchRepository.class)
public class MemberUserSearchConfiguration {
}
