package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImChannelMessagePageBo;
import com.fly.system.api.im.domain.ImChannelMessage;
import com.fly.system.api.im.domain.vo.ImChannelMessageVo;

import java.util.List;

/**
 * IM 频道消息 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImChannelMessageMapper extends BaseMapperPlus<ImChannelMessageMapper, ImChannelMessage, ImChannelMessageVo> {

    /**
     * 拉取指定用户应收的频道消息
     * <p>
     * 命中条件：id 大于游标 + (receiver_user_ids 为空表示全员 OR 逗号分隔列表里包含当前 userId)
     *
     * @param userId 当前用户编号
     * @param minId  游标；返回大于此值的消息
     * @param size   返回条数
     * @return 频道消息列表；按 id 升序
     */
    default List<ImChannelMessage> selectListByUserAndMinId(Long userId, Long minId, Integer size) {
        return selectList(new LambdaQueryWrapperX<ImChannelMessage>()
                .gt(ImChannelMessage::getId, minId)
                .and(w -> w.isNull(ImChannelMessage::getReceiverUserIds)
                        .or().eq(ImChannelMessage::getReceiverUserIds, "")
                        .or().apply("FIND_IN_SET({0}, receiver_user_ids)", userId))
                .orderByAsc(ImChannelMessage::getId)
                .last("LIMIT " + size));
    }

    default Long selectCountByMaterialId(Long materialId) {
        return selectCount(ImChannelMessage::getMaterialId, materialId);
    }

    default PageResult<ImChannelMessage> selectPage(ImChannelMessagePageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImChannelMessage>()
                .eqIfPresent(ImChannelMessage::getChannelId, reqVo.getChannelId())
                .eqIfPresent(ImChannelMessage::getMaterialId, reqVo.getMaterialId())
                .betweenIfPresent(ImChannelMessage::getSendTime, reqVo.getSendTime())
                .orderByDesc(ImChannelMessage::getId)));
    }

}
