package com.fly.im.service.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.vo.admin.manager.channel.material.ImChannelMaterialPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.channel.material.ImChannelMaterialSaveReqVo;
import com.fly.system.api.im.domain.channel.ImChannelMaterial;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.fly.common.utils.collection.CollectionUtils.convertMap;

/**
 * IM 频道素材 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImChannelMaterialService {

    // ==================== 用户端 ====================

    /**
     * 校验素材存在
     *
     * @param id 素材编号
     * @return 素材 DO
     */
    ImChannelMaterial validateMaterialExists(Long id);

    /**
     * 按编号批量查询素材
     *
     * @param ids 素材编号列表
     * @return 素材列表
     */
    List<ImChannelMaterial> getMaterialList(Collection<Long> ids);

    /**
     * 按编号批量查询素材 Map
     *
     * @param ids 素材编号列表
     * @return id -> 素材 Map
     */
   default Map<Long, ImChannelMaterial> getMaterialMap(Collection<Long> ids) {
       return convertMap(getMaterialList(ids), ImChannelMaterial::getId);
   }

    /**
     * 统计指定频道下的素材数量
     *
     * @param channelId 频道编号
     * @return 数量
     */
    Long getMaterialCountByChannelId(Long channelId);

    // ==================== 管理后台 ====================

    /**
     * 按频道查询素材精简列表
     *
     * @param channelId 频道编号
     * @return 素材列表
     */
    List<ImChannelMaterial> getMaterialListByChannelId(Long channelId);

    /**
     * 分页查询素材
     *
     * @param reqVo 分页查询条件
     * @return 素材分页
     */
    PageResult<ImChannelMaterial> getMaterialPage(ImChannelMaterialPageReqVo reqVo);

    /**
     * 获取素材详情（含 content 富文本）
     *
     * @param id 素材编号
     * @return 素材 DO
     */
    ImChannelMaterial getMaterial(Long id);

    /**
     * 新增素材
     *
     * @param reqVo 新增请求
     * @return 新增素材编号
     */
    Long createMaterial(@Valid ImChannelMaterialSaveReqVo reqVo);

    /**
     * 修改素材
     *
     * @param reqVo 修改请求
     */
    void updateMaterial(@Valid ImChannelMaterialSaveReqVo reqVo);

    /**
     * 删除素材；素材已被推送过时拒绝，避免历史消息无法回查
     *
     * @param id 素材编号
     */
    void deleteMaterial(Long id);

}
