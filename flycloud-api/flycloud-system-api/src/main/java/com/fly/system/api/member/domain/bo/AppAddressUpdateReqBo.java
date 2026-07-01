package com.fly.system.api.member.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 移动端 - 会员收件地址更新请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "移动端 - 会员收件地址更新请求对象")
public class AppAddressUpdateReqBo extends AppAddressCreateReqBo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "地址编号")
    @NotNull(message = "地址编号不能为空")
    private Long id;

}
