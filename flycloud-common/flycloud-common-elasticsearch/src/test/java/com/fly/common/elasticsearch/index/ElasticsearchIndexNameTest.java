package com.fly.common.elasticsearch.index;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

/**
 * 索引版本规则单元测试。
 */
class ElasticsearchIndexNameTest {

    /**
     * 验证版本索引名称构造、解析和升级。
     */
    @Test
    void shouldBuildParseAndAdvanceVersion() {
        assertEquals("member_user_v2", ElasticsearchIndexName.buildVersionedIndex("member_user", 2));
        assertEquals(2, ElasticsearchIndexName.parseVersion("member_user_v2"));
        assertEquals("member_user", ElasticsearchIndexName.parseAlias("member_user_v2"));
        assertEquals("member_user_v3", ElasticsearchIndexName.nextVersion("member_user_v2"));
    }

    /**
     * 验证版本列表按数字版本而非字符串字典序倒序排列。
     */
    @Test
    void shouldSortVersionIndexesByNumericVersionDescending() {
        List<String> indexes = List.of("member_user_v2", "member_user_v10", "member_user_v1");

        assertEquals(List.of("member_user_v10", "member_user_v2", "member_user_v1"),
                indexes.stream().sorted(ElasticsearchIndexName.versionDescendingComparator()).toList());
    }

    /**
     * 验证非法索引版本号会被拒绝。
     */
    @Test
    void shouldRejectInvalidVersionedIndex() {
        assertFalse(ElasticsearchIndexName.isVersionedIndex("member_user"));
        assertThrows(IllegalArgumentException.class, () -> ElasticsearchIndexName.validateVersionedIndex("member_user_v0"));
    }
}
