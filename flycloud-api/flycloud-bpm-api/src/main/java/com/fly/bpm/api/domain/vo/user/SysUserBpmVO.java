package com.fly.bpm.api.domain.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "用户精简信息 VO")
@Data
public class SysUserBpmVO implements Serializable {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "真名", requiredMode = Schema.RequiredMode.REQUIRED, example = "fly")
    private String realName;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "fly")
    private String name;

    @Schema(description = "用户头像", example = "https://www.iocoder.cn/1.png")
    private String avatar;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long deptId;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "研发部")
    private String deptName;

}