package com.fly.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.database.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置对象 sys_config
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 参数主键
     */
    @TableId(value = "config_id")
    private Long configId;
    /**
     * 参数名称
     */
    private String configName;
    /**
     * 参数键名
     */
    private String configKey;
    /**
     * 参数键值
     */
    private String configValue;
    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
    /**
     * 备注
     */
    private String remark;

}
