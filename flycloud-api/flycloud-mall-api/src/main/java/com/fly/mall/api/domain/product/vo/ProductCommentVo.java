package com.fly.mall.api.domain.product.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 商品评价 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductCommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private List<Object> skuProperties;

    private Boolean visible;

    private Integer scores;

    private Integer descriptionScores;

    private Integer benefitScores;

    private String content;

    private List<String> picUrls;

    private Boolean replyStatus;

    private Long replyUserId;

    private String replyContent;

    private LocalDateTime replyTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
