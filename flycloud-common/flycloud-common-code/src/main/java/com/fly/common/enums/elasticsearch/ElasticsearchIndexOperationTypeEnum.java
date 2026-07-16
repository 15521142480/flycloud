package com.fly.common.enums.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Elasticsearch 版本索引操作类型。
 *
 * <p>用于索引操作审计，区分初始化、数据同步、版本升级、别名回滚与历史版本删除等业务动作。</p>
 */
@Getter
@AllArgsConstructor
public enum ElasticsearchIndexOperationTypeEnum {

    /** 首次创建版本索引并绑定业务别名。 */
    INITIALIZE("INITIALIZE", "初始化"),

    /** 向当前业务别名指向的真实索引执行全量同步。 */
    SYNCHRONIZE("SYNCHRONIZE", "全量同步"),

    /** 创建下一版本索引、同步数据并原子切换业务别名。 */
    UPGRADE("UPGRADE", "升级"),

    /** 将业务别名回切到指定历史版本索引。 */
    ROLLBACK("ROLLBACK", "回滚"),

    /** 删除不再被业务别名引用的历史版本索引。 */
    DELETE("DELETE", "删除历史版本");

    /** 持久化编码。 */
    private final String code;

    /** 中文名称。 */
    private final String name;
}
