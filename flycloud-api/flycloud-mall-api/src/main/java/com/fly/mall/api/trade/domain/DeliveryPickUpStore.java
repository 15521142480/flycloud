package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自提门店 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "trade_delivery_pick_up_store", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "自提门店对象", description = "自提门店表")
public class DeliveryPickUpStore extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String introduction;

    private String phone;

    private Integer areaId;

    private String detailAddress;

    private String logo;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private Double latitude;

    private Double longitude;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> verifyUserIds;

    private Integer status;

}
