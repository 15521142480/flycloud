package com.fly.im.controller.admin.manager.face.vo.item;

import com.fly.im.framework.enums.CommonStatusEnum;
import com.fly.common.domain.bo.PageBo;
import com.fly.im.framework.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IM 表情包项分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImFacePackItemPageReqVO extends PageBo {

    @Schema(description = "所属表情包编号", example = "1024")
    private Long packId;

    @Schema(description = "表情名，模糊匹配", example = "狗")
    private String name;

    @Schema(description = "状态", example = "0")
    @InEnum(value = CommonStatusEnum.class, message = "状态必须是 {value}")
    private Integer status; // 参见 CommonStatusEnum 枚举类

}
