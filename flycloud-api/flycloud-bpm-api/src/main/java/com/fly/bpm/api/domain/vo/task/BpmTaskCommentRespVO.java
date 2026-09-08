package com.fly.bpm.api.domain.vo.task;

import com.fly.bpm.api.domain.vo.user.SysUserBpmVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务评论 Response VO")
@Data
@Accessors(chain = true)
public class BpmTaskCommentRespVO implements Serializable {

    @Schema(description = "评论编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "评论类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    private String type;

    @Schema(description = "评论类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "转派")
    private String typeName;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[张三]将任务转派给[李四]，转派理由为:请协助处理")
    private String message;

    @Schema(description = "评论时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "操作人")
    private SysUserBpmVO user;

}
