package com.fly.im.service.face;

import cn.hutool.core.collection.CollUtil;
import com.fly.im.framework.enums.CommonStatusEnum;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.face.vo.pack.ImFacePackPageReqVo;
import com.fly.im.controller.admin.manager.face.vo.pack.ImFacePackSaveReqVo;
import com.fly.im.dal.dataobject.face.ImFacePackDO;
import com.fly.im.dal.mysql.face.ImFacePackMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.im.enums.ErrorCodeConstants.FACE_PACK_HAS_ITEMS;
import static com.fly.im.enums.ErrorCodeConstants.FACE_PACK_NOT_EXISTS;

/**
 * IM 表情包 Service 实现类
 *
 * @author lxs
 * @date 2026-06-30
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
    public List<ImFacePackDO> getEnabledFacePackList() {
        return facePackMapper.selectListByStatusOrderBySort(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public ImFacePackDO validateFacePackExists(Long id) {
        ImFacePackDO pack = facePackMapper.selectById(id);
        if (pack == null) {
            throw exception(FACE_PACK_NOT_EXISTS);
        }
        return pack;
    }

    // ==================== 管理后台 ====================

    @Override
    public PageResult<ImFacePackDO> getFacePackPage(ImFacePackPageReqVo reqVo) {
        return facePackMapper.selectPage(reqVo);
    }

    @Override
    public ImFacePackDO getFacePack(Long id) {
        return facePackMapper.selectById(id);
    }

    @Override
    public Long createFacePack(ImFacePackSaveReqVo reqVo) {
        ImFacePackDO pack = BeanUtils.toBean(reqVo, ImFacePackDO.class);
        facePackMapper.insert(pack);
        return pack.getId();
    }

    @Override
    public void updateFacePack(ImFacePackSaveReqVo reqVo) {
        // 1. 校验存在
        validateFacePackExists(reqVo.getId());

        // 2. 更新
        ImFacePackDO updateObj = BeanUtils.toBean(reqVo, ImFacePackDO.class);
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
