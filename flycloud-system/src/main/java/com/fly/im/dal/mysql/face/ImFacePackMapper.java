package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.face.pack.ImFacePackPageReqVo;
import com.fly.system.api.im.domain.face.ImFacePack;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 表情包 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImFacePackMapper extends BaseMapperX<ImFacePack> {

    default List<ImFacePack> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePack>()
                .eq(ImFacePack::getStatus, status)
                .orderByAsc(ImFacePack::getSort)
                .orderByAsc(ImFacePack::getId));
    }

    default PageResult<ImFacePack> selectPage(ImFacePackPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFacePack>()
                .likeIfPresent(ImFacePack::getName, reqVo.getName())
                .eqIfPresent(ImFacePack::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImFacePack::getCreateTime, reqVo.getCreateTime())
                .orderByAsc(ImFacePack::getSort)
                .orderByDesc(ImFacePack::getId));
    }

}
