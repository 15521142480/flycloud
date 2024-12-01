package com.fly.bpm.api.domain.vo.form;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 动态表单分页 Request VO")
@Data
public class BpmFormPageReqVO extends PageBo {

    @Schema(description = "表单名称", example = "fly")
    private String name;

}
