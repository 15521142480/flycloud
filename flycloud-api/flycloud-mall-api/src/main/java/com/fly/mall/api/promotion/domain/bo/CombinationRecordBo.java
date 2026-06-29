package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团记录 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CombinationRecordBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "combinationPrice")
    private Integer combinationPrice;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "spuName")
    private String spuName;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "count")
    private Integer count;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "nickname")
    private String nickname;

    @Schema(description = "avatar")
    private String avatar;

    @Schema(description = "headId")
    private Long headId;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "orderId")
    private Long orderId;

    @Schema(description = "userSize")
    private Integer userSize;

    @Schema(description = "userCount")
    private Integer userCount;

    @Schema(description = "virtualGroup")
    private Boolean virtualGroup;

    @Schema(description = "expireTime")
    private LocalDateTime expireTime;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

}
