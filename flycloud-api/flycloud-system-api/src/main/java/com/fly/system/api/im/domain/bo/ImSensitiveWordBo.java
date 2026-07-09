package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 敏感词业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 敏感词业务对象")
@Data
@Accessors(chain = true)
public class ImSensitiveWordBo {

    @Schema(description = "敏感词编号", example = "1001")
    private Long id;

    @Schema(description = "敏感词")
    private String word;

    @Schema(description = "状态")
    private Integer status;

}
