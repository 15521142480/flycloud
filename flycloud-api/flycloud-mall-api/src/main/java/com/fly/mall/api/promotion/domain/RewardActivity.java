package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 满减送活动 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_reward_activity", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "满减送活动对象", description = "满减送活动表")
public class RewardActivity extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String remark;

    private Integer conditionType;

    private Integer productScope;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> productScopeValues;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Rule> rules;

//    private Integer limit;
//
//    private Integer discountPrice;
//
//    private Boolean freeDelivery;
//
//    private Integer point;
//
//    @TableField(typeHandler = JacksonTypeHandler.class)
//    private Map<Long, Integer> giveCouponTemplateCounts;

    /**
     * 满减送活动优惠规则。
     *
     * @author lxs
     * @date 2026-07-02
     */
    @Data
    public static class Rule implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 优惠门槛，按活动条件表示金额分值或商品件数。
         */
        private Integer limit;

        /**
         * 优惠金额，单位分。
         */
        private Integer discountPrice;

        /**
         * 是否包邮。
         */
        private Boolean freeDelivery;

        /**
         * 赠送积分。
         */
        private Integer point;

        /**
         * 赠送优惠券模板数量，key 为模板编号，value 为数量。
         */
        private Map<Long, Integer> giveCouponTemplateCounts;
    }

}
