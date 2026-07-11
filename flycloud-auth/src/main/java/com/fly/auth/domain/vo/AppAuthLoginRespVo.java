package com.fly.auth.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 登录 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppAuthLoginRespVo {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @JsonLongId
    private Long userId;

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "happy")
    private String accessToken;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String refreshToken;

    /**
     * jti 唯一编号
     *
     * token = 一整张身份证
     * jti   = 身份证编号
     */
    @Schema(description = "刷新令牌")
    private String jti;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresTime;

    /**
     * 仅社交登录、社交绑定时会返回
     *
     * 为什么需要返回？微信公众号、微信小程序支付需要传递 openid 给支付接口
     */
    @Schema(description = "社交用户 openid", example = "qq768")
    private String openid;

}
