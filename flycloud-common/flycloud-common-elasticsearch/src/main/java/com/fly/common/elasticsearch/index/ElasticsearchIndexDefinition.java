package com.fly.common.elasticsearch.index;

/**
 * 业务索引定义。
 *
 * <p>公共层只依赖该契约，不感知具体业务字段或 Mapping 文件路径。</p>
 */
public interface ElasticsearchIndexDefinition {

    /** @return 程序访问的稳定业务索引别名。 */
    String alias();

    /** @return classpath 下的 JSON Mapping 资源路径。 */
    String mappingResource();

    /** @return 初始真实索引版本号。 */
    int initialVersion();

    /** @return 全量同步时单批文档数量。 */
    int bulkSize();

    /** @return 业务 ES Document 类型。 */
    Class<?> documentType();
}
