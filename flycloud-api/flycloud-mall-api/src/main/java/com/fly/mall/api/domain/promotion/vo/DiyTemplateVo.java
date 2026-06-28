package com.fly.mall.api.domain.promotion.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 装修模板 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
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

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
