package com.fly.member.search.model;

import lombok.Data;

/**
 * 会员用户 ES 分页检索条件。排序字段使用白名单，避免将请求参数直接拼为 DSL。
 */
@Data
public class MemberUserSearchPageBo {

    /**
     * 会员用户主键；传入时执行精确查询。
     */
    private Long id;

    /**
     * 会员手机号；传入时执行精确查询。
     */
    private String mobile;

    /**
     * 会员邮箱；传入时执行精确查询。
     */
    private String email;

    /**
     * 会员昵称、姓名和备注的全文检索关键字。
     */
    private String keyword;

    /**
     * 会员账号状态。
     */
    private Integer status;

    /**
     * 会员分组编号。
     */
    private Long groupId;

    /**
     * 会员等级编号。
     */
    private Long levelId;

    /**
     * 当前页码，从 1 开始。
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数。
     */
    private Integer pageSize = 10;

    /**
     * createTime、updateTime、id 之一。
     */
    private String sortField = "createTime";
    /**
     * ASC 或 DESC。
     */
    private String sortOrder = "DESC";
}
