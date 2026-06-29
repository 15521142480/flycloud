package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 装修页面 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DiyPageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long templateId;

    private String name;

    private String remark;

    private List<String> previewPicUrls;

    private String property;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
