package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 积分商城活动 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PointActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    private Integer status;

    private String remark;

    private Integer sort;

    private Integer stock;

    private Integer totalStock;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
