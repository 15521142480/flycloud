package com.fly.im.dal.mysql.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.face.useritem.ImFaceUserItemManagerPageReqVo;
import com.fly.system.api.im.domain.face.ImFaceUserItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 用户私有表情 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImFaceUserItemMapper extends BaseMapperX<ImFaceUserItem> {

    default List<ImFaceUserItem> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<ImFaceUserItem>()
                .eq(ImFaceUserItem::getUserId, userId)
                .orderByAsc(ImFaceUserItem::getSort)
                .orderByDesc(ImFaceUserItem::getId));
    }

    default ImFaceUserItem selectByUserIdAndUrl(Long userId, String url) {
        return selectOne(new LambdaQueryWrapperX<ImFaceUserItem>()
                .eq(ImFaceUserItem::getUserId, userId)
                .eq(ImFaceUserItem::getUrl, url));
    }

    default Long selectCountByUserId(Long userId) {
        return selectCount(ImFaceUserItem::getUserId, userId);
    }

    default PageResult<ImFaceUserItem> selectPage(ImFaceUserItemManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFaceUserItem>()
                .eqIfPresent(ImFaceUserItem::getUserId, reqVo.getUserId())
                .likeIfPresent(ImFaceUserItem::getName, reqVo.getName())
                .betweenIfPresent(ImFaceUserItem::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImFaceUserItem::getId));
    }

}
