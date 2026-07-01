package com.fly.system.api.file.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fly.system.api.file.domain.FileConfig;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件 vo
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Data
@ExcelIgnoreUnannotated
public class FileVo implements Serializable {


    private static final long serialVersionUID = 1L;


    /**
     * 编号，数据库自增
     */
    private Long id;
    /**
     * 配置编号
     *
     * 关联 {@link FileConfig#getId()}
     */
    private Long configId;
    /**
     * 原文件名
     */
    private String name;
    /**
     * 路径，即文件名
     */
    private String path;
    /**
     * 访问地址
     */
    private String url;
    /**
     * 文件的 MIME 类型，例如 "application/octet-stream"
     */
    private String type;
    /**
     * 文件大小
     */
    private Long size;


}
