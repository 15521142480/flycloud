package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.DiyTemplate;
import com.fly.mall.api.promotion.domain.bo.DiyPageBo;
import com.fly.mall.api.promotion.domain.bo.DiyTemplateBo;
import com.fly.mall.api.promotion.domain.vo.AppDiyTemplatePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplatePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplateRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplateVo;
import com.fly.mall.promotion.mapper.DiyTemplateMapper;
import com.fly.mall.promotion.service.IDiyPageService;
import com.fly.mall.promotion.service.IDiyTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 装修模板 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class DiyTemplateServiceImpl extends BaseServiceImpl<DiyTemplateMapper, DiyTemplate> implements IDiyTemplateService {

    private static final String DEFAULT_PROPERTY = "{}";
    private static final String INDEX_PAGE_NAME = "首页";
    private static final String MY_PAGE_NAME = "我的";

    private final DiyTemplateMapper baseMapper;
    private final IDiyPageService diyPageService;

    /**
     * 查询装修模板详情。
     */
    @Override
    public DiyTemplateVo queryById(Long id) {
        return baseMapper.selectVoOne(Wrappers.<DiyTemplate>lambdaQuery()
            .eq(DiyTemplate::getId, id)
            .eq(DiyTemplate::getIsDeleted, false));
    }

    /**
     * 查询装修模板响应详情。
     */
    @Override
    public DiyTemplateRespVo queryRespById(Long id) {
        return toRespVo(validateExists(id));
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
     * 分页查询装修模板响应列表。
     */
    @Override
    public PageVo<DiyTemplateRespVo> queryRespPageList(DiyTemplateBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiyTemplate> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(DiyTemplate::getUsed, DiyTemplate::getId);
        Page<DiyTemplate> result = baseMapper.selectPage(pageBo.<DiyTemplate>build(), lqw);
        PageVo<DiyTemplateRespVo> pageVo = new PageVo<>();
        pageVo.setPages(result.getPages());
        pageVo.setTotal(result.getTotal());
        pageVo.setList(result.getRecords().stream().map(this::toRespVo).collect(Collectors.toList()));
        return pageVo;
    }

    /**
     * 查询装修模板列表。
     */
    @Override
    public List<DiyTemplateVo> queryList(DiyTemplateBo bo) {
        LambdaQueryWrapper<DiyTemplate> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(DiyTemplate::getUsed, DiyTemplate::getId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询装修模板响应列表。
     */
    @Override
    public List<DiyTemplateRespVo> queryRespList(DiyTemplateBo bo) {
        LambdaQueryWrapper<DiyTemplate> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(DiyTemplate::getUsed, DiyTemplate::getId);
        return baseMapper.selectList(lqw).stream().map(this::toRespVo).collect(Collectors.toList());
    }

    /**
     * 创建装修模板，并同步创建首页和我的两个默认装修页面。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDiyTemplate(DiyTemplateBo bo) {
        validateNameUnique(null, bo.getName());
        DiyTemplate entity = BeanUtil.toBean(bo, DiyTemplate.class);
        entity.setUsed(false);
        entity.setUsedTime(null);
        if (!StringUtils.isNotBlank(entity.getProperty())) {
            entity.setProperty(DEFAULT_PROPERTY);
        }
        fillCreateInfo(entity);
        baseMapper.insert(entity);
        createDefaultPage(entity.getId(), entity.getName(), INDEX_PAGE_NAME);
        createDefaultPage(entity.getId(), entity.getName(), MY_PAGE_NAME);
        return entity.getId();
    }

    /**
     * 修改装修模板基础信息。
     */
    @Override
    public Boolean updateDiyTemplate(DiyTemplateBo bo) {
        validateExists(bo.getId());
        validateNameUnique(bo.getId(), bo.getName());
        DiyTemplate entity = BeanUtil.toBean(bo, DiyTemplate.class);
        entity.setUsed(null);
        entity.setUsedTime(null);
        fillUpdateInfo(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 删除装修模板，正在使用的模板不允许删除。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDiyTemplate(Long id) {
        DiyTemplate entity = validateExists(id);
        if (Boolean.TRUE.equals(entity.getUsed())) {
            throw new ServiceException("正在使用的装修模板不能删除");
        }
        DiyTemplate update = new DiyTemplate();
        update.setId(id);
        update.setIsDeleted(true);
        fillUpdateInfo(update);
        boolean success = baseMapper.updateById(update) > 0;
        for (DiyPageVo page : diyPageService.queryListByTemplateId(id)) {
            diyPageService.deleteDiyPage(page.getId());
        }
        return success;
    }

    /**
     * 启用装修模板，同一时间只允许一个模板生效。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean useDiyTemplate(Long id) {
        validateExists(id);
        DiyTemplateVo usedTemplate = queryUsedTemplate();
        if (usedTemplate != null && id.equals(usedTemplate.getId())) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        if (usedTemplate != null) {
            LambdaUpdateWrapper<DiyTemplate> oldWrapper = Wrappers.lambdaUpdate();
            oldWrapper.set(DiyTemplate::getUsed, false)
                .set(DiyTemplate::getUsedTime, null)
                .set(DiyTemplate::getUpdateBy, userId)
                .set(DiyTemplate::getUpdateTime, now)
                .eq(DiyTemplate::getId, usedTemplate.getId())
                .eq(DiyTemplate::getIsDeleted, false);
            baseMapper.update(null, oldWrapper);
        }
        LambdaUpdateWrapper<DiyTemplate> newWrapper = Wrappers.lambdaUpdate();
        newWrapper.set(DiyTemplate::getUsed, true)
            .set(DiyTemplate::getUsedTime, now)
            .set(DiyTemplate::getUpdateBy, userId)
            .set(DiyTemplate::getUpdateTime, now)
            .eq(DiyTemplate::getId, id)
            .eq(DiyTemplate::getIsDeleted, false);
        return baseMapper.update(null, newWrapper) > 0;
    }

    /**
     * 查询装修模板属性，并附带模板下的装修页面配置。
     */
    @Override
    public DiyTemplateVo queryPropertyById(Long id) {
        return fillTemplatePages(queryById(id));
    }

    /**
     * 查询后台装修模板属性响应。
     */
    @Override
    public DiyTemplatePropertyRespVo queryPropertyRespById(Long id) {
        return toPropertyRespVo(validateExists(id));
    }

    /**
     * 查询移动端装修模板属性响应。
     */
    @Override
    public AppDiyTemplatePropertyRespVo queryAppPropertyRespById(Long id) {
        return toAppPropertyRespVo(validateExists(id));
    }

    /**
     * 修改装修模板属性。
     */
    @Override
    public Boolean updateDiyTemplateProperty(DiyTemplateBo bo) {
        validateExists(bo.getId());
        DiyTemplate entity = new DiyTemplate();
        entity.setId(bo.getId());
        entity.setProperty(bo.getProperty());
        entity.setPreviewPicUrls(bo.getPreviewPicUrls());
        fillUpdateInfo(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 查询正在使用的装修模板。
     */
    @Override
    public DiyTemplateVo queryUsedTemplate() {
        return baseMapper.selectVoOne(Wrappers.<DiyTemplate>lambdaQuery()
            .eq(DiyTemplate::getUsed, true)
            .eq(DiyTemplate::getIsDeleted, false)
            .orderByDesc(DiyTemplate::getUsedTime)
            .last("limit 1"));
    }

    /**
     * 查询正在使用的装修模板属性。
     */
    @Override
    public DiyTemplateVo queryUsedTemplateProperty() {
        return fillTemplatePages(queryUsedTemplate());
    }

    /**
     * 查询移动端正在使用的装修模板属性响应。
     */
    @Override
    public AppDiyTemplatePropertyRespVo queryUsedAppTemplateProperty() {
        DiyTemplateVo usedTemplate = queryUsedTemplate();
        if (usedTemplate == null) {
            return null;
        }
        return queryAppPropertyRespById(usedTemplate.getId());
    }

    /**
     * 新增或修改装修模板。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(DiyTemplateBo bo) {
        if (bo.getId() != null) {
            return updateDiyTemplate(bo);
        }
        createDiyTemplate(bo);
        return true;
    }

    /**
     * 校验并批量删除装修模板。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            deleteDiyTemplate(id);
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

    /**
     * 创建模板默认装修页面。
     */
    private void createDefaultPage(Long templateId, String templateName, String pageName) {
        DiyPageBo pageBo = new DiyPageBo();
        pageBo.setTemplateId(templateId);
        pageBo.setName(pageName);
        pageBo.setRemark("模板【" + templateName + "】自动创建");
        pageBo.setProperty(DEFAULT_PROPERTY);
        diyPageService.createDiyPage(pageBo);
    }

    /**
     * 填充模板下的装修页面和移动端常用页面属性。
     */
    private DiyTemplateVo fillTemplatePages(DiyTemplateVo template) {

        if (template == null || template.getId() == null) {
            return template;
        }
        List<DiyPageVo> pages = diyPageService.queryListByTemplateId(template.getId());
        template.setPages(pages);
        for (DiyPageVo page : pages) {
            if (INDEX_PAGE_NAME.equals(page.getName())) {
                template.setHome(page.getProperty());
            } else if (MY_PAGE_NAME.equals(page.getName())) {
                template.setUser(page.getProperty());
            }
        }
        return template;
    }

    /**
     * 转换为后台装修模板响应对象。
     */
    private DiyTemplateRespVo toRespVo(DiyTemplate entity) {
        return BeanUtil.toBean(entity, DiyTemplateRespVo.class);
    }

    /**
     * 转换为后台装修模板属性响应对象。
     */
    private DiyTemplatePropertyRespVo toPropertyRespVo(DiyTemplate entity) {
        DiyTemplatePropertyRespVo respVo = BeanUtil.toBean(entity, DiyTemplatePropertyRespVo.class);
        List<DiyPagePropertyRespVo> pages = diyPageService.queryListByTemplateId(entity.getId()).stream()
            .map(this::toPagePropertyRespVo)
            .collect(Collectors.toList());
        respVo.setPages(pages);
        return respVo;
    }

    /**
     * 转换为移动端装修模板属性响应对象。
     */
    private AppDiyTemplatePropertyRespVo toAppPropertyRespVo(DiyTemplate entity) {
        AppDiyTemplatePropertyRespVo respVo = BeanUtil.toBean(entity, AppDiyTemplatePropertyRespVo.class);
        for (DiyPageVo page : diyPageService.queryListByTemplateId(entity.getId())) {
            if (INDEX_PAGE_NAME.equals(page.getName())) {
                respVo.setHome(page.getProperty());
            } else if (MY_PAGE_NAME.equals(page.getName())) {
                respVo.setUser(page.getProperty());
            }
        }
        return respVo;
    }

    /**
     * 转换为后台装修页面属性响应对象。
     */
    private DiyPagePropertyRespVo toPagePropertyRespVo(DiyPageVo page) {
        return BeanUtil.toBean(page, DiyPagePropertyRespVo.class);
    }

    /**
     * 校验装修模板是否存在。
     */
    private DiyTemplate validateExists(Long id) {
        if (id == null) {
            throw new ServiceException("装修模板编号不能为空");
        }
        DiyTemplate entity = baseMapper.selectOne(Wrappers.<DiyTemplate>lambdaQuery()
            .eq(DiyTemplate::getId, id)
            .eq(DiyTemplate::getIsDeleted, false));
        if (entity == null) {
            throw new ServiceException("装修模板不存在");
        }
        return entity;
    }

    /**
     * 校验装修模板名称不能重复。
     */
    private void validateNameUnique(Long id, String name) {
        if (!StringUtils.isNotBlank(name)) {
            return;
        }
        LambdaQueryWrapper<DiyTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiyTemplate::getName, name)
            .eq(DiyTemplate::getIsDeleted, false)
            .ne(id != null, DiyTemplate::getId, id);
        if (baseMapper.selectCount(lqw) > 0) {
            throw new ServiceException("装修模板名称已存在：" + name);
        }
    }

    /**
     * 填充新增装修模板审计字段。
     */
    private void fillCreateInfo(DiyTemplate entity) {
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        entity.setIsDeleted(false);
    }

    /**
     * 填充修改装修模板审计字段。
     */
    private void fillUpdateInfo(DiyTemplate entity) {
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
    }

}
