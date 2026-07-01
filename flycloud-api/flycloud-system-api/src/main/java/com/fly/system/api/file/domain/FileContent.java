package com.fly.system.api.file.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.*;

/**
 * 文件内容表
 *
 * 专门用于存储 {@link } 的文件内容 DBFileClient
 *
 */
@TableName("file_content")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileContent extends BaseEntity {

    /**
     * 编号，数据库自增
     */
    @TableId
    private Long id;
    /**
     * 配置编号
     *
     * 关联 {@link FileConfig#getId()}
     */
    private Long configId;
    /**
     * 路径，即文件名
     */
    private String path;
    /**
     * 文件内容
     */
    private byte[] content;

}
