package com.fly.system.api.file.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件配置 Response VO。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Schema(description = "文件配置 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileConfigRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文件存储基础路径")
    private String basePath;

    @Schema(description = "文件访问基础地址")
    private String baseUrl;

}
