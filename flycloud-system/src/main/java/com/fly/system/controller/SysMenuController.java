package com.fly.system.controller;

import com.fly.common.domain.entity.LoginUser;
import com.fly.common.enums.SysTypeEnum;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.api.domain.vo.SysMenuVo;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.system.service.ISysMenuService;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 菜单控制器
 *
 * @author fly
 * @date 2024-08-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    private final ISysMenuService iSysMenuService;


    /**
     * 查询菜单列表
     */
    @GetMapping("/getList")
    public R<List<SysMenuVo>> getList(SysMenuBo bo) {

        bo.setType(SysTypeEnum.fly_platform.getCode());
        return R.ok(iSysMenuService.queryList(bo));
    }

    /**
     * 查询菜单列表 - 树型
     */
    @PreAuthorize("@pms.hasPermission('sys.menu.list')")
    @GetMapping("/getTreeList")
    public R<List<SysMenuTreeVo>> getTreeList(SysMenuBo bo) {

        bo.setType(SysTypeEnum.fly_platform.getCode());
        return R.ok(iSysMenuService.getTreeList(bo));
    }

    /**
     * 新增/修改菜单
     */
    @PreAuthorize("@pms.hasPermission('sys.menu.saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@Validated @RequestBody SysMenuBo bo, HttpServletRequest request) {

        LoginUser loginUser = SecurityUtils.getCurUser(request);
        return R.ok(iSysMenuService.saveOrUpdate(bo));
    }



    /**
     * 查询菜单列表 - 分页
     */
    @PreAuthorize("@pms.hasPermission('sys.menu.list')")
    @GetMapping("/list")
    public R<PageVo<SysMenuVo>> list(SysMenuBo bo, PageBo page) {
        return R.ok(iSysMenuService.queryPageList(bo, page));
    }


    /**
     * 导出菜单列表
     */
    @Log(title = "菜单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMenuBo bo, HttpServletResponse response) {
        List<SysMenuVo> list = iSysMenuService.queryList(bo);
        ExcelUtil.exportExcel(list, "菜单", SysMenuVo.class, response);
    }


    /**
     * 获取菜单详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<SysMenuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysMenuService.queryById(id));
    }


    /**
     * 新增菜单
     */
    @Log(title = "菜单", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysMenuBo bo) {
        return R.ok(iSysMenuService.insertByBo(bo));
    }


    /**
     * 修改菜单
     */
    @Log(title = "菜单", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysMenuBo bo) {
        return R.ok(iSysMenuService.updateByBo(bo));
    }


    /**
     * 删除菜单
     *
     * @param ids 主键串
     */
    @PreAuthorize("@pms.hasPermission('sys.menu.delete')")
    @Log(title = "菜单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysMenuService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
