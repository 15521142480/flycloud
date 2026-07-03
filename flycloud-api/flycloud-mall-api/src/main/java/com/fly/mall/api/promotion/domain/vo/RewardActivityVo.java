package com.fly.mall.api.promotion.domain.vo;

import com.fly.mall.api.promotion.domain.RewardActivity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 满减送活动 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class RewardActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String remark;

    private Integer conditionType;

    private Integer productScope;

    private List<Long> productScopeValues;

    private List<RewardActivity.Rule> rules;

    private Integer limit;

    private Integer discountPrice;

    private Boolean freeDelivery;

    private Integer point;

    private Map<Long, Integer> giveCouponTemplateCounts;

    /**
     * 当前活动命中的商品 SPU 编号集合。
     */
    private List<Long> spuIds;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
