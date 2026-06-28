package com.fly.mall.api.domain.promotion;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拼团活动 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_combination_activity")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "拼团活动对象", description = "拼团活动表")
public class CombinationActivity extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Long spuId;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer userSize;

    private Boolean virtualGroup;

    private Integer status;

    private Integer limitDuration;

}
