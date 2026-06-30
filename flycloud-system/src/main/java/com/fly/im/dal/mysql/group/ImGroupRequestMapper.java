package com.fly.im.dal.mysql.group;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.group.vo.ImGroupRequestManagerPageReqVo;
import com.fly.im.dal.dataobject.group.ImGroupRequestDO;
import com.fly.im.enums.group.ImGroupRequestHandleResultEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 加群申请记录 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImGroupRequestMapper extends BaseMapperX<ImGroupRequestDO> {

    default ImGroupRequestDO selectByGroupIdAndUserId(Long groupId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<ImGroupRequestDO>()
                .eq(ImGroupRequestDO::getGroupId, groupId)
                .eq(ImGroupRequestDO::getUserId, userId));
    }

    default List<ImGroupRequestDO> selectListByGroupIdsAndHandleResult(Collection<Long> groupIds, Integer handleResult) {
        return selectList(new LambdaQueryWrapperX<ImGroupRequestDO>()
                .in(ImGroupRequestDO::getGroupId, groupIds)
                .eq(ImGroupRequestDO::getHandleResult, handleResult)
                .orderByDesc(ImGroupRequestDO::getUpdateTime)
                .orderByDesc(ImGroupRequestDO::getId));
    }

    default List<ImGroupRequestDO> selectListByGroupId(Long groupId) {
        // 同上，update_time 倒序优先于 id
        return selectList(new LambdaQueryWrapperX<ImGroupRequestDO>()
                .eq(ImGroupRequestDO::getGroupId, groupId)
                .orderByDesc(ImGroupRequestDO::getUpdateTime)
                .orderByDesc(ImGroupRequestDO::getId));
    }

    default int updateByIdAndHandleResult(Long id, Integer expectedHandleResult, ImGroupRequestDO updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ImGroupRequestDO>()
                .eq(ImGroupRequestDO::getId, id).eq(ImGroupRequestDO::getHandleResult, expectedHandleResult));
    }

    /**
     * 复用主动申请的旧记录：覆盖申请理由 / 来源，重置为未处理 + 清空旧处理痕迹 + 刷 update_time
     * <p>
     * update_time 显式 set，因为 update(null, wrapper) 不会触发 MetaObjectHandler.updateFill；
     * 列表查询按 update_time 倒序，复用记录必须刷这一列才会排到最前
     */
    default int updateApplyByIdReset(Long id, String applyContent, Integer addSource, LocalDateTime updateTime) {
        return update(null, new LambdaUpdateWrapper<ImGroupRequestDO>()
                .eq(ImGroupRequestDO::getId, id)
                .set(ImGroupRequestDO::getApplyContent, applyContent)
                .set(ImGroupRequestDO::getAddSource, addSource)
                .set(ImGroupRequestDO::getHandleResult, ImGroupRequestHandleResultEnum.UNHANDLED.getResult())
                .set(ImGroupRequestDO::getInviterUserId, null)
                .set(ImGroupRequestDO::getHandleUserId, null)
                .set(ImGroupRequestDO::getHandleContent, null)
                .set(ImGroupRequestDO::getHandleTime, null)
                .set(ImGroupRequestDO::getUpdateTime, updateTime));
    }

    default int updateInviteByIdReset(Long id, Long inviterUserId, Integer addSource, LocalDateTime updateTime) {
        return update(null, new LambdaUpdateWrapper<ImGroupRequestDO>()
                .eq(ImGroupRequestDO::getId, id)
                .set(ImGroupRequestDO::getInviterUserId, inviterUserId)
                .set(ImGroupRequestDO::getAddSource, addSource)
                .set(ImGroupRequestDO::getHandleResult, ImGroupRequestHandleResultEnum.UNHANDLED.getResult())
                .set(ImGroupRequestDO::getHandleUserId, null)
                .set(ImGroupRequestDO::getHandleContent, null)
                .set(ImGroupRequestDO::getHandleTime, null)
                .set(ImGroupRequestDO::getUpdateTime, updateTime));
    }

    default PageResult<ImGroupRequestDO> selectPage(ImGroupRequestManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImGroupRequestDO>()
                .eqIfPresent(ImGroupRequestDO::getGroupId, reqVo.getGroupId())
                .eqIfPresent(ImGroupRequestDO::getUserId, reqVo.getUserId())
                .eqIfPresent(ImGroupRequestDO::getInviterUserId, reqVo.getInviterUserId())
                .eqIfPresent(ImGroupRequestDO::getHandleResult, reqVo.getHandleResult())
                .eqIfPresent(ImGroupRequestDO::getAddSource, reqVo.getAddSource())
                .betweenIfPresent(ImGroupRequestDO::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImGroupRequestDO::getId));
    }

}
