package com.fly.common.enums.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Elasticsearch 索引操作执行状态。
 *
 * <p>该状态只描述一次操作是否执行完成，不混入别名是否已切换、是否仍可回滚等版本生命周期语义。</p>
 */
@Getter
@AllArgsConstructor
public enum ElasticsearchIndexOperationStatusEnum {

    /** 审计记录已创建，尚未执行外部索引操作。 */
    PENDING("PENDING", "待执行"),

    /** 正在执行索引创建、同步、校验、别名切换或删除。 */
    RUNNING("RUNNING", "执行中"),

    /** 操作已完整成功。 */
    SUCCEEDED("SUCCEEDED", "成功"),

    /** 操作异常结束。 */
    FAILED("FAILED", "失败");

    /** 持久化编码。 */
    private final String code;

    /** 中文名称。 */
    private final String name;
}
