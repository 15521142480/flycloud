package com.fly.common.enums.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Elasticsearch 索引升级记录的回滚可用状态。
 *
 * <p>仅升级操作有回滚语义；初始化、同步、删除和回滚操作固定为不适用。</p>
 */
@Getter
@AllArgsConstructor
public enum ElasticsearchIndexRollbackStatusEnum {

    /** 当前操作不具备回滚语义。 */
    NOT_APPLICABLE("NOT_APPLICABLE", "不适用"),

    /** 升级已成功，升级前索引仍保留，可执行别名回滚。 */
    AVAILABLE("AVAILABLE", "可回滚"),

    /** 已完成一次别名回滚。 */
    ROLLED_BACK("ROLLED_BACK", "已回滚"),

    /** 升级前索引已删除等原因导致不能再回滚。 */
    UNAVAILABLE("UNAVAILABLE", "不可回滚");

    /** 持久化编码。 */
    private final String code;

    /** 中文名称。 */
    private final String name;
}
