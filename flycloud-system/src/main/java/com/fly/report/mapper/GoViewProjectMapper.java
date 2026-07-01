package com.fly.report.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.report.domain.GoViewProject;
import com.fly.system.api.report.domain.vo.GoViewProjectVo;

/**
 * GoView 项目 Mapper。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface GoViewProjectMapper extends BaseMapperPlus<GoViewProjectMapper, GoViewProject, GoViewProjectVo> {

    /**
     * 分页查询我的 GoView 项目。
     */
    default Page<GoViewProjectVo> selectMyPage(PageBo pageBo, Long userId) {
        LambdaQueryWrapper<GoViewProject> lqw = new LambdaQueryWrapper<GoViewProject>()
                .eq(GoViewProject::getIsDeleted, false)
                .eq(userId != null, GoViewProject::getCreateBy, String.valueOf(userId))
                .orderByDesc(GoViewProject::getId);
        return selectVoPage(pageBo.build(), lqw);
    }

    /**
     * 分页查询我的 GoView 项目实体，用于组装自定义响应对象。
     */
    default Page<GoViewProject> selectMyEntityPage(PageBo pageBo, Long userId) {
        LambdaQueryWrapper<GoViewProject> lqw = new LambdaQueryWrapper<GoViewProject>()
                .eq(GoViewProject::getIsDeleted, false)
                .eq(userId != null, GoViewProject::getCreateBy, String.valueOf(userId))
                .orderByDesc(GoViewProject::getId);
        return selectPage(pageBo.build(), lqw);
    }

}
