package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 装修模板 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DiyTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Boolean used;

    private LocalDateTime usedTime;

    private String remark;

    private List<String> previewPicUrls;

    private String property;

    /**
     * 模板下的装修页面列表。
     */
    private List<DiyPageVo> pages;

    /**
     * 首页装修属性 JSON。
     */
    private String home;

    /**
     * 我的页面装修属性 JSON。
     */
    private String user;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
