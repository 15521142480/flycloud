package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费模板计费规则 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_delivery_express_template_charge", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "运费模板计费规则对象", description = "运费模板计费规则表")
public class DeliveryExpressTemplateCharge extends BaseEntity {

    @TableId
    private Long id;

    private Long templateId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> areaIds;

    private Integer chargeMode;

    private Double startCount;

    private Integer startPrice;

    private Double extraCount;

    private Integer extraPrice;

}
