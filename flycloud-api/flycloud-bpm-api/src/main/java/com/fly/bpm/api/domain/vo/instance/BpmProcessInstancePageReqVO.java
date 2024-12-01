package com.fly.bpm.api.domain.vo.instance;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.enums.bpm.BpmProcessInstanceStatusEnum;
import com.fly.common.utils.DateUtils;
import com.fly.common.validate.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - 流程实例分页 Request VO")
@Data
public class BpmProcessInstancePageReqVO extends PageBo {

    @Schema(description = "流程名称", example = "fly")
    private String name;

    @Schema(description = "流程定义的标识", example = "2048")
    private String processDefinitionKey; // 精准匹配

    @Schema(description = "流程实例的状态", example = "1")
    @InEnum(BpmProcessInstanceStatusEnum.class)
    private Integer status;

    @Schema(description = "流程分类", example = "1")
    private String category;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

    @Schema(description = "发起用户编号", example = "1024")
    private Long startUserId; // 注意，只有在【流程实例】菜单，才使用该参数

}
