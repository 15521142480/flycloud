package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.face.vo.useritem.ImFaceUserItemManagerPageReqVo;
import com.fly.im.dal.dataobject.face.ImFaceUserItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 用户私有表情 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImFaceUserItemMapper extends BaseMapperX<ImFaceUserItemDO> {

    default List<ImFaceUserItemDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<ImFaceUserItemDO>()
                .eq(ImFaceUserItemDO::getUserId, userId)
                .orderByAsc(ImFaceUserItemDO::getSort)
                .orderByDesc(ImFaceUserItemDO::getId));
    }

    default ImFaceUserItemDO selectByUserIdAndUrl(Long userId, String url) {
        return selectOne(new LambdaQueryWrapperX<ImFaceUserItemDO>()
                .eq(ImFaceUserItemDO::getUserId, userId)
                .eq(ImFaceUserItemDO::getUrl, url));
    }

    default Long selectCountByUserId(Long userId) {
        return selectCount(ImFaceUserItemDO::getUserId, userId);
    }

    default PageResult<ImFaceUserItemDO> selectPage(ImFaceUserItemManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFaceUserItemDO>()
                .eqIfPresent(ImFaceUserItemDO::getUserId, reqVo.getUserId())
                .likeIfPresent(ImFaceUserItemDO::getName, reqVo.getName())
                .betweenIfPresent(ImFaceUserItemDO::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImFaceUserItemDO::getId));
    }

}
