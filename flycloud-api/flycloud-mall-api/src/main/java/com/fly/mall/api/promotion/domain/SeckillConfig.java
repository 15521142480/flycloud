package com.fly.mall.api.promotion.domain;

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
 * 秒杀时段配置 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_seckill_config", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "秒杀时段配置对象", description = "秒杀时段配置表")
public class SeckillConfig extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String startTime;

    private String endTime;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> sliderPicUrls;

    private Integer status;

}
