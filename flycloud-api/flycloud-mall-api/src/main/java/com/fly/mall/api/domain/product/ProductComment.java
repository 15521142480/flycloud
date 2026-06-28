package com.fly.mall.api.domain.product;

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
 * 商品评价 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_comment", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品评价对象", description = "商品评价表")
public class ProductComment extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private String userNickname;

    private String userAvatar;

    private Boolean anonymous;

    private Long orderId;

    private Long orderItemId;

    private Long spuId;

    private String spuName;

    private Long skuId;

    private String skuPicUrl;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Object> skuProperties;

    private Boolean visible;

    private Integer scores;

    private Integer descriptionScores;

    private Integer benefitScores;

    private String content;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> picUrls;

    private Boolean replyStatus;

    private Long replyUserId;

    private String replyContent;

    private LocalDateTime replyTime;

}
