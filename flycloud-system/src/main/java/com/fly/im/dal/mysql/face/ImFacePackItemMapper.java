package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.face.vo.item.ImFacePackItemPageReqVo;
import com.fly.im.dal.dataobject.face.ImFacePackItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * IM 表情包项 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImFacePackItemMapper extends BaseMapperX<ImFacePackItemDO> {

    default List<ImFacePackItemDO> selectListByPackIdsAndStatus(Collection<Long> packIds, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePackItemDO>()
                .in(ImFacePackItemDO::getPackId, packIds)
                .eq(ImFacePackItemDO::getStatus, status)
                .orderByAsc(ImFacePackItemDO::getPackId)
                .orderByAsc(ImFacePackItemDO::getSort)
                .orderByAsc(ImFacePackItemDO::getId));
    }

    default Long selectCountByPackId(Long packId) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItemDO>()
                .eq(ImFacePackItemDO::getPackId, packId));
    }

    default Long selectCountByPackIds(Collection<Long> packIds) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItemDO>()
                .in(ImFacePackItemDO::getPackId, packIds));
    }

    default PageResult<ImFacePackItemDO> selectPage(ImFacePackItemPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFacePackItemDO>()
                .eqIfPresent(ImFacePackItemDO::getPackId, reqVo.getPackId())
                .likeIfPresent(ImFacePackItemDO::getName, reqVo.getName())
                .eqIfPresent(ImFacePackItemDO::getStatus, reqVo.getStatus())
                .orderByAsc(ImFacePackItemDO::getSort)
                .orderByDesc(ImFacePackItemDO::getId));
    }

}
