package com.fly.member.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员用户 Mapper。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface MemberUserMapper extends BaseMapperPlus<MemberUserMapper, MemberUser, MemberUserVo> {

    /**
     * 按主键稳定游标查询有效会员用户。
     *
     * @param lastId 上一批最后一条主键；首次传 {@code 0}
     * @param limit 单批最大行数
     * @return 按主键正序排列的会员用户
     */
    @Select("""
            SELECT * FROM member_user
            WHERE is_deleted = 0 AND id > #{lastId}
            ORDER BY id ASC LIMIT #{limit}
            """)
    List<MemberUser> selectForEsSyncByCursor(@Param("lastId") Long lastId, @Param("limit") int limit);
}
