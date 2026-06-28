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
import com.fly.mall.api.domain.promotion.DiyTemplate;
import com.fly.mall.api.domain.promotion.bo.DiyTemplateBo;
import com.fly.mall.api.domain.promotion.vo.DiyTemplateVo;
import com.fly.mall.promotion.mapper.DiyTemplateMapper;
import com.fly.mall.promotion.service.IDiyTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 装修模板 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DiyTemplateServiceImpl extends BaseServiceImpl<DiyTemplateMapper, DiyTemplate> implements IDiyTemplateService {

    private final DiyTemplateMapper baseMapper;

    /**
     * 查询装修模板详情。
     */
    @Override
    public DiyTemplateVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询装修模板。
     */
    @Override
    public PageVo<DiyTemplateVo> queryPageList(DiyTemplateBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiyTemplate> lqw = buildQueryWrapper(bo);
        Page<DiyTemplateVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询装修模板列表。
     */
    @Override
    public List<DiyTemplateVo> queryList(DiyTemplateBo bo) {
        LambdaQueryWrapper<DiyTemplate> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改装修模板。
     */
    @Override
    public Boolean saveOrUpdate(DiyTemplateBo bo) {
        DiyTemplate entity = BeanUtil.toBean(bo, DiyTemplate.class);
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
     * 校验并批量删除装修模板。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DiyTemplate entity = new DiyTemplate();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建装修模板查询条件。
     */
    private LambdaQueryWrapper<DiyTemplate> buildQueryWrapper(DiyTemplateBo bo) {
        LambdaQueryWrapper<DiyTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiyTemplate::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DiyTemplate::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DiyTemplate::getName, bo.getName());
        lqw.eq(bo.getUsed() != null, DiyTemplate::getUsed, bo.getUsed());
        lqw.eq(bo.getUsedTime() != null, DiyTemplate::getUsedTime, bo.getUsedTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), DiyTemplate::getRemark, bo.getRemark());
        lqw.like(StringUtils.isNotBlank(bo.getProperty()), DiyTemplate::getProperty, bo.getProperty());
        return lqw;
    }

}
