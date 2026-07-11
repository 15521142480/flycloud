package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端拼团记录详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppCombinationRecordDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private AppCombinationRecordRespVo headRecord;

    private List<AppCombinationRecordRespVo> memberRecords;

    @JsonLongId
    private Long orderId;

}
