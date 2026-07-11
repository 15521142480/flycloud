package com.fly.system.api.im.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Schema(description = "管理后台 - 群活跃通话 Response VO；不含 token，胶囊条仅展示用")
@Data
@Accessors(chain = true)
public class ImRtcGroupCallVo {

    @Schema(description = "业务通话编号")
    private String room;

    @Schema(description = "群编号")
    @JsonLongId
    private Long groupId;

    @Schema(description = "媒体类型")
    private Integer mediaType; // 参见 ImCallMediaTypeEnum 枚举类

    @Schema(description = "发起人编号")
    @JsonLongId
    private Long inviterId;

    @Schema(description = "已加入房间的用户编号集合")
    @JsonLongId
    private Set<Long> joinedUserIds;

    @Schema(description = "被邀请池；用于胶囊条展开时显示待加入头像")
    @JsonLongId
    private Set<Long> inviteeIds;

}
