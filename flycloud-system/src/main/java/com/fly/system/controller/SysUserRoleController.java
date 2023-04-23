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
import com.fly.system.domain.bo.SysUserRoleBo;
import com.fly.system.domain.vo.SysUserRoleVo;
import com.fly.system.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户和角色关联控制器
 *
 * @author fly
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userRole")
public class SysUserRoleController extends BaseController {

    private final ISysUserRoleService iSysUserRoleService;


    /**
     * 查询用户和角色关联列表
     */
    @GetMapping("/list")
    public R<PageVo<SysUserRoleVo>> list(SysUserRoleBo bo, PageBo page) {
        return R.ok(iSysUserRoleService.queryPageList(bo, page));
    }


    /**
     * 导出用户和角色关联列表
     */
    @Log(title = "用户和角色关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysUserRoleBo bo, HttpServletResponse response) {
        List<SysUserRoleVo> list = iSysUserRoleService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户和角色关联", SysUserRoleVo.class, response);
    }


    /**
     * 获取用户和角色关联详细信息
     *
     * @param userId 主键
     */
    @GetMapping("/{userId}")
    public R<SysUserRoleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long userId) {
        return R.ok(iSysUserRoleService.queryById(userId));
    }


    /**
     * 新增用户和角色关联
     */
    @Log(title = "用户和角色关联", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserRoleBo bo) {
        return R.ok(iSysUserRoleService.insertByBo(bo));
    }


    /**
     * 修改用户和角色关联
     */
    @Log(title = "用户和角色关联", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysUserRoleBo bo) {
        return R.ok(iSysUserRoleService.updateByBo(bo));
    }


    /**
     * 删除用户和角色关联
     *
     * @param userIds 主键串
     */
    @Log(title = "用户和角色关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] userIds) {
        return R.ok(iSysUserRoleService.deleteWithValidByIds(Arrays.asList(userIds), true));
    }
}
