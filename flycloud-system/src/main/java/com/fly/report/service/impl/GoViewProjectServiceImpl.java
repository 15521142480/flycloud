package com.fly.report.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.report.mapper.GoViewProjectMapper;
import com.fly.report.service.IGoViewProjectService;
import com.fly.system.api.report.domain.GoViewProject;
import com.fly.system.api.report.domain.bo.GoViewProjectBo;
import com.fly.system.api.report.domain.vo.GoViewProjectRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * GoView 项目 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Service
@Validated
@RequiredArgsConstructor
public class GoViewProjectServiceImpl implements IGoViewProjectService {

    private final GoViewProjectMapper goViewProjectMapper;

    @Override
    public Long createProject(GoViewProjectBo bo) {
        GoViewProject project = BeanUtil.toBean(bo, GoViewProject.class);
        LocalDateTime now = LocalDateTime.now();
        project.setStatus(1);
        project.setIsDeleted(false);
        project.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        project.setCreateTime(now);
        project.setUpdateBy(project.getCreateBy());
        project.setUpdateTime(now);
        goViewProjectMapper.insert(project);
        return project.getId();
    }

    @Override
    public void updateProject(GoViewProjectBo bo) {
        validateProjectExists(bo.getId());
        GoViewProject project = BeanUtil.toBean(bo, GoViewProject.class);
        project.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        project.setUpdateTime(LocalDateTime.now());
        goViewProjectMapper.updateById(project);
    }

    @Override
    public void deleteProject(Long id) {
        validateProjectExists(id);
        goViewProjectMapper.deleteById(id);
    }

    @Override
    public GoViewProject getProject(Long id) {
        return goViewProjectMapper.selectById(id);
    }

    @Override
    public GoViewProjectRespVo queryById(Long id) {
        return buildRespVo(goViewProjectMapper.selectById(id));
    }

    @Override
    public PageVo<GoViewProjectRespVo> getMyProjectPage(PageBo pageBo, Long userId) {
        Page<GoViewProject> page = goViewProjectMapper.selectMyEntityPage(pageBo, userId);
        PageVo<GoViewProjectRespVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords().stream().map(this::buildRespVo).toList());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 校验 GoView 项目存在。
     */
    private void validateProjectExists(Long id) {
        if (goViewProjectMapper.selectById(id) == null) {
            throw new ServiceException("GoView 项目不存在");
        }
    }

    /**
     * 构建 GoView 项目响应对象。
     */
    private GoViewProjectRespVo buildRespVo(GoViewProject project) {
        if (project == null) {
            return null;
        }
        GoViewProjectRespVo respVo = BeanUtil.toBean(project, GoViewProjectRespVo.class);
        respVo.setCreator(project.getCreateBy());
        return respVo;
    }

}
