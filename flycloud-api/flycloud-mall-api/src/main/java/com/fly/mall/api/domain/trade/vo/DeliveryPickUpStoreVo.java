package com.fly.mall.api.domain.trade.vo;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 自提门店 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryPickUpStoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private List<Long> verifyUserIds;

    private Integer status;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
