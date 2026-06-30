package com.fly.im.service.channel;

import cn.hutool.core.collection.CollUtil;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialPageReqVo;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialSaveReqVo;
import com.fly.im.dal.dataobject.channel.ImChannelMaterialDO;
import com.fly.im.dal.mysql.channel.ImChannelMaterialMapper;
import com.fly.im.dal.mysql.message.ImChannelMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.im.enums.ErrorCodeConstants.IM_CHANNEL_MATERIAL_NOT_EXISTS;
import static com.fly.im.enums.ErrorCodeConstants.IM_CHANNEL_MATERIAL_USED;

/**
 * IM 频道素材 Service 实现类
 *
 * @author lxs
 * @date 2026-06-30
 */
@Service
@Validated
public class ImChannelMaterialServiceImpl implements ImChannelMaterialService {

    @Resource
    private ImChannelMaterialMapper channelMaterialMapper;
    @Resource
    private ImChannelService channelService;
    @Resource
    private ImChannelMessageMapper channelMessageMapper;

    // ==================== 用户端 ====================

    @Override
    public ImChannelMaterialDO validateMaterialExists(Long id) {
        ImChannelMaterialDO material = channelMaterialMapper.selectById(id);
        if (material == null) {
            throw exception(IM_CHANNEL_MATERIAL_NOT_EXISTS);
        }
        return material;
    }

    @Override
    public List<ImChannelMaterialDO> getMaterialList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return channelMaterialMapper.selectByIds(ids);
    }

    @Override
    public Long getMaterialCountByChannelId(Long channelId) {
        return channelMaterialMapper.selectCountByChannelId(channelId);
    }

    // ==================== 管理后台 ====================

    @Override
    public List<ImChannelMaterialDO> getMaterialListByChannelId(Long channelId) {
        return channelMaterialMapper.selectListByChannelId(channelId);
    }

    @Override
    public PageResult<ImChannelMaterialDO> getMaterialPage(ImChannelMaterialPageReqVo reqVo) {
        return channelMaterialMapper.selectPage(reqVo);
    }

    @Override
    public ImChannelMaterialDO getMaterial(Long id) {
        return channelMaterialMapper.selectById(id);
    }

    @Override
    public Long createMaterial(ImChannelMaterialSaveReqVo reqVo) {
        // 1. 校验所属频道存在
        channelService.validateChannelExists(reqVo.getChannelId());

        // 2. 插入素材
        ImChannelMaterialDO material = BeanUtils.toBean(reqVo, ImChannelMaterialDO.class);
        channelMaterialMapper.insert(material);
        return material.getId();
    }

    @Override
    public void updateMaterial(ImChannelMaterialSaveReqVo reqVo) {
        // 1.1 校验存在
        validateMaterialExists(reqVo.getId());
        // 1.2 校验所属频道存在
        channelService.validateChannelExists(reqVo.getChannelId());

        // 2. 更新素材
        ImChannelMaterialDO updateObj = BeanUtils.toBean(reqVo, ImChannelMaterialDO.class);
        channelMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterial(Long id) {
        validateMaterialExists(id);
        // 防止删除素材导致历史 channel_message 反查不到内容
        if (channelMessageMapper.selectCountByMaterialId(id) > 0) {
            throw exception(IM_CHANNEL_MATERIAL_USED);
        }
        channelMaterialMapper.deleteById(id);
    }

}
