package com.fly.system.api.file.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上传文件 Response VO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Schema(description = "上传文件 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文件访问地址")
    private String url;

    @Schema(description = "文件访问地址前缀")
    private String baseUrl;

    @Schema(description = "文件相对路径")
    private String path;

}
