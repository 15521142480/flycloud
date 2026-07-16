package com.fly.member.search.index;

/**
 * 会员用户索引字段名。禁止业务查询散落字符串字段。
 */
public final class MemberUserIndexFields {

    /**
     * ES 文档主键字段。
     */
    public static final String ID = "id";

    /**
     * 会员状态字段。
     */
    public static final String STATUS = "status";

    /**
     * 会员分组编号字段。
     */
    public static final String GROUP_ID = "groupId";

    /**
     * 会员等级编号字段。
     */
    public static final String LEVEL_ID = "levelId";

    /**
     * 会员手机号字段。
     */
    public static final String MOBILE = "mobile";

    /**
     * 会员邮箱字段。
     */
    public static final String EMAIL = "email";

    /**
     * 会员昵称字段。
     */
    public static final String NICKNAME = "nickname";

    /**
     * 会员真实姓名字段。
     */
    public static final String NAME = "name";

    /**
     * 会员备注字段。
     */
    public static final String MARK = "mark";

    /**
     * 会员创建时间字段。
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 会员最后修改时间字段。
     */
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 工具类禁止实例化。
     */
    private MemberUserIndexFields() {
    }
}
