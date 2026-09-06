package com.fly.ai.common.tool.model;

import java.time.LocalDateTime;

/**
 * 提供给模型的商城会员公共信息。
 * <p>
 * 仅供已通过订单授权后的“查询该订单由谁下单”场景使用。故意排除手机号、邮箱、密码、登录 IP、收货地址等
 * 个人敏感字段，且不与后台 {@code sys_user} 用户信息混用。
 *
 * @param memberUserId 商城会员用户编号
 * @param nickname 会员昵称
 * @param name 会员姓名或展示名称
 * @param status 会员状态
 * @param createTime 注册时间
 * @author lxs
 * @date 2026-09-06
 */
public record AiToolPublicMemberInfo(
        Long memberUserId,
        String nickname,
        String name,
        Integer status,
        LocalDateTime createTime) {
}
