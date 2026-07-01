package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员分组视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberGroupVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String remark;
    private Integer status;
    private Boolean isDeleted;
    private LocalDateTime createTime;
}
