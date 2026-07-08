package com.fly.im.service.face;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemSaveReqVo;
import com.fly.system.api.im.domain.face.ImFacePackItem;
import com.fly.im.dal.mysql.face.ImFacePackItemMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.system.api.im.enums.ErrorCodeConstants.FACE_PACK_ITEM_NOT_EXISTS;

/**
 * IM 表情包项 Service 实现类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
public class ImFacePackItemServiceImpl implements ImFacePackItemService {

    @Resource
    private ImFacePackItemMapper facePackItemMapper;
    @Resource
    private ImFacePackService facePackService;


    // ==================== 用户端 ====================

    @Override
    public List<ImFacePackItem> getEnabledItemListByPackIds(Collection<Long> packIds) {
        if (CollUtil.isEmpty(packIds)) {
            return Collections.emptyList();
        }
        return facePackItemMapper.selectListByPackIdsAndStatus(packIds, CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public Long getFacePackItemCount(Long packId) {
        return facePackItemMapper.selectCountByPackId(packId);
    }

    @Override
    public Long getFacePackItemCount(Collection<Long> packIds) {
        if (CollUtil.isEmpty(packIds)) {
            return 0L;
        }
        return facePackItemMapper.selectCountByPackIds(packIds);
    }

    // ==================== 管理后台 ====================

    @Override
    public PageResult<ImFacePackItem> getFacePackItemPage(ImFacePackItemPageReqVo reqVo) {
        return facePackItemMapper.selectPage(reqVo);
    }

    @Override
    public ImFacePackItem getFacePackItem(Long id) {
        return facePackItemMapper.selectById(id);
    }

    @Override
    public Long createFacePackItem(ImFacePackItemSaveReqVo reqVo) {
        // 1. 校验所属表情包存在
        facePackService.validateFacePackExists(reqVo.getPackId());

        // 2. 入库
        ImFacePackItem item = BeanUtils.toBean(reqVo, ImFacePackItem.class);
        item.setUrl(item.getUrl());
        item.setCreateBy(UserUtils.getCurUserIdStr());
        item.setCreateTime(LocalDateTime.now());
        facePackItemMapper.insert(item);
        return item.getId();
    }

    @Override
    public void updateFacePackItem(ImFacePackItemSaveReqVo reqVo) {
        // 1.1 校验存在
        validateFacePackItemExists(reqVo.getId());
        // 1.2 校验所属表情包存在
        facePackService.validateFacePackExists(reqVo.getPackId());

        // 2. 更新
        ImFacePackItem updateObj = BeanUtils.toBean(reqVo, ImFacePackItem.class);
        updateObj.setUrl(updateObj.getUrl());
        updateObj.setUpdateBy(UserUtils.getCurUserIdStr());
        updateObj.setUpdateTime(LocalDateTime.now());
        facePackItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteFacePackItem(Long id) {
        // 1. 校验存在
        validateFacePackItemExists(id);

        // 2. 删除
        facePackItemMapper.deleteById(id);
    }

    @Override
    public void deleteFacePackItemList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        facePackItemMapper.deleteByIds(ids);
    }

    private void validateFacePackItemExists(Long id) {
        if (facePackItemMapper.selectById(id) == null) {
            throw exception(FACE_PACK_ITEM_NOT_EXISTS);
        }
    }

}
