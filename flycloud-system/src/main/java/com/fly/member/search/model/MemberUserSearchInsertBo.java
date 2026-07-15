package com.fly.member.search.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 会员用户新增并异步建立 ES 投影的请求对象。
 *
 * <p>该对象只承载新增会员所需的业务字段。会员实体不会作为 RocketMQ 消息发送，
 * ES 消费端始终依据新增后的会员主键回查 MySQL 权威数据。</p>
 */
@Data
public class MemberUserSearchInsertBo {

    /**
     * 会员手机号。
     */
    @NotBlank(message = "手机号不能为空")
    @Size(max = 32, message = "手机号长度不能超过 32 个字符")
    private String mobile;

    /**
     * 会员邮箱。
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    /**
     * 会员昵称。
     */
    @NotBlank(message = "会员昵称不能为空")
    @Size(max = 64, message = "会员昵称长度不能超过 64 个字符")
    private String nickname;

    /**
     * 会员真实姓名。
     */
    @Size(max = 64, message = "会员真实姓名长度不能超过 64 个字符")
    private String name;

    /**
     * 账号状态；0 为启用，1 为停用。
     */
    private Integer status;

    /**
     * 会员备注。
     */
    @Size(max = 512, message = "会员备注长度不能超过 512 个字符")
    private String mark;

    /**
     * 会员标签编号集合。
     */
    private List<Long> tagIds;

    /**
     * 岗位编号集合。
     */
    private List<Long> postIds;

    /**
     * 会员等级编号。
     */
    private Long levelId;

    /**
     * 会员分组编号。
     */
    private Long groupId;
}
