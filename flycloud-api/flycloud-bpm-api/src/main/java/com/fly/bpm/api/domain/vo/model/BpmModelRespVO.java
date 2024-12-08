package com.fly.bpm.api.domain.vo.model;

import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.api.domain.vo.user.SysUserBpmVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 流程模型 Response VO")
@Data
@Accessors(chain = true)
public class BpmModelRespVO extends BpmModelMetaInfoVO implements Serializable {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "process_yudao")
    private String key;

    @Schema(description = "流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "fly")
    private String name;

//    @Schema(description = "流程图标", example = "https://www.iocoder.cn/yudao.jpg")
//    private String icon;

    @Schema(description = "流程分类编码", example = "1")
    private String category;
    @Schema(description = "流程分类名字", example = "请假")
    private String categoryName;

    @Schema(description = "表单名字", example = "请假表单")
    private String formName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "BPMN XML", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bpmnXml;

    @Schema(description = "可发起的用户数组")
    private List<SysUserBpmVO> startUsers;

    /**
     * 最新部署的流程定义
     */
    private BpmProcessDefinitionRespVO processDefinition;

}
