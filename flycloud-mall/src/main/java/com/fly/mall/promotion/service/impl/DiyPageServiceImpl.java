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
import com.fly.mall.api.domain.promotion.DiyPage;
import com.fly.mall.api.domain.promotion.bo.DiyPageBo;
import com.fly.mall.api.domain.promotion.vo.DiyPageVo;
import com.fly.mall.promotion.mapper.DiyPageMapper;
import com.fly.mall.promotion.service.IDiyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 装修页面 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DiyPageServiceImpl extends BaseServiceImpl<DiyPageMapper, DiyPage> implements IDiyPageService {

    private final DiyPageMapper baseMapper;

    /**
     * 查询装修页面详情。
     */
    @Override
    public DiyPageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询装修页面。
     */
    @Override
    public PageVo<DiyPageVo> queryPageList(DiyPageBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiyPage> lqw = buildQueryWrapper(bo);
        Page<DiyPageVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询装修页面列表。
     */
    @Override
    public List<DiyPageVo> queryList(DiyPageBo bo) {
        LambdaQueryWrapper<DiyPage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改装修页面。
     */
    @Override
    public Boolean saveOrUpdate(DiyPageBo bo) {
        DiyPage entity = BeanUtil.toBean(bo, DiyPage.class);
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
     * 校验并批量删除装修页面。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DiyPage entity = new DiyPage();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建装修页面查询条件。
     */
    private LambdaQueryWrapper<DiyPage> buildQueryWrapper(DiyPageBo bo) {
        LambdaQueryWrapper<DiyPage> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiyPage::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DiyPage::getId, bo.getId());
        lqw.eq(bo.getTemplateId() != null, DiyPage::getTemplateId, bo.getTemplateId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DiyPage::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), DiyPage::getRemark, bo.getRemark());
        lqw.like(StringUtils.isNotBlank(bo.getProperty()), DiyPage::getProperty, bo.getProperty());
        return lqw;
    }

}
