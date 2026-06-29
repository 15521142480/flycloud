package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级记录业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员等级记录业务对象")
public class MemberLevelRecordBo extends BaseEntity {

    private Long id;
    private Long userId;
    private Long levelId;
    private Integer level;

}
