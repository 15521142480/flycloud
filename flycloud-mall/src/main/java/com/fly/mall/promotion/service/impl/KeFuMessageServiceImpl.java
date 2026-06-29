package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.KeFuMessage;
import com.fly.mall.api.promotion.domain.bo.KeFuMessageBo;
import com.fly.mall.api.promotion.domain.vo.KeFuMessageVo;
import com.fly.mall.promotion.mapper.KeFuMessageMapper;
import com.fly.mall.promotion.service.IKeFuMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 客服消息 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class KeFuMessageServiceImpl extends BaseServiceImpl<KeFuMessageMapper, KeFuMessage> implements IKeFuMessageService {

    private final KeFuMessageMapper baseMapper;

    /**
     * 查询客服消息详情。
     */
    @Override
    public KeFuMessageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客服消息。
     */
    @Override
    public PageVo<KeFuMessageVo> queryPageList(KeFuMessageBo bo, PageBo pageBo) {
        LambdaQueryWrapper<KeFuMessage> lqw = buildQueryWrapper(bo);
        Page<KeFuMessageVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询客服消息列表。
     */
    @Override
    public List<KeFuMessageVo> queryList(KeFuMessageBo bo) {
        LambdaQueryWrapper<KeFuMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改客服消息。
     */
    @Override
    public Boolean saveOrUpdate(KeFuMessageBo bo) {
        KeFuMessage entity = BeanUtil.toBean(bo, KeFuMessage.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除客服消息。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            KeFuMessage entity = new KeFuMessage();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建客服消息查询条件。
     */
    private LambdaQueryWrapper<KeFuMessage> buildQueryWrapper(KeFuMessageBo bo) {
        LambdaQueryWrapper<KeFuMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(KeFuMessage::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, KeFuMessage::getId, bo.getId());
        lqw.eq(bo.getConversationId() != null, KeFuMessage::getConversationId, bo.getConversationId());
        lqw.eq(bo.getSenderId() != null, KeFuMessage::getSenderId, bo.getSenderId());
        lqw.eq(bo.getSenderType() != null, KeFuMessage::getSenderType, bo.getSenderType());
        lqw.eq(bo.getReceiverId() != null, KeFuMessage::getReceiverId, bo.getReceiverId());
        lqw.eq(bo.getReceiverType() != null, KeFuMessage::getReceiverType, bo.getReceiverType());
        lqw.eq(bo.getContentType() != null, KeFuMessage::getContentType, bo.getContentType());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), KeFuMessage::getContent, bo.getContent());
        lqw.eq(bo.getReadStatus() != null, KeFuMessage::getReadStatus, bo.getReadStatus());
        return lqw;
    }

}
