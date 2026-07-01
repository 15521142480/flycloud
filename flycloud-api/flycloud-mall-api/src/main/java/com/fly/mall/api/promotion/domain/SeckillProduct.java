package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 秒杀商品 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_seckill_product", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "秒杀商品对象", description = "秒杀商品表")
public class SeckillProduct extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> configIds;

    private Long spuId;

    private Long skuId;

    private Integer seckillPrice;

    private Integer stock;

    private Integer activityStatus;

    private LocalDateTime activityStartTime;

    private LocalDateTime activityEndTime;

}
