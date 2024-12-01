package com.fly.bpm.api.domain.vo.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "管理后台 - 用户组 Response VO")
@Data
public class BpmUserGroupRespVO implements Serializable {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "组名", requiredMode = Schema.RequiredMode.REQUIRED, example = "fly")
    private String name;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "fly源码")
    private String description;

    @Schema(description = "成员编号数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "1,2,3")
    private Set<Long> userIds;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
