package com.fly.member.search.index;

import com.fly.common.database.web.support.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员用户 Elasticsearch 索引操作审计记录数据访问对象。
 */
@Mapper
public interface MemberUserIndexOperationRecordMapper extends BaseMapperPlus<
        MemberUserIndexOperationRecordMapper,
        MemberUserIndexOperationRecord,
        MemberUserIndexOperationRecord> {
}
