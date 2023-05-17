package com.fly.user.controller;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.model.R;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.user.domain.vo.SysUserVo;
import com.fly.user.domain.bo.SysUserBo;
import com.fly.user.service.ISysUserService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息控制器
 *
 * @author fly
 * @date 2023-04-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final ISysUserService iSysUserService;


    /**
     * 查询用户信息列表
     */
    @GetMapping("/list")
    public R<PageVo<SysUserVo>> list(SysUserBo bo, PageBo page) {
        return R.ok(iSysUserService.queryPageList(bo, page));
    }


    /**
     * 导出用户信息列表
     */
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysUserBo bo, HttpServletResponse response) {
        List<SysUserVo> list = iSysUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户信息", SysUserVo.class, response);
    }


    /**
     * 获取用户信息详细信息
     *
     * @param userId 主键
     */
    @GetMapping("/{userId}")
    public R<SysUserVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long userId) {
        return R.ok(iSysUserService.queryById(userId));
    }


    /**
     * 新增用户信息
     */
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.insertByBo(bo));
    }


    /**
     * 修改用户信息
     */
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.updateByBo(bo));
    }


    /**
     * 删除用户信息
     *
     * @param userIds 主键串
     */
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] userIds) {
        return R.ok(iSysUserService.deleteWithValidByIds(Arrays.asList(userIds), true));
    }
}
