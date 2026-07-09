package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 好友申请业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 好友申请业务对象")
@Data
@Accessors(chain = true)
public class ImFriendRequestBo {

    @Schema(description = "接收人用户编号", example = "1024")
    private Long toUserId;

    @Schema(description = "申请理由")
    private String applyContent;

    @Schema(description = "好友备注名")
    private String displayName;

    @Schema(description = "添加来源")
    private Integer addSource;

}
