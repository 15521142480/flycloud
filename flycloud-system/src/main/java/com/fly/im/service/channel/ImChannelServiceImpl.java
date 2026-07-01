package com.fly.im.service.channel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelSaveReqVo;
import com.fly.system.api.im.domain.channel.ImChannel;
import com.fly.im.dal.mysql.channel.ImChannelMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.system.api.im.enums.ErrorCodeConstants.*;

/**
 * IM 频道 Service 实现类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
public class ImChannelServiceImpl implements ImChannelService {

    @Resource
    private ImChannelMapper channelMapper;
    @Resource
    @Lazy // 延迟加载，解决循环依赖
    private ImChannelMaterialService channelMaterialService;

    // ==================== 用户端 ====================

    @Override
    public List<ImChannel> getChannelListByStatus(Integer status) {
        return channelMapper.selectListByStatusOrderBySort(status);
    }

    @Override
    public List<ImChannel> getChannelList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return channelMapper.selectByIds(ids);
    }

    @Override
    public ImChannel validateChannelExists(Long id) {
        ImChannel channel = channelMapper.selectById(id);
        if (channel == null) {
            throw exception(IM_CHANNEL_NOT_EXISTS);
        }
        return channel;
    }

    // ==================== 管理后台 ====================

    @Override
    public PageResult<ImChannel> getChannelPage(ImChannelPageReqVo reqVo) {
        return channelMapper.selectPage(reqVo);
    }

    @Override
    public ImChannel getChannel(Long id) {
        return channelMapper.selectById(id);
    }

    @Override
    public Long createChannel(ImChannelSaveReqVo reqVo) {
        // 校验 code 唯一
        validateCodeUnique(null, reqVo.getCode());

        // 插入
        ImChannel channel = BeanUtils.toBean(reqVo, ImChannel.class);
        channelMapper.insert(channel);
        return channel.getId();
    }

    @Override
    public void updateChannel(ImChannelSaveReqVo reqVo) {
        // 1.1 校验存在
        validateChannelExists(reqVo.getId());
        // 1.2 校验 code 唯一
        validateCodeUnique(reqVo.getId(), reqVo.getCode());

        // 2. 更新
        ImChannel updateObj = BeanUtils.toBean(reqVo, ImChannel.class);
        channelMapper.updateById(updateObj);
    }

    @Override
    public void deleteChannel(Long id) {
        // 1.1 校验存在
        validateChannelExists(id);
        // 1.2 防止误删频道导致历史素材 / 消息回查不到归属
        if (channelMaterialService.getMaterialCountByChannelId(id) > 0) {
            throw exception(IM_CHANNEL_HAS_MATERIAL);
        }

        // 2. 删除频道
        channelMapper.deleteById(id);
    }

    private void validateCodeUnique(Long id, String code) {
        ImChannel exist = channelMapper.selectByCode(code);
        if (exist == null) {
            return;
        }
        if (id == null || ObjUtil.notEqual(exist.getId(), id)) {
            throw exception(IM_CHANNEL_CODE_DUPLICATED, code);
        }
    }

}
