package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价助力 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_bargain_help")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "砍价助力对象", description = "砍价助力表")
public class BargainHelp extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    private Long recordId;

    private Long userId;

    private Integer reducePrice;

}
