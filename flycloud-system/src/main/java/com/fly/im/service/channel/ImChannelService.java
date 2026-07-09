package com.fly.im.service.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.bo.ImChannelPageBo;
import com.fly.system.api.im.domain.bo.ImChannelBo;
import com.fly.system.api.im.domain.ImChannel;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.fly.common.utils.collection.CollectionUtils.convertMap;

/**
 * IM 频道 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImChannelService {

    // ==================== 用户端 ====================

    /**
     * 按状态查询频道列表，按 sort 升序
     *
     * @param status 状态；对应 CommonStatusEnum
     * @return 频道列表
     */
    List<ImChannel> getChannelListByStatus(Integer status);

    /**
     * 按编号批量查询频道
     *
     * @param ids 频道编号列表
     * @return 频道列表
     */
    List<ImChannel> getChannelList(Collection<Long> ids);

    /**
     * 按编号批量查询频道 Map
     *
     * @param ids 频道编号列表
     * @return id -> 频道 Map
     */
    default Map<Long, ImChannel> getChannelMap(Collection<Long> ids) {
        return convertMap(getChannelList(ids), ImChannel::getId);
    }

    /**
     * 校验频道存在
     *
     * @param id 频道编号
     * @return 频道 DO
     */
    @SuppressWarnings("UnusedReturnValue")
    ImChannel validateChannelExists(Long id);

    // ==================== 管理后台 ====================

    /**
     * 分页查询频道
     *
     * @param reqVo 分页查询条件
     * @return 频道分页
     */
    PageResult<ImChannel> getChannelPage(ImChannelPageBo reqVo);

    /**
     * 获取频道详情
     *
     * @param id 频道编号
     * @return 频道 DO
     */
    ImChannel getChannel(Long id);

    /**
     * 新增频道
     *
     * @param reqVo 新增请求
     * @return 新增频道编号
     */
    Long createChannel(@Valid ImChannelBo reqVo);

    /**
     * 修改频道
     *
     * @param reqVo 修改请求
     */
    void updateChannel(@Valid ImChannelBo reqVo);

    /**
     * 删除频道；频道下有素材或消息时拒绝
     *
     * @param id 频道编号
     */
    void deleteChannel(Long id);

}
