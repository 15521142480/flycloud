package com.fly.system.api.im.domain.vo.admin.manager.sensitiveword;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.system.api.im.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IM 敏感词新增 / 修改 Request VO")
@Data
@Accessors(chain = true)
public class ImSensitiveWordSaveReqVo {

    @Schema(description = "编号（修改时必填）", example = "1024")
    private Long id;

    @Schema(description = "敏感词", requiredMode = Schema.RequiredMode.REQUIRED, example = "敏感词内容")
    @NotBlank(message = "敏感词不能为空")
    @Size(max = 64, message = "敏感词长度不能超过 64")
    private String word;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(value = CommonStatusEnum.class, message = "状态必须是 {value}")
    private Integer status; // 参见 CommonStatusEnum 枚举类（0 启用 / 1 禁用）

}
