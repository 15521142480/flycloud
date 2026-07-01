package com.fly.report.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.report.domain.GoViewProject;
import com.fly.system.api.report.domain.bo.GoViewProjectBo;
import com.fly.system.api.report.domain.vo.GoViewProjectRespVo;

/**
 * GoView 项目 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IGoViewProjectService {

    Long createProject(GoViewProjectBo bo);

    void updateProject(GoViewProjectBo bo);

    void deleteProject(Long id);

    GoViewProject getProject(Long id);

    GoViewProjectRespVo queryById(Long id);

    PageVo<GoViewProjectRespVo> getMyProjectPage(PageBo pageBo, Long userId);

}
