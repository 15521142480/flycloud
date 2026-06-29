package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员收件地址视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class MemberAddressVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String name;

    private String mobile;

    private Long areaId;

    private String detailAddress;

    private Boolean defaultStatus;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
