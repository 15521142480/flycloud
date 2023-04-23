package com.fly.system.controller;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.model.R;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.system.domain.bo.SysMenuBo;
import com.fly.system.domain.vo.SysMenuVo;
import com.fly.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单权限控制器
 *
 * @author fly
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    private final ISysMenuService iSysMenuService;


    /**
     * 查询菜单权限列表
     */
    @GetMapping("/list")
    public R<PageVo<SysMenuVo>> list(SysMenuBo bo, PageBo page) {
        return R.ok(iSysMenuService.queryPageList(bo, page));
    }


    /**
     * 导出菜单权限列表
     */
    @Log(title = "菜单权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMenuBo bo, HttpServletResponse response) {
        List<SysMenuVo> list = iSysMenuService.queryList(bo);
        ExcelUtil.exportExcel(list, "菜单权限", SysMenuVo.class, response);
    }


    /**
     * 获取菜单权限详细信息
     *
     * @param menuId 主键
     */
    @GetMapping("/{menuId}")
    public R<SysMenuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long menuId) {
        return R.ok(iSysMenuService.queryById(menuId));
    }


    /**
     * 新增菜单权限
     */
    @Log(title = "菜单权限", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysMenuBo bo) {
        return R.ok(iSysMenuService.insertByBo(bo));
    }


    /**
     * 修改菜单权限
     */
    @Log(title = "菜单权限", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysMenuBo bo) {
        return R.ok(iSysMenuService.updateByBo(bo));
    }


    /**
     * 删除菜单权限
     *
     * @param menuIds 主键串
     */
    @Log(title = "菜单权限", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] menuIds) {
        return R.ok(iSysMenuService.deleteWithValidByIds(Arrays.asList(menuIds), true));
    }
}
