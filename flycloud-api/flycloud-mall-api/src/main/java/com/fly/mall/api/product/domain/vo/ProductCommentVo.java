package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 商品评价 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductCommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long userId;

    private String userNickname;

    private String userAvatar;

    private Boolean anonymous;

    @JsonLongId
    private Long orderId;

    @JsonLongId
    private Long orderItemId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    @JsonLongId
    private Long skuId;

    private String skuPicUrl;

    private List<Object> skuProperties;

    private Boolean visible;

    private Integer scores;

    private Integer descriptionScores;

    private Integer benefitScores;

    private String content;

    private List<String> picUrls;

    private Boolean replyStatus;

    @JsonLongId
    private Long replyUserId;

    private String replyContent;

    private LocalDateTime replyTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
