package com.fly.common.elasticsearch.index;

import java.util.Objects;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Elasticsearch 版本索引命名值对象。
 */
public final class ElasticsearchIndexName {

    private static final Pattern VERSIONED_INDEX = Pattern.compile("^([a-z0-9][a-z0-9_-]*)_v([1-9][0-9]*)$");

    private ElasticsearchIndexName() {
    }

    /**
     * 按统一规则构造 {alias}_v{version} 真实索引名。
     */
     public static String buildVersionedIndex(String alias, int version) {
        validateAlias(alias);
        if (version < 1) {
            throw new IllegalArgumentException("索引版本必须大于 0");
        }
        return alias + "_v" + version;
    }

    /**
     * 解析真实索引中的正整数版本号。
     */
    public static int parseVersion(String indexName) {
        Matcher matcher = matcher(indexName);
        return Integer.parseInt(matcher.group(2));
    }

    /**
     * 解析真实版本索引归属的业务别名。
     *
     * @param indexName 真实版本索引名称
     * @return 业务稳定别名
     */
    public static String parseAlias(String indexName) {
        return matcher(indexName).group(1);
    }

    /**
     * 返回按版本号倒序排列真实版本索引的比较器。
     *
     * <p>不能使用字符串自然排序，否则 {@code _v10} 会被错误排在 {@code _v2} 前面。</p>
     */
    public static Comparator<String> versionDescendingComparator() {
        return Comparator.comparingInt(ElasticsearchIndexName::parseVersion).reversed();
    }

    /**
     * 基于当前真实索引生成下一版本真实索引名。
     */
     public static String nextVersion(String indexName) {
        Matcher matcher = matcher(indexName);
        return buildVersionedIndex(matcher.group(1), parseVersion(indexName) + 1);
    }

    /**
     * 判断字符串是否符合版本索引规则。
     */
     public static boolean isVersionedIndex(String indexName) {
        return indexName != null && VERSIONED_INDEX.matcher(indexName).matches();
    }

    /**
     * 校验真实索引规则，不符合时抛出参数异常。
     */
     public static void validateVersionedIndex(String indexName) {
        matcher(indexName);
    }

    /**
     * 匹配并返回索引名称组成部分。
     */
     private static Matcher matcher(String indexName) {
        Matcher matcher = VERSIONED_INDEX.matcher(Objects.requireNonNull(indexName, "索引名不能为空"));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("索引必须符合 {alias}_v{version}：" + indexName);
        }
        return matcher;
    }

    /**
     * 校验业务别名字符规则。
     */
     private static void validateAlias(String alias) {
        if (alias == null || !alias.matches("^[a-z0-9][a-z0-9_-]*$")) {
            throw new IllegalArgumentException("别名只能包含小写字母、数字、下划线和连字符：" + alias);
        }
    }
}
