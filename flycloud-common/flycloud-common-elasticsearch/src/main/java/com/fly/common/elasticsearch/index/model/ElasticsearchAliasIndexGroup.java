package com.fly.common.elasticsearch.index.model;

import java.util.List;

/**
 * 业务索引别名及其全部真实版本索引。
 *
 * @param alias 业务稳定别名
 * @param currentIndex 当前由别名指向、正在读写的真实索引
 * @param versionIndexes 按版本号倒序排列的真实版本索引
 */
public record ElasticsearchAliasIndexGroup(String alias, String currentIndex, List<String> versionIndexes) {
}
