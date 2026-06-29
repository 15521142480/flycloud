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
import com.fly.mall.api.promotion.domain.ArticleCategory;
import com.fly.mall.api.promotion.domain.bo.ArticleCategoryBo;
import com.fly.mall.api.promotion.domain.vo.ArticleCategoryVo;
import com.fly.mall.promotion.mapper.ArticleCategoryMapper;
import com.fly.mall.promotion.service.IArticleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 文章分类 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategoryMapper, ArticleCategory> implements IArticleCategoryService {

    private final ArticleCategoryMapper baseMapper;

    /**
     * 查询文章分类详情。
     */
    @Override
    public ArticleCategoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询文章分类。
     */
    @Override
    public PageVo<ArticleCategoryVo> queryPageList(ArticleCategoryBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ArticleCategory> lqw = buildQueryWrapper(bo);
        Page<ArticleCategoryVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询文章分类列表。
     */
    @Override
    public List<ArticleCategoryVo> queryList(ArticleCategoryBo bo) {
        LambdaQueryWrapper<ArticleCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改文章分类。
     */
    @Override
    public Boolean saveOrUpdate(ArticleCategoryBo bo) {
        ArticleCategory entity = BeanUtil.toBean(bo, ArticleCategory.class);
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
     * 校验并批量删除文章分类。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ArticleCategory entity = new ArticleCategory();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建文章分类查询条件。
     */
    private LambdaQueryWrapper<ArticleCategory> buildQueryWrapper(ArticleCategoryBo bo) {
        LambdaQueryWrapper<ArticleCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ArticleCategory::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ArticleCategory::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ArticleCategory::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), ArticleCategory::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getStatus() != null, ArticleCategory::getStatus, bo.getStatus());
        lqw.eq(bo.getSort() != null, ArticleCategory::getSort, bo.getSort());
        return lqw;
    }

}
