package com.fly.system.api.file.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.*;


/**
 * 文件配置表
 *
 */
@TableName(value = "file_config", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileConfig extends BaseEntity {

    /**
     * 配置编号，数据库自增
     */
    private Long id;
    /**
     * 配置名
     */
    private String name;
    /**
     * 存储器
     *
     * 枚举 {@link } // FileStorageEnum
     */
    private Integer storage;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否为主配置
     *
     * 由于我们可以配置多个文件配置，默认情况下，使用主配置进行文件的上传
     */
    private Boolean master;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 支付渠道配置
     */
//    @TableField(typeHandler = FileClientConfigTypeHandler.class)
//    private FileClientConfig config;

    private String config;

}
