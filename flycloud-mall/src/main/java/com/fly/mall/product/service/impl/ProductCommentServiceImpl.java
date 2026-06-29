package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.product.domain.ProductComment;
import com.fly.mall.api.product.domain.bo.ProductCommentBo;
import com.fly.mall.api.product.domain.vo.ProductCommentVo;
import com.fly.mall.product.mapper.ProductCommentMapper;
import com.fly.mall.product.service.IProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 商品评价 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductCommentServiceImpl extends BaseServiceImpl<ProductCommentMapper, ProductComment> implements IProductCommentService {

    private final ProductCommentMapper baseMapper;

    /**
     * 查询商品评价详情。
     */
    @Override
    public ProductCommentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品评价。
     */
    @Override
    public PageVo<ProductCommentVo> queryPageList(ProductCommentBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductComment> lqw = buildQueryWrapper(bo);
        Page<ProductCommentVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品评价列表。
     */
    @Override
    public List<ProductCommentVo> queryList(ProductCommentBo bo) {
        LambdaQueryWrapper<ProductComment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品评价。
     */
    @Override
    public Boolean saveOrUpdate(ProductCommentBo bo) {
        ProductComment entity = BeanUtil.toBean(bo, ProductComment.class);
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
     * 校验并批量删除商品评价。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductComment entity = new ProductComment();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品评价查询条件。
     */
    private LambdaQueryWrapper<ProductComment> buildQueryWrapper(ProductCommentBo bo) {
        LambdaQueryWrapper<ProductComment> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductComment::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductComment::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, ProductComment::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserNickname()), ProductComment::getUserNickname, bo.getUserNickname());
        lqw.like(StringUtils.isNotBlank(bo.getUserAvatar()), ProductComment::getUserAvatar, bo.getUserAvatar());
        lqw.eq(bo.getAnonymous() != null, ProductComment::getAnonymous, bo.getAnonymous());
        lqw.eq(bo.getOrderId() != null, ProductComment::getOrderId, bo.getOrderId());
        lqw.eq(bo.getOrderItemId() != null, ProductComment::getOrderItemId, bo.getOrderItemId());
        lqw.eq(bo.getSpuId() != null, ProductComment::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuName()), ProductComment::getSpuName, bo.getSpuName());
        lqw.eq(bo.getSkuId() != null, ProductComment::getSkuId, bo.getSkuId());
        lqw.like(StringUtils.isNotBlank(bo.getSkuPicUrl()), ProductComment::getSkuPicUrl, bo.getSkuPicUrl());
        lqw.eq(bo.getVisible() != null, ProductComment::getVisible, bo.getVisible());
        lqw.eq(bo.getScores() != null, ProductComment::getScores, bo.getScores());
        lqw.eq(bo.getDescriptionScores() != null, ProductComment::getDescriptionScores, bo.getDescriptionScores());
        lqw.eq(bo.getBenefitScores() != null, ProductComment::getBenefitScores, bo.getBenefitScores());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), ProductComment::getContent, bo.getContent());
        lqw.eq(bo.getReplyStatus() != null, ProductComment::getReplyStatus, bo.getReplyStatus());
        lqw.eq(bo.getReplyUserId() != null, ProductComment::getReplyUserId, bo.getReplyUserId());
        lqw.like(StringUtils.isNotBlank(bo.getReplyContent()), ProductComment::getReplyContent, bo.getReplyContent());
        lqw.eq(bo.getReplyTime() != null, ProductComment::getReplyTime, bo.getReplyTime());
        return lqw;
    }

}
