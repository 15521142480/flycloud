package com.fly.test.member.domain.bo;

import lombok.Data;

/** 会员用户 ES 分页检索条件。排序字段使用白名单，避免将请求参数直接拼为 DSL。 */
@Data
public class MemberUserSearchPageBo {

    private Long id;
    private String mobile;
    private String email;
    private String keyword;
    private Integer status;
    private Long groupId;
    private Long levelId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    /** createTime、updateTime、id 之一。 */
    private String sortField = "createTime";
    /** ASC 或 DESC。 */
    private String sortOrder = "DESC";
}
