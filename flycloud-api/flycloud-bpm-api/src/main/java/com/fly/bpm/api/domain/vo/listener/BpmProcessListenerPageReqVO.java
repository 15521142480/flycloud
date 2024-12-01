package com.fly.bpm.api.domain.vo.listener;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.validate.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - BPM 流程监听器分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessListenerPageReqVO extends PageBo {

    @Schema(description = "监听器名字", example = "赵六")
    private String name;

    @Schema(description = "监听器类型", example = "execution")
    private String type;

    @Schema(description = "监听事件", example = "start")
    private String event;

    @Schema(description = "状态", example = "1")
    @InEnum(StatusEnum.class)
    private Integer status;

}