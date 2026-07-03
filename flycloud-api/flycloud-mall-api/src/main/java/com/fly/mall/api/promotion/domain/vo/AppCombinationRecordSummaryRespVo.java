package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端拼团记录概要返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppCombinationRecordSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer AVATAR_COUNT = 7;

    private Long userCount;

    private List<String> avatars;

}
