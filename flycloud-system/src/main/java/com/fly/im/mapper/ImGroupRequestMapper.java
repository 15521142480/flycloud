package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.security.util.UserUtils;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImGroupRequestManagerPageBo;
import com.fly.system.api.im.domain.ImGroupRequest;
import com.fly.system.api.im.domain.vo.ImGroupRequestVo;
import com.fly.system.api.im.enums.group.ImGroupRequestHandleResultEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 加群申请记录 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImGroupRequestMapper extends BaseMapperPlus<ImGroupRequestMapper, ImGroupRequest, ImGroupRequestVo> {

    default ImGroupRequest selectByGroupIdAndUserId(Long groupId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<ImGroupRequest>()
                .eq(ImGroupRequest::getGroupId, groupId)
                .eq(ImGroupRequest::getUserId, userId));
    }

    default List<ImGroupRequest> selectListByGroupIdsAndHandleResult(Collection<Long> groupIds, Integer handleResult) {
        return selectList(new LambdaQueryWrapperX<ImGroupRequest>()
                .in(ImGroupRequest::getGroupId, groupIds)
                .eq(ImGroupRequest::getHandleResult, handleResult)
                .orderByDesc(ImGroupRequest::getUpdateTime)
                .orderByDesc(ImGroupRequest::getId));
    }

    default List<ImGroupRequest> selectListByGroupId(Long groupId) {
        // 同上，update_time 倒序优先于 id
        return selectList(new LambdaQueryWrapperX<ImGroupRequest>()
                .eq(ImGroupRequest::getGroupId, groupId)
                .orderByDesc(ImGroupRequest::getUpdateTime)
                .orderByDesc(ImGroupRequest::getId));
    }

    default int updateByIdAndHandleResult(Long id, Integer expectedHandleResult, ImGroupRequest updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ImGroupRequest>()
                .eq(ImGroupRequest::getId, id).eq(ImGroupRequest::getHandleResult, expectedHandleResult));
    }

    /**
     * 复用主动申请的旧记录：覆盖申请理由 / 来源，重置为未处理 + 清空旧处理痕迹 + 刷 update_time
     * <p>
     * update_time 显式 set，因为 update(null, wrapper) 不会触发 MetaObjectHandler.updateFill；
     * 列表查询按 update_time 倒序，复用记录必须刷这一列才会排到最前
     */
    default int updateApplyByIdReset(Long id, String applyContent, Integer addSource, LocalDateTime updateTime) {
        return update(null, new LambdaUpdateWrapper<ImGroupRequest>()
                .eq(ImGroupRequest::getId, id)
                .set(ImGroupRequest::getApplyContent, applyContent)
                .set(ImGroupRequest::getAddSource, addSource)
                .set(ImGroupRequest::getHandleResult, ImGroupRequestHandleResultEnum.UNHANDLED.getResult())
                .set(ImGroupRequest::getInviterUserId, null)
                .set(ImGroupRequest::getHandleUserId, null)
                .set(ImGroupRequest::getHandleContent, null)
                .set(ImGroupRequest::getHandleTime, null)
                .set(ImGroupRequest::getUpdateBy, UserUtils.getCurUserIdStr())
                .set(ImGroupRequest::getUpdateTime, updateTime)
        );
    }

    default int updateInviteByIdReset(Long id, Long inviterUserId, Integer addSource, LocalDateTime updateTime) {
        return update(null, new LambdaUpdateWrapper<ImGroupRequest>()
                .eq(ImGroupRequest::getId, id)
                .set(ImGroupRequest::getInviterUserId, inviterUserId)
                .set(ImGroupRequest::getAddSource, addSource)
                .set(ImGroupRequest::getHandleResult, ImGroupRequestHandleResultEnum.UNHANDLED.getResult())
                .set(ImGroupRequest::getHandleUserId, null)
                .set(ImGroupRequest::getHandleContent, null)
                .set(ImGroupRequest::getHandleTime, null)
                .set(ImGroupRequest::getUpdateBy, UserUtils.getCurUserIdStr())
                .set(ImGroupRequest::getUpdateTime, updateTime)
        );
    }

    default PageResult<ImGroupRequest> selectPage(ImGroupRequestManagerPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImGroupRequest>()
                .eqIfPresent(ImGroupRequest::getGroupId, reqVo.getGroupId())
                .eqIfPresent(ImGroupRequest::getUserId, reqVo.getUserId())
                .eqIfPresent(ImGroupRequest::getInviterUserId, reqVo.getInviterUserId())
                .eqIfPresent(ImGroupRequest::getHandleResult, reqVo.getHandleResult())
                .eqIfPresent(ImGroupRequest::getAddSource, reqVo.getAddSource())
                .betweenIfPresent(ImGroupRequest::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImGroupRequest::getId)));
    }

}
