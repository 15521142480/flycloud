package com.fly.system.api.member.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 会员收件地址创建请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "移动端 - 会员收件地址创建请求对象")
public class AppAddressCreateReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收件人名称")
    @NotNull(message = "收件人名称不能为空")
    private String name;

    @Schema(description = "手机号")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "地区编号")
    @NotNull(message = "地区编号不能为空")
    private Long areaId;

    @Schema(description = "收件详细地址")
    @NotNull(message = "收件详细地址不能为空")
    private String detailAddress;

    @Schema(description = "是否默认地址")
    @NotNull(message = "是否默认地址不能为空")
    private Boolean defaultStatus;

}
