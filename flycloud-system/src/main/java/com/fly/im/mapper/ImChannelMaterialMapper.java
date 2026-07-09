package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImChannelMaterialPageBo;
import com.fly.system.api.im.domain.ImChannelMaterial;
import com.fly.system.api.im.domain.vo.ImChannelMaterialVo;

import java.util.List;

/**
 * IM 频道素材 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImChannelMaterialMapper extends BaseMapperPlus<ImChannelMaterialMapper, ImChannelMaterial, ImChannelMaterialVo> {

    default Long selectCountByChannelId(Long channelId) {
        return selectCount(ImChannelMaterial::getChannelId, channelId);
    }

    default List<ImChannelMaterial> selectListByChannelId(Long channelId) {
        return selectList(new LambdaQueryWrapperX<ImChannelMaterial>()
                .eq(ImChannelMaterial::getChannelId, channelId)
                .orderByDesc(ImChannelMaterial::getId));
    }

    default PageResult<ImChannelMaterial> selectPage(ImChannelMaterialPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImChannelMaterial>()
                .eqIfPresent(ImChannelMaterial::getChannelId, reqVo.getChannelId())
                .eqIfPresent(ImChannelMaterial::getType, reqVo.getType())
                .likeIfPresent(ImChannelMaterial::getTitle, reqVo.getTitle())
                .betweenIfPresent(ImChannelMaterial::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImChannelMaterial::getId)));
    }

}
