package com.fly.mall.api.domain.promotion;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 限时折扣活动 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_discount_activity")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "限时折扣活动对象", description = "限时折扣活动表")
public class DiscountActivity extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String remark;

}
