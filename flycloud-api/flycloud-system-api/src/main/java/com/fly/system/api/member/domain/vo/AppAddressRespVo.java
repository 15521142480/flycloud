package com.fly.system.api.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 会员收件地址返回对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "移动端 - 会员收件地址返回对象")
public class AppAddressRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "地址编号")
    private Long id;

    @Schema(description = "收件人名称")
    private String name;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "地区编号")
    private Long areaId;

    @Schema(description = "地区名称")
    private String areaName;

    @Schema(description = "收件详细地址")
    private String detailAddress;

    @Schema(description = "是否默认地址")
    private Boolean defaultStatus;

}
