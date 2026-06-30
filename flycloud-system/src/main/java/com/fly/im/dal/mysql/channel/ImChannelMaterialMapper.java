package com.fly.im.dal.mysql.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialPageReqVo;
import com.fly.im.dal.dataobject.channel.ImChannelMaterialDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 频道素材 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImChannelMaterialMapper extends BaseMapperX<ImChannelMaterialDO> {

    default Long selectCountByChannelId(Long channelId) {
        return selectCount(ImChannelMaterialDO::getChannelId, channelId);
    }

    default List<ImChannelMaterialDO> selectListByChannelId(Long channelId) {
        return selectList(new LambdaQueryWrapperX<ImChannelMaterialDO>()
                .eq(ImChannelMaterialDO::getChannelId, channelId)
                .orderByDesc(ImChannelMaterialDO::getId));
    }

    default PageResult<ImChannelMaterialDO> selectPage(ImChannelMaterialPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImChannelMaterialDO>()
                .eqIfPresent(ImChannelMaterialDO::getChannelId, reqVo.getChannelId())
                .eqIfPresent(ImChannelMaterialDO::getType, reqVo.getType())
                .likeIfPresent(ImChannelMaterialDO::getTitle, reqVo.getTitle())
                .betweenIfPresent(ImChannelMaterialDO::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImChannelMaterialDO::getId));
    }

}
