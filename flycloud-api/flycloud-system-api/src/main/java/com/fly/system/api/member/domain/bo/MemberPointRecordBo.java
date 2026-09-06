package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 会员积分记录业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员积分记录业务对象")
public class MemberPointRecordBo extends BaseEntity {

    private Long id;
    private Long userId;
    private String bizId;
    private Integer bizType;
    private String title;
    private Integer point;

    @Schema(description = "是否增加积分")
    private Boolean addStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间范围")
    private LocalDateTime[] createTimeRange;

}
