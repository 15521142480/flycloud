package com.fly.system.api.im.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * IM 通话参与者视图对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Data
public class ImRtcParticipantVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long callId;

    private String room;

    @JsonLongId
    private Long userId;

    private Integer role;

    private Integer status;

    private LocalDateTime inviteTime;

    private LocalDateTime acceptTime;

    private LocalDateTime leaveTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
