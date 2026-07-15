package com.fly.member.search.index;

import com.fly.common.database.web.support.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员索引升级记录 Mapper。
 */
@Mapper
public interface MemberUserIndexUpgradeRecordMapper extends BaseMapperPlus<
        MemberUserIndexUpgradeRecordMapper,
        MemberUserIndexUpgradeRecord,
        MemberUserIndexUpgradeRecord> {
}
