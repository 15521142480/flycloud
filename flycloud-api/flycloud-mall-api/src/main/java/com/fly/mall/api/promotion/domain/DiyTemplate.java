package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 装修模板 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_diy_template", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "装修模板对象", description = "装修模板表")
public class DiyTemplate extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Boolean used;

    private LocalDateTime usedTime;

    private String remark;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> previewPicUrls;

    private String property;

}
