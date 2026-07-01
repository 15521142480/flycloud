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
 * 秒杀活动 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_seckill_activity", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "秒杀活动对象", description = "秒杀活动表")
public class SeckillActivity extends BaseEntity {

    @TableId
    private Long id;

    private Long spuId;

    private String name;

    private Integer status;

    private String remark;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer sort;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> configIds;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private Integer stock;

    private Integer totalStock;

}
