package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员收件地址。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_address")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员收件地址对象", description = "会员收件地址")
public class MemberAddress extends BaseEntity {

    @TableId
    @Schema(description = "地址编号")
    private Long id;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "收件人名称")
    private String name;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "地区编号")
    private Long areaId;

    @Schema(description = "收件详细地址")
    private String detailAddress;

    @Schema(description = "是否默认地址")
    private Boolean defaultStatus;

}
