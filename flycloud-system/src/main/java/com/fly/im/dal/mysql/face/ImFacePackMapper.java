package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.face.vo.pack.ImFacePackPageReqVo;
import com.fly.im.dal.dataobject.face.ImFacePackDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 表情包 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImFacePackMapper extends BaseMapperX<ImFacePackDO> {

    default List<ImFacePackDO> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePackDO>()
                .eq(ImFacePackDO::getStatus, status)
                .orderByAsc(ImFacePackDO::getSort)
                .orderByAsc(ImFacePackDO::getId));
    }

    default PageResult<ImFacePackDO> selectPage(ImFacePackPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFacePackDO>()
                .likeIfPresent(ImFacePackDO::getName, reqVo.getName())
                .eqIfPresent(ImFacePackDO::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImFacePackDO::getCreateTime, reqVo.getCreateTime())
                .orderByAsc(ImFacePackDO::getSort)
                .orderByDesc(ImFacePackDO::getId));
    }

}
