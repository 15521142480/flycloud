package com.fly.ai.common.tool.model;

import java.util.Date;

/**
 * 提供给模型的公共用户信息。
 * <p>
 * 故意排除密码、邮箱、手机号、头像等个人敏感字段。
 *
 * @param userId 用户编号
 * @param account 登录账号
 * @param name 用户昵称
 * @param realName 真实姓名
 * @param deptId 部门编号
 * @param deptName 部门名称
 * @param status 用户状态
 * @param createTime 创建时间
 * @author lxs
 * @date 2026-08-27
 */
public record AiToolPublicUserInfo(
        Long userId,
        String account,
        String name,
        String realName,
        Long deptId,
        String deptName,
        Integer status,
        Date createTime) {
}
