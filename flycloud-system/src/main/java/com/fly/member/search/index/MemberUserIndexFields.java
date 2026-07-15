package com.fly.member.search.index;

/** 会员用户索引字段名。禁止业务查询散落字符串字段。 */
public final class MemberUserIndexFields {

    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String GROUP_ID = "groupId";
    public static final String LEVEL_ID = "levelId";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String NICKNAME = "nickname";
    public static final String NAME = "name";
    public static final String MARK = "mark";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    private MemberUserIndexFields() {
    }
}
