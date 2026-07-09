package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 加群申请业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 加群申请业务对象")
@Data
@Accessors(chain = true)
public class ImGroupRequestBo {

    @Schema(description = "群编号", example = "1024")
    private Long groupId;

    @Schema(description = "申请理由")
    private String applyContent;

    @Schema(description = "加入来源")
    private Integer addSource;

}
