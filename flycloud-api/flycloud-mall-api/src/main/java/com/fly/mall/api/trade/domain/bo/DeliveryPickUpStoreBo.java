package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

/**
 * 自提门店 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DeliveryPickUpStoreBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "introduction")
    private String introduction;

    @Schema(description = "phone")
    private String phone;

    @Schema(description = "areaId")
    private Integer areaId;

    @Schema(description = "detailAddress")
    private String detailAddress;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "openingTime")
    private LocalTime openingTime;

    @Schema(description = "closingTime")
    private LocalTime closingTime;

    @Schema(description = "latitude")
    private Double latitude;

    @Schema(description = "longitude")
    private Double longitude;

    @Schema(description = "verifyUserIds")
    private List<Long> verifyUserIds;

    @Schema(description = "status")
    private Integer status;

}
