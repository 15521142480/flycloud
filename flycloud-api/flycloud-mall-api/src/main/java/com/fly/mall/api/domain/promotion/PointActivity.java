package com.fly.mall.api.domain.promotion;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分商城活动 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_point_activity")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "积分商城活动对象", description = "积分商城活动表")
public class PointActivity extends BaseEntity {

    @TableId
    private Long id;

    private Long spuId;

    private Integer status;

    private String remark;

    private Integer sort;

    private Integer stock;

    private Integer totalStock;

}
