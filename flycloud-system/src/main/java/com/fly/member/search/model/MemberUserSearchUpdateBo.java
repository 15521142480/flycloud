package com.fly.member.search.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 会员用户修改并异步同步 ES 的测试请求。
 */
@Data
public class MemberUserSearchUpdateBo {

    /**
     * 待修改的会员用户主键。
     */
    @NotNull(message = "会员用户 ID 不能为空")
    private Long id;

    /**
     * 修改后的会员昵称。
     */
    private String nickname;

    /**
     * 修改后的会员真实姓名。
     */
    private String name;

    /**
     * 修改后的会员备注。
     */
    private String mark;

    /**
     * 修改后的会员账号状态。
     */
    private Integer status;

    /**
     * 修改后的岗位编号集合。
     */
    private List<Long> postIds;
}
