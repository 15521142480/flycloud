package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员等级业务对象")
public class MemberLevelBo extends BaseEntity {

    private Long id;
    private String name;
    private Integer level;
    private Integer experience;
    private Integer discountPercent;
    private String icon;
    private String backgroundUrl;
    private Integer status;

}
