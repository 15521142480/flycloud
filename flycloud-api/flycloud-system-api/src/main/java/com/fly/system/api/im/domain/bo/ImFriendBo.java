package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 好友业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 好友业务对象")
@Data
@Accessors(chain = true)
public class ImFriendBo {

    @Schema(description = "好友用户编号", example = "1024")
    private Long friendUserId;

    @Schema(description = "是否免打扰")
    private Boolean silent;

    @Schema(description = "好友备注名")
    private String displayName;

    @Schema(description = "是否置顶")
    private Boolean pinned;

}
