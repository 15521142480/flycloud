package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 商品评价 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductCommentBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "userNickname")
    private String userNickname;

    @Schema(description = "userAvatar")
    private String userAvatar;

    @Schema(description = "anonymous")
    private Boolean anonymous;

    @Schema(description = "orderId")
    private Long orderId;

    @Schema(description = "orderItemId")
    private Long orderItemId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "spuName")
    private String spuName;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "skuPicUrl")
    private String skuPicUrl;

    @Schema(description = "skuProperties")
    private List<Object> skuProperties;

    @Schema(description = "visible")
    private Boolean visible;

    @Schema(description = "scores")
    private Integer scores;

    @Schema(description = "descriptionScores")
    private Integer descriptionScores;

    @Schema(description = "benefitScores")
    private Integer benefitScores;

    @Schema(description = "content")
    private String content;

    @Schema(description = "picUrls")
    private List<String> picUrls;

    @Schema(description = "replyStatus")
    private Boolean replyStatus;

    @Schema(description = "replyUserId")
    private Long replyUserId;

    @Schema(description = "replyContent")
    private String replyContent;

    @Schema(description = "replyTime")
    private LocalDateTime replyTime;

}
