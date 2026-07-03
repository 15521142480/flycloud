package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.DiyPage;
import com.fly.mall.api.promotion.domain.bo.DiyPageBo;
import com.fly.mall.api.promotion.domain.vo.AppDiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageVo;
import com.fly.mall.promotion.mapper.DiyPageMapper;
import com.fly.mall.promotion.service.IDiyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 装修页面 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
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
        return baseMapper.selectVoOne(Wrappers.<DiyPage>lambdaQuery()
            .eq(DiyPage::getId, id)
            .eq(DiyPage::getIsDeleted, false));
    }

    /**
     * 查询装修页面响应详情。
     */
    @Override
    public DiyPageRespVo queryRespById(Long id) {
        return toRespVo(validateExists(id));
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
     * 分页查询装修页面响应列表。
     */
    @Override
    public PageVo<DiyPageRespVo> queryRespPageList(DiyPageBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiyPage> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(DiyPage::getId);
        Page<DiyPage> result = baseMapper.selectPage(pageBo.<DiyPage>build(), lqw);
        PageVo<DiyPageRespVo> pageVo = new PageVo<>();
        pageVo.setPages(result.getPages());
        pageVo.setTotal(result.getTotal());
        pageVo.setList(result.getRecords().stream().map(this::toRespVo).collect(Collectors.toList()));
        return pageVo;
    }

    /**
     * 查询装修页面列表。
     */
    @Override
    public List<DiyPageVo> queryList(DiyPageBo bo) {
        LambdaQueryWrapper<DiyPage> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(DiyPage::getTemplateId, DiyPage::getId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询装修页面响应列表。
     */
    @Override
    public List<DiyPageRespVo> queryRespList(DiyPageBo bo) {
        LambdaQueryWrapper<DiyPage> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(DiyPage::getTemplateId, DiyPage::getId);
        return baseMapper.selectList(lqw).stream().map(this::toRespVo).collect(Collectors.toList());
    }

    /**
     * 创建装修页面。
     */
    @Override
    public Long createDiyPage(DiyPageBo bo) {
        validateNameUnique(null, bo.getTemplateId(), bo.getName());
        DiyPage entity = BeanUtil.toBean(bo, DiyPage.class);
        if (!StringUtils.isNotBlank(entity.getProperty())) {
            entity.setProperty("{}");
        }
        fillCreateInfo(entity);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 修改装修页面。
     */
    @Override
    public Boolean updateDiyPage(DiyPageBo bo) {
        validateExists(bo.getId());
        validateNameUnique(bo.getId(), bo.getTemplateId(), bo.getName());
        DiyPage entity = BeanUtil.toBean(bo, DiyPage.class);
        fillUpdateInfo(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 删除装修页面。
     */
    @Override
    public Boolean deleteDiyPage(Long id) {
        validateExists(id);
        DiyPage entity = new DiyPage();
        entity.setId(id);
        entity.setIsDeleted(true);
        fillUpdateInfo(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 查询装修页面属性。
     */
    @Override
    public DiyPageVo queryPropertyById(Long id) {
        return queryById(id);
    }

    /**
     * 查询后台装修页面属性响应。
     */
    @Override
    public DiyPagePropertyRespVo queryPropertyRespById(Long id) {
        return toPropertyRespVo(validateExists(id));
    }

    /**
     * 查询移动端装修页面属性响应。
     */
    @Override
    public AppDiyPagePropertyRespVo queryAppPropertyRespById(Long id) {
        return toAppPropertyRespVo(validateExists(id));
    }

    /**
     * 修改装修页面属性。
     */
    @Override
    public Boolean updateDiyPageProperty(DiyPageBo bo) {
        validateExists(bo.getId());
        DiyPage entity = new DiyPage();
        entity.setId(bo.getId());
        entity.setProperty(bo.getProperty());
        entity.setPreviewPicUrls(bo.getPreviewPicUrls());
        fillUpdateInfo(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 根据编号集合查询装修页面。
     */
    @Override
    public List<DiyPageVo> queryListByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.selectVoList(Wrappers.<DiyPage>lambdaQuery()
            .in(DiyPage::getId, ids)
            .eq(DiyPage::getIsDeleted, false)
            .orderByAsc(DiyPage::getId));
    }

    /**
     * 根据编号集合查询装修页面响应列表。
     */
    @Override
    public List<DiyPageRespVo> queryRespListByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DiyPage>lambdaQuery()
                .in(DiyPage::getId, ids)
                .eq(DiyPage::getIsDeleted, false)
                .orderByAsc(DiyPage::getId))
            .stream()
            .map(this::toRespVo)
            .collect(Collectors.collectingAndThen(Collectors.toList(), list -> list));
    }

    /**
     * 根据模板编号查询装修页面。
     */
    @Override
    public List<DiyPageVo> queryListByTemplateId(Long templateId) {
        if (templateId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectVoList(Wrappers.<DiyPage>lambdaQuery()
            .eq(DiyPage::getTemplateId, templateId)
            .eq(DiyPage::getIsDeleted, false)
            .orderByAsc(DiyPage::getId));
    }

    /**
     * 新增或修改装修页面。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(DiyPageBo bo) {
        if (bo.getId() != null) {
            return updateDiyPage(bo);
        }
        createDiyPage(bo);
        return true;
    }

    /**
     * 校验并批量删除装修页面。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            deleteDiyPage(id);
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

    /**
     * 转换为后台装修页面响应对象。
     */
    private DiyPageRespVo toRespVo(DiyPage entity) {
        return BeanUtil.toBean(entity, DiyPageRespVo.class);
    }

    /**
     * 转换为后台装修页面属性响应对象。
     */
    private DiyPagePropertyRespVo toPropertyRespVo(DiyPage entity) {
        return BeanUtil.toBean(entity, DiyPagePropertyRespVo.class);
    }

    /**
     * 转换为移动端装修页面属性响应对象。
     */
    private AppDiyPagePropertyRespVo toAppPropertyRespVo(DiyPage entity) {
        return BeanUtil.toBean(entity, AppDiyPagePropertyRespVo.class);
    }

    /**
     * 校验装修页面是否存在。
     */
    private DiyPage validateExists(Long id) {
        if (id == null) {
            throw new ServiceException("装修页面编号不能为空");
        }
        DiyPage entity = baseMapper.selectOne(Wrappers.<DiyPage>lambdaQuery()
            .eq(DiyPage::getId, id)
            .eq(DiyPage::getIsDeleted, false));
        if (entity == null) {
            throw new ServiceException("装修页面不存在");
        }
        return entity;
    }

    /**
     * 校验同一个模板下的装修页面名称不能重复。
     */
    private void validateNameUnique(Long id, Long templateId, String name) {
        if (!StringUtils.isNotBlank(name)) {
            return;
        }
        LambdaQueryWrapper<DiyPage> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiyPage::getName, name);
        lqw.eq(DiyPage::getIsDeleted, false);
        if (templateId == null) {
            lqw.isNull(DiyPage::getTemplateId);
        } else {
            lqw.eq(DiyPage::getTemplateId, templateId);
        }
        lqw.ne(id != null, DiyPage::getId, id);
        if (baseMapper.selectCount(lqw) > 0) {
            throw new ServiceException("装修页面名称已存在：" + name);
        }
    }

    /**
     * 填充新增装修页面审计字段。
     */
    private void fillCreateInfo(DiyPage entity) {
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        entity.setIsDeleted(false);
    }

    /**
     * 填充修改装修页面审计字段。
     */
    private void fillUpdateInfo(DiyPage entity) {
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
    }

}
