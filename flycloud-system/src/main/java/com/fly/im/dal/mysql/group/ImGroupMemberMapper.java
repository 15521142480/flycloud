package com.fly.im.dal.mysql.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.group.ImGroupMember;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IM 群成员 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImGroupMemberMapper extends BaseMapperX<ImGroupMember> {

    default List<ImGroupMember> selectListByGroupId(Long groupId) {
        return selectList(new LambdaQueryWrapperX<ImGroupMember>().eq(ImGroupMember::getGroupId, groupId));
    }

    default ImGroupMember selectByGroupIdAndUserId(Long groupId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getUserId, userId));
    }

    default List<ImGroupMember> selectListByGroupIdAndUserIds(Long groupId, Collection<Long> userIds) {
        return selectList(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .in(ImGroupMember::getUserId, userIds));
    }

    default List<ImGroupMember> selectListByGroupIdAndStatus(Long groupId, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getStatus, status));
    }

    default List<ImGroupMember> selectListByGroupIdAndStatusAndRoles(Long groupId, Integer status,
                                                                        Collection<Integer> roles) {
        return selectList(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getStatus, status)
                .in(ImGroupMember::getRole, roles));
    }

    default List<ImGroupMember> selectListByUserIdAndStatus(Long userId, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getUserId, userId)
                .eq(ImGroupMember::getStatus, status));
    }

    /**
     * 查询用户已退群的成员记录
     * <p>
     * 当 {@code minQuitTime} 非空时额外按 {@code quitTime ≥ minQuitTime} 过滤。
     *
     * @param userId      用户编号
     * @param minQuitTime 最早退群时间（含），可空
     * @return 已退群成员记录列表
     */
    default List<ImGroupMember> selectQuitListByUserId(Long userId, LocalDateTime minQuitTime) {
        LambdaQueryWrapperX<ImGroupMember> wrapper = new LambdaQueryWrapperX<>();
        wrapper.eq(ImGroupMember::getUserId, userId)
                .eq(ImGroupMember::getStatus, CommonStatusEnum.DISABLE.getStatus());
        wrapper.geIfPresent(ImGroupMember::getQuitTime, minQuitTime);
        return selectList(wrapper);
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateByGroupIdAndStatus(Long groupId, Integer oldStatus, ImGroupMember updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getStatus, oldStatus));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateByGroupIdAndUserIdsAndStatus(Long groupId, Collection<Long> userIds,
                                                   Integer oldStatus, ImGroupMember updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .in(ImGroupMember::getUserId, userIds)
                .eq(ImGroupMember::getStatus, oldStatus));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateListByGroupIdAndUserIds(Long groupId, Collection<Long> userIds, ImGroupMember updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .in(ImGroupMember::getUserId, userIds));
    }

    default Long selectCountByGroupIdAndRoleAndStatus(Long groupId, Integer role, Integer status) {
        return selectCount(new LambdaQueryWrapperX<ImGroupMember>()
                .eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getRole, role)
                .eq(ImGroupMember::getStatus, status));
    }

    /**
     * 批量按 group_id 统计指定状态的成员数
     */
    default Map<Long, Long> selectCountMapByGroupIdsAndStatus(Collection<Long> groupIds, Integer status) {
        if (CollUtil.isEmpty(groupIds)) {
            return Collections.emptyMap();
        }
        List<Map<String, Object>> rows = selectMaps(Wrappers.<ImGroupMember>query()
                .select("group_id AS groupId", "COUNT(*) AS cnt")
                .in("group_id", groupIds)
                .eq("status", status)
                .groupBy("group_id"));
        // 转换成 Map<Long, Long>
        Map<Long, Long> result = new HashMap<>(rows.size());
        rows.forEach(row -> result.put(
                ((Number) row.get("groupId")).longValue(),
                ((Number) row.get("cnt")).longValue()));
        return result;
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateMuteEndTimeNull(Long id) {
        return update(null, Wrappers.<ImGroupMember>lambdaUpdate()
                .eq(ImGroupMember::getId, id)
                .set(ImGroupMember::getMuteEndTime, null));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateRejoinFields(Long id, Integer status, LocalDateTime joinTime,
                                   Integer role, Integer addSource, Long inviterUserId) {
        return update(null, Wrappers.<ImGroupMember>lambdaUpdate()
                .eq(ImGroupMember::getId, id)
                .set(ImGroupMember::getStatus, status)
                .set(ImGroupMember::getJoinTime, joinTime)
                .set(ImGroupMember::getRole, role)
                .set(ImGroupMember::getAddSource, addSource)
                .set(ImGroupMember::getInviterUserId, inviterUserId)
                .set(ImGroupMember::getQuitTime, null)
                .set(ImGroupMember::getMuteEndTime, null));
    }

}
