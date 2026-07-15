package com.fly.member.search.index;

import com.fly.common.elasticsearch.index.ElasticsearchIndexDefinition;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.member.search.document.MemberUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 会员用户索引的唯一业务定义入口。
 */
@Component
@RequiredArgsConstructor
public class MemberUserIndexDefinition implements ElasticsearchIndexDefinition {

    private final ElasticsearchProperties properties;

    /**
     * @return 会员用户的稳定业务别名。
     */
    @Override
    public String alias() {
        return "member_user";
    }

    /**
     * @return 会员业务域下受版本控制的 JSON Mapping。
     */
    @Override
    public String mappingResource() {
        return "elasticsearch/member/member_user.json";
    }

    /**
     * @return 首次初始化使用 v1。
     */
    @Override
    public int initialVersion() {
        return 1;
    }

    /**
     * @return 复用公共 ES 配置中的 Bulk 批次大小。
     */
    @Override
    public int bulkSize() {
        return properties.getBulkSize();
    }

    /**
     * @return 会员搜索专用 Document 类型。
     */
    @Override
    public Class<?> documentType() {
        return MemberUserDocument.class;
    }
}
