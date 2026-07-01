package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 移动端 - 商品评价创建请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppTradeOrderItemCommentCreateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean anonymous;

    private Long orderItemId;

    private Integer descriptionScores;

    private Integer benefitScores;

    private String content;

    private List<String> picUrls;
}
