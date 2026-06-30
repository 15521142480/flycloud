package com.fly.im.dal.mysql.group;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.group.vo.ImGroupManagerPageReqVo;
import com.fly.im.dal.dataobject.group.ImGroupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 群 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImGroupMapper extends BaseMapperX<ImGroupDO> {

    default ImGroupDO selectByIdForUpdate(Long id) {
        return selectOne(new LambdaQueryWrapperX<ImGroupDO>()
                .eq(ImGroupDO::getId, id)
                .last("FOR UPDATE"));
    }

    default PageResult<ImGroupDO> selectPage(ImGroupManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImGroupDO>()
                .likeIfPresent(ImGroupDO::getName, reqVo.getName())
                .eqIfPresent(ImGroupDO::getOwnerUserId, reqVo.getOwnerUserId())
                .eqIfPresent(ImGroupDO::getStatus, reqVo.getStatus())
                .eqIfPresent(ImGroupDO::getBanned, reqVo.getBanned())
                .betweenIfPresent(ImGroupDO::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImGroupDO::getId));
    }

}
