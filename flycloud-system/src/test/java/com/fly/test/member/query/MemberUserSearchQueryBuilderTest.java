package com.fly.test.member.query;

import com.fly.test.member.domain.bo.MemberUserSearchPageBo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** MemberUser 查询构造器单元测试。 */
class MemberUserSearchQueryBuilderTest {

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
