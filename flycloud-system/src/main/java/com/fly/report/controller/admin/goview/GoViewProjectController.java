package com.fly.report.controller.admin.goview;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.report.service.IGoViewProjectService;
import com.fly.system.api.report.domain.bo.GoViewProjectBo;
import com.fly.system.api.report.domain.vo.GoViewProjectVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - GoView 项目控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping({"/report/go-view/project", "/admin/report/go-view/project"})
public class GoViewProjectController {

    private final IGoViewProjectService goViewProjectService;

    @PostMapping("/create")
    @PreAuthorize("@pms.hasPermission('report:go-view-project:create')")
    public R<Long> createProject(@Valid @RequestBody GoViewProjectBo bo) {
        return R.ok(goViewProjectService.createProject(bo));
    }

    @PutMapping("/update")
    @PreAuthorize("@pms.hasPermission('report:go-view-project:update')")
    public R<Boolean> updateProject(@Valid @RequestBody GoViewProjectBo bo) {
        goViewProjectService.updateProject(bo);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('report:go-view-project:delete')")
    public R<Boolean> deleteProject(@RequestParam("id") Long id) {
        goViewProjectService.deleteProject(id);
        return R.ok(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@pms.hasPermission('report:go-view-project:query')")
    public R<GoViewProjectVo> getProject(@RequestParam("id") Long id) {
        return R.ok(goViewProjectService.queryById(id));
    }

    @GetMapping("/my-page")
    @PreAuthorize("@pms.hasPermission('report:go-view-project:query')")
    public R<PageVo<GoViewProjectVo>> getMyProjectPage(PageBo pageBo) {
        return R.ok(goViewProjectService.getMyProjectPage(pageBo, UserUtils.getCurUserId()));
    }

}
