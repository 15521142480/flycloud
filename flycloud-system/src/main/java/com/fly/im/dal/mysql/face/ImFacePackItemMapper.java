package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemPageReqVo;
import com.fly.system.api.im.domain.face.ImFacePackItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * IM 表情包项 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImFacePackItemMapper extends BaseMapperX<ImFacePackItem> {

    default List<ImFacePackItem> selectListByPackIdsAndStatus(Collection<Long> packIds, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePackItem>()
                .in(ImFacePackItem::getPackId, packIds)
                .eq(ImFacePackItem::getStatus, status)
                .orderByAsc(ImFacePackItem::getPackId)
                .orderByAsc(ImFacePackItem::getSort)
                .orderByAsc(ImFacePackItem::getId));
    }

    default Long selectCountByPackId(Long packId) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItem>()
                .eq(ImFacePackItem::getPackId, packId));
    }

    default Long selectCountByPackIds(Collection<Long> packIds) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItem>()
                .in(ImFacePackItem::getPackId, packIds));
    }

    default PageResult<ImFacePackItem> selectPage(ImFacePackItemPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFacePackItem>()
                .eqIfPresent(ImFacePackItem::getPackId, reqVo.getPackId())
                .likeIfPresent(ImFacePackItem::getName, reqVo.getName())
                .eqIfPresent(ImFacePackItem::getStatus, reqVo.getStatus())
                .orderByAsc(ImFacePackItem::getSort)
                .orderByDesc(ImFacePackItem::getId));
    }

}
