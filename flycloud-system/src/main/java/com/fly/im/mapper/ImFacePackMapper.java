package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImFacePackPageBo;
import com.fly.system.api.im.domain.ImFacePack;
import com.fly.system.api.im.domain.vo.ImFacePackVo;

import java.util.List;

/**
 * IM 表情包 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFacePackMapper extends BaseMapperPlus<ImFacePackMapper, ImFacePack, ImFacePackVo> {

    default List<ImFacePack> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePack>()
                .eq(ImFacePack::getStatus, status)
                .orderByAsc(ImFacePack::getSort)
                .orderByAsc(ImFacePack::getId));
    }

    default PageResult<ImFacePack> selectPage(ImFacePackPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImFacePack>()
                .likeIfPresent(ImFacePack::getName, reqVo.getName())
                .eqIfPresent(ImFacePack::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImFacePack::getCreateTime, reqVo.getCreateTime())
                .orderByAsc(ImFacePack::getSort)
                .orderByDesc(ImFacePack::getId)));
    }

}
