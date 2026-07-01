package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拼团商品 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_combination_product")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "拼团商品对象", description = "拼团商品表")
public class CombinationProduct extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer combinationPrice;

    private Integer activityStatus;

    private LocalDateTime activityStartTime;

    private LocalDateTime activityEndTime;

}
