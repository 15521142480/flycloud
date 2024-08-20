package com.fly.common.seata.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Seata配置
 *
 * @author aaronuu
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "seata")
public class SeataProperties {

  private String applicationId;

  private String txServiceGroup;

}
