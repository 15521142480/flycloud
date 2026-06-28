package com.fly.mall.api.domain.promotion;

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
 * 装修页面 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_diy_page", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "装修页面对象", description = "装修页面表")
public class DiyPage extends BaseEntity {

    @TableId
    private Long id;

    private Long templateId;

    private String name;

    private String remark;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> previewPicUrls;

    private String property;

}
