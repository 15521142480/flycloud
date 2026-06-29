package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价活动 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_bargain_activity")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "砍价活动对象", description = "砍价活动表")
public class BargainActivity extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Long spuId;

    private Long skuId;

    private Integer bargainFirstPrice;

    private Integer bargainMinPrice;

    private Integer stock;

    private Integer totalStock;

    private Integer helpMaxCount;

    private Integer bargainCount;

    private Integer totalLimitCount;

    private Integer randomMinPrice;

    private Integer randomMaxPrice;

}
