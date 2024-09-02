package com.fly.system.controller;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.model.R;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.system.api.domain.vo.UserDetailInfoVo;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.domain.bo.SysUserBo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制器
 *
 * @author fly
 * @date 2024-08-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final ISysUserService iSysUserService;



    /**
     * 获取用户详情信息
     */
    @GetMapping("/getDetailInfo{id}")
    public R<UserDetailInfoVo> getDetailInfo(@NotNull(message = "id不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.getDetailInfo(id));
    }


    /**
     * 查询用户列表
     */
    @GetMapping("/list")
    public R<PageVo<SysUserVo>> list(SysUserBo bo, PageBo page) {
        return R.ok(iSysUserService.queryPageList(bo, page));
    }

    /**
     * 用户新增/修改
     */
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.saveOrUpdate(bo));
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword{id}")
    public R<Void> resetPassword(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.resetPassword(id));
    }


    /**
     * 获取用户详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<SysUserVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.queryById(id));
    }


    /**
     * 新增用户
     */
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.insertByBo(bo));
    }


    /**
     * 修改用户
     */
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.updateByBo(bo));
    }


    /**
     * 删除用户
     *
     * @param ids 主键串
     */
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysUserService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 导出用户列表
     */
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysUserBo bo, HttpServletResponse response) {
        List<SysUserVo> list = iSysUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", SysUserVo.class, response);
    }
}
