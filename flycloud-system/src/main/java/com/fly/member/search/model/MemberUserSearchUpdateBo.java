package com.fly.member.search.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 会员用户修改并异步同步 ES 的测试请求。
 */
@Data
public class MemberUserSearchUpdateBo {

    @NotNull(message = "会员用户 ID 不能为空")
    private Long id;
    private String nickname;
    private String name;
    private String mark;
    private Integer status;
    private List<Long> postIds;
}
