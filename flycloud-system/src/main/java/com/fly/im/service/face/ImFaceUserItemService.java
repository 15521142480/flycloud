package com.fly.im.service.face;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.bo.ImFaceUserItemBo;
import com.fly.system.api.im.domain.bo.ImFaceUserItemManagerPageBo;
import com.fly.system.api.im.domain.ImFaceUserItem;
import jakarta.validation.Valid;

import java.util.List;

/**
 * IM 用户私有表情 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFaceUserItemService {

    /**
     * 获取指定用户的个人表情列表
     *
     * @param userId 用户编号
     * @return 个人表情列表
     */
    List<ImFaceUserItem> getFaceUserItemList(Long userId);

    /**
     * 添加个人表情
     *
     * @param userId 用户编号
     * @param reqVo  添加请求
     * @return 新增表情编号
     */
    Long createFaceUserItem(Long userId, @Valid ImFaceUserItemBo reqVo);

    /**
     * 删除指定用户的某条个人表情
     *
     * @param userId 用户编号
     * @param id     表情编号
     */
    void deleteFaceUserItem(Long userId, Long id);

    // ==================== 管理后台 ====================

    /**
     * 分页查询所有用户的个人表情；管理后台审计 / 删除违规图用
     *
     * @param reqVo 分页查询条件
     * @return 个人表情分页
     */
    PageResult<ImFaceUserItem> getFaceUserItemPage(ImFaceUserItemManagerPageBo reqVo);

    /**
     * 管理后台直接删除某条个人表情；不做归属校验
     *
     * @param id 表情编号
     */
    void deleteFaceUserItem(Long id);

}
