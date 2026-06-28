package com.fly.mall.api.domain.promotion;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拼团记录 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_combination_record")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "拼团记录对象", description = "拼团记录表")
public class CombinationRecord extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    private Integer combinationPrice;

    private Long spuId;

    private String spuName;

    private String picUrl;

    private Long skuId;

    private Integer count;

    private Long userId;

    private String nickname;

    private String avatar;

    private Long headId;

    private Integer status;

    private Long orderId;

    private Integer userSize;

    private Integer userCount;

    private Boolean virtualGroup;

    private LocalDateTime expireTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
