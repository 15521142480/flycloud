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
import com.fly.mall.api.promotion.domain.KeFuConversation;
import com.fly.mall.api.promotion.domain.bo.KeFuConversationBo;
import com.fly.mall.api.promotion.domain.vo.KeFuConversationVo;
import com.fly.mall.promotion.mapper.KeFuConversationMapper;
import com.fly.mall.promotion.service.IKeFuConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 客服会话 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class KeFuConversationServiceImpl extends BaseServiceImpl<KeFuConversationMapper, KeFuConversation> implements IKeFuConversationService {

    private final KeFuConversationMapper baseMapper;

    /**
     * 查询客服会话详情。
     */
    @Override
    public KeFuConversationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客服会话。
     */
    @Override
    public PageVo<KeFuConversationVo> queryPageList(KeFuConversationBo bo, PageBo pageBo) {
        LambdaQueryWrapper<KeFuConversation> lqw = buildQueryWrapper(bo);
        Page<KeFuConversationVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询客服会话列表。
     */
    @Override
    public List<KeFuConversationVo> queryList(KeFuConversationBo bo) {
        LambdaQueryWrapper<KeFuConversation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改客服会话。
     */
    @Override
    public Boolean saveOrUpdate(KeFuConversationBo bo) {
        KeFuConversation entity = BeanUtil.toBean(bo, KeFuConversation.class);
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
     * 校验并批量删除客服会话。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            KeFuConversation entity = new KeFuConversation();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建客服会话查询条件。
     */
    private LambdaQueryWrapper<KeFuConversation> buildQueryWrapper(KeFuConversationBo bo) {
        LambdaQueryWrapper<KeFuConversation> lqw = Wrappers.lambdaQuery();
        lqw.eq(KeFuConversation::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, KeFuConversation::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, KeFuConversation::getUserId, bo.getUserId());
        lqw.eq(bo.getLastMessageTime() != null, KeFuConversation::getLastMessageTime, bo.getLastMessageTime());
        lqw.like(StringUtils.isNotBlank(bo.getLastMessageContent()), KeFuConversation::getLastMessageContent, bo.getLastMessageContent());
        lqw.eq(bo.getLastMessageContentType() != null, KeFuConversation::getLastMessageContentType, bo.getLastMessageContentType());
        lqw.eq(bo.getAdminPinned() != null, KeFuConversation::getAdminPinned, bo.getAdminPinned());
        lqw.eq(bo.getUserDeleted() != null, KeFuConversation::getUserDeleted, bo.getUserDeleted());
        lqw.eq(bo.getAdminDeleted() != null, KeFuConversation::getAdminDeleted, bo.getAdminDeleted());
        lqw.eq(bo.getAdminUnreadMessageCount() != null, KeFuConversation::getAdminUnreadMessageCount, bo.getAdminUnreadMessageCount());
        return lqw;
    }

}
