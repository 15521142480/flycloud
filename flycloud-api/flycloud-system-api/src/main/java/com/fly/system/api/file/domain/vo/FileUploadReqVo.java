package com.fly.system.api.file.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fly.common.utils.file.FilePathUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件 Request Vo
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Schema(description = "管理后台 - 上传文件 Request VO")
@Data
public class FileUploadReqVo {

    @Schema(description = "文件附件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件附件不能为空")
    private MultipartFile file;

    @Schema(description = "文件目录", example = "XXX/YYY")
    private String directory;

    @AssertTrue(message = "文件目录不正确")
    @JsonIgnore
    public boolean isDirectoryValid() {
        return isDirectoryValid(directory);
    }

    public static boolean isDirectoryValid(String directory) {
        return FilePathUtils.isDirectoryValid(directory);
    }
}
