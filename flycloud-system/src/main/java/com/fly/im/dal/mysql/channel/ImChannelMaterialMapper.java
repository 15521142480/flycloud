package com.fly.im.dal.mysql.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.channel.material.ImChannelMaterialPageReqVo;
import com.fly.system.api.im.domain.channel.ImChannelMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 频道素材 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImChannelMaterialMapper extends BaseMapperX<ImChannelMaterial> {

    default Long selectCountByChannelId(Long channelId) {
        return selectCount(ImChannelMaterial::getChannelId, channelId);
    }

    default List<ImChannelMaterial> selectListByChannelId(Long channelId) {
        return selectList(new LambdaQueryWrapperX<ImChannelMaterial>()
                .eq(ImChannelMaterial::getChannelId, channelId)
                .orderByDesc(ImChannelMaterial::getId));
    }

    default PageResult<ImChannelMaterial> selectPage(ImChannelMaterialPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImChannelMaterial>()
                .eqIfPresent(ImChannelMaterial::getChannelId, reqVo.getChannelId())
                .eqIfPresent(ImChannelMaterial::getType, reqVo.getType())
                .likeIfPresent(ImChannelMaterial::getTitle, reqVo.getTitle())
                .betweenIfPresent(ImChannelMaterial::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImChannelMaterial::getId));
    }

}
