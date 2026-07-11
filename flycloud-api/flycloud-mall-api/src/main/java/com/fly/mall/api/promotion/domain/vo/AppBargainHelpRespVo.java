package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端砍价助力返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainHelpRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long userId;

    private String nickname;

    private String avatar;

    private Integer reducePrice;

    private LocalDateTime createTime;

}
