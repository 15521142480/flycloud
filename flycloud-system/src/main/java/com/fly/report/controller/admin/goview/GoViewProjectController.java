package com.fly.report.controller.admin.goview;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.report.service.IGoViewProjectService;
import com.fly.system.api.report.domain.bo.GoViewProjectBo;
import com.fly.system.api.report.domain.vo.GoViewProjectRespVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - GoView 项目控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping({"/report/go-view/project", "/admin/report/go-view/project"})
public class GoViewProjectController {

    private final IGoViewProjectService goViewProjectService;

    @PostMapping("/create")
    @PreAuthorize("@pms.hasAnyPermission({'report:goview:createOrUpdate', 'report:manage'})")
    public R<Long> createProject(@Valid @RequestBody GoViewProjectBo bo) {
        return R.ok(goViewProjectService.createProject(bo));
    }

    @PutMapping("/update")
    @PreAuthorize("@pms.hasAnyPermission({'report:goview:createOrUpdate', 'report:manage'})")
    public R<Boolean> updateProject(@Valid @RequestBody GoViewProjectBo bo) {
        goViewProjectService.updateProject(bo);
        return R.result(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasAnyPermission({'report:goview:delete', 'report:manage'})")
    public R<Boolean> deleteProject(@RequestParam("id") Long id) {
        goViewProjectService.deleteProject(id);
        return R.result(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@pms.hasAnyPermission({'report:goview:list', 'report:manage'})")
    public R<GoViewProjectRespVo> getProject(@RequestParam("id") Long id) {
        return R.ok(goViewProjectService.queryById(id));
    }

    @GetMapping("/my-page")
    @PreAuthorize("@pms.hasAnyPermission({'report:goview:list', 'report:manage'})")
    public R<PageVo<GoViewProjectRespVo>> getMyProjectPage(PageBo pageBo) {
        return R.ok(goViewProjectService.getMyProjectPage(pageBo, UserUtils.getCurUserId()));
    }

}
