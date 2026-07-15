package com.fly.member.search.query;

import com.fly.member.search.model.MemberUserSearchPageBo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** MemberUser 查询构造器单元测试。 */
class MemberUserSearchPageBoBuilderTest {

    private final MemberUserSearchQueryBuilder builder = new MemberUserSearchQueryBuilder();

    /** 多字段条件应构建为 Bool Query。 */
    @Test
    void shouldBuildBoolQueryForMemberSearch() {
        MemberUserSearchPageBo bo = new MemberUserSearchPageBo();
        bo.setStatus(1);
        bo.setMobile("13800138000");
        bo.setKeyword("飞翔云");

        assertTrue(builder.build(bo).isBool());
    }
}
