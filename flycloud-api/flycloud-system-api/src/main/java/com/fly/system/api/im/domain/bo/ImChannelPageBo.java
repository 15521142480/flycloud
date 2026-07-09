package com.fly.system.api.im.domain.bo;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IM 频道分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImChannelPageBo extends PageBo {

    @Schema(description = "频道业务码", example = "system_notice")
    private String code;

    @Schema(description = "频道名称", example = "系统")
    private String name;

    @Schema(description = "状态", example = "0")
    private Integer status; // 参见 CommonStatusEnum 枚举类

}
