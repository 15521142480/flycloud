package com.fly.member.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fly.member.search.index.MemberUserIndexFields;
import com.fly.member.search.model.MemberUserSearchPageBo;
import org.springframework.stereotype.Component;

/**
 * 会员用户 ES 业务查询构造器：只构造 Query，不关心分页、别名或客户端调用。
 */
@Component
public class MemberUserSearchQueryBuilder {

    /**
     * 根据业务检索条件构造结构化 Query。
     *
     * <p>本类不处理分页、排序和客户端调用，避免查询 DSL 与服务编排耦合。</p>
     */
    public Query build(MemberUserSearchPageBo bo) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        if (bo.getId() != null) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.ID).value(bo.getId())));
        }
        if (bo.getStatus() != null) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.STATUS).value(bo.getStatus())));
        }
        if (bo.getGroupId() != null) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.GROUP_ID).value(bo.getGroupId())));
        }
        if (bo.getLevelId() != null) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.LEVEL_ID).value(bo.getLevelId())));
        }
        if (hasText(bo.getMobile())) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.MOBILE).value(bo.getMobile())));
        }
        if (hasText(bo.getEmail())) {
            builder.filter(q -> q.term(t -> t.field(MemberUserIndexFields.EMAIL).value(bo.getEmail())));
        }
        if (hasText(bo.getKeyword())) {
            builder.must(q -> q.multiMatch(m -> m.query(bo.getKeyword())
                    .fields(MemberUserIndexFields.NICKNAME + "^2", MemberUserIndexFields.NAME, MemberUserIndexFields.MARK)));
        }
        return builder.build()._toQuery();
    }

    /**
     * 判断检索文本是否有效。
     */
     private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
