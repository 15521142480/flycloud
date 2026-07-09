package com.fly.im.service.face;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.bo.ImFacePackPageBo;
import com.fly.system.api.im.domain.bo.ImFacePackBo;
import com.fly.system.api.im.domain.ImFacePack;
import com.fly.im.mapper.ImFacePackMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.system.api.im.enums.ErrorCodeConstants.FACE_PACK_HAS_ITEMS;
import static com.fly.system.api.im.enums.ErrorCodeConstants.FACE_PACK_NOT_EXISTS;

/**
 * IM 表情包 Service 实现类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
public class ImFacePackServiceImpl implements ImFacePackService {

    @Resource
    private ImFacePackMapper facePackMapper;

    /**
     * @Lazy 解决与 ImFacePackItemServiceImpl 的循环依赖（item.create / update 校验所属包存在 → 反向调用本类）
     */
    @Resource
    @Lazy
    private ImFacePackItemService facePackItemService;

    // ==================== 用户端 ====================

    @Override
    public List<ImFacePack> getEnabledFacePackList() {
        return facePackMapper.selectListByStatusOrderBySort(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public ImFacePack validateFacePackExists(Long id) {
        ImFacePack pack = facePackMapper.selectById(id);
        if (pack == null) {
            throw exception(FACE_PACK_NOT_EXISTS);
        }
        return pack;
    }

    // ==================== 管理后台 ====================

    @Override
    public PageResult<ImFacePack> getFacePackPage(ImFacePackPageBo reqVo) {
        return facePackMapper.selectPage(reqVo);
    }

    @Override
    public ImFacePack getFacePack(Long id) {
        return facePackMapper.selectById(id);
    }

    @Override
    public Long createFacePack(ImFacePackBo reqVo) {
        ImFacePack pack = BeanUtils.toBean(reqVo, ImFacePack.class);
        pack.setCreateBy(UserUtils.getCurUserIdStr());
        pack.setCreateTime(LocalDateTime.now());
        facePackMapper.insert(pack);
        return pack.getId();
    }

    @Override
    public void updateFacePack(ImFacePackBo reqVo) {
        // 1. 校验存在
        validateFacePackExists(reqVo.getId());

        // 2. 更新
        ImFacePack updateObj = BeanUtils.toBean(reqVo, ImFacePack.class);
        updateObj.setUpdateBy(UserUtils.getCurUserIdStr());
        updateObj.setUpdateTime(LocalDateTime.now());
        facePackMapper.updateById(updateObj);
    }

    @Override
    public void deleteFacePack(Long id) {
        // 1.1 校验存在
        validateFacePackExists(id);
        // 1.2 校验表情包下没有表情；防止误删表情包导致历史 face 消息无法回查归属
        if (facePackItemService.getFacePackItemCount(id) > 0) {
            throw exception(FACE_PACK_HAS_ITEMS);
        }

        // 2. 删除
        facePackMapper.deleteById(id);
    }

    @Override
    public void deleteFacePackList(List<Long> ids) {
        // 1. 任一存在表情则拒绝整批删除，避免「只删一半」的中间态
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        if (facePackItemService.getFacePackItemCount(ids) > 0) {
            throw exception(FACE_PACK_HAS_ITEMS);
        }

        // 2. 删除
        facePackMapper.deleteByIds(ids);
    }

}
