package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * IM 频道消息业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 频道消息业务对象")
@Data
@Accessors(chain = true)
public class ImChannelMessageBo {

    @Schema(description = "素材编号", example = "1001")
    private Long materialId;

    @Schema(description = "接收用户编号列表", example = "[1, 2]")
    private List<Long> receiverUserIds;

}
