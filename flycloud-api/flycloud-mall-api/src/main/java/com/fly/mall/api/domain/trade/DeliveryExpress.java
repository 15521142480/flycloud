package com.fly.mall.api.domain.trade;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 快递公司 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_delivery_express")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "快递公司对象", description = "快递公司表")
public class DeliveryExpress extends BaseEntity {

    @TableId
    private Long id;

    private String code;

    private String name;

    private String logo;

    private Integer sort;

    private Integer status;

}
