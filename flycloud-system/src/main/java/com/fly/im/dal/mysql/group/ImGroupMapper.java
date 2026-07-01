package com.fly.im.dal.mysql.group;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.group.ImGroupManagerPageReqVo;
import com.fly.system.api.im.domain.group.ImGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 群 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImGroupMapper extends BaseMapperX<ImGroup> {

    default ImGroup selectByIdForUpdate(Long id) {
        return selectOne(new LambdaQueryWrapperX<ImGroup>()
                .eq(ImGroup::getId, id)
                .last("FOR UPDATE"));
    }

    default PageResult<ImGroup> selectPage(ImGroupManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImGroup>()
                .likeIfPresent(ImGroup::getName, reqVo.getName())
                .eqIfPresent(ImGroup::getOwnerUserId, reqVo.getOwnerUserId())
                .eqIfPresent(ImGroup::getStatus, reqVo.getStatus())
                .eqIfPresent(ImGroup::getBanned, reqVo.getBanned())
                .betweenIfPresent(ImGroup::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImGroup::getId));
    }

}
