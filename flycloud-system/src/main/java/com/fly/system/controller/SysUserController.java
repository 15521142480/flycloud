package com.fly.system.controller;

import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.UserDetailInfoVo;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 查询用户列表
     */
    @PreAuthorize("@pms.hasPermission('sys:user:list')")
    @GetMapping("/list")
    public R<PageVo<SysUserVo>> list(SysUserBo bo, PageBo page) {
        return R.ok(iSysUserService.queryPageList(bo, page));
    }


    /**
     * 查询所有用户精简版
     */
    @GetMapping("/allListSimple")
    public R<List<SysUserVo>> list(SysUserBo bo) {
        return R.ok(iSysUserService.queryAllListSimple(bo));
    }


    /**
     * 获取用户详情信息
     */
    @GetMapping("/getUserInfo")
    public R<UserDetailInfoVo> getDetailInfo() {

        FlyUser user = UserUtils.getCurUser();
        return R.ok(iSysUserService.getDetailInfo(user.getId()));
    }


    /**
     * 获取用户详情信息
     */
    @GetMapping("/getDetailInfo/{id}")
    public R<UserDetailInfoVo> getDetailInfo(@NotNull(message = "id不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.getDetailInfo(id));
    }


    /**
     * 获取用户详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysUserVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.queryById(id));
    }


    /**
     * 用户注册
     */
//    @PreAuthorize("@pms.hasPermission('sys.user.register')")
//    @PostMapping("/register")
//    public R<Void> register(@RequestBody SysUserBo bo) {
//        return R.ok(iSysUserService.register(bo));
//    }


    /**
     * 用户新增/修改
     */
    @PreAuthorize("@pms.hasPermission('sys:user:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody SysUserBo bo) {
        return R.ok(iSysUserService.saveOrUpdate(bo));
    }


    /**
     * 禁用启用
     */
    @PreAuthorize("@pms.hasPermission('sys:user:enable')")
    @PostMapping("/enable")
    public R<Void> enable(@RequestParam() Long id, @RequestParam() int status) {

        return R.ok(iSysUserService.updateById(new SysUser().setId(id).setStatus(status)));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@pms.hasPermission('sys:user:restartPassword')")
    @PostMapping("/resetPassword/{id}")
    public R<Void> resetPassword(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysUserService.resetPassword(id));
    }

    /**
     * 自定义重置密码
     */
    @PreAuthorize("@pms.hasPermission('sys:user:restartPassword')")
    @PostMapping("/customResetPassword/{id}/{password}")
    public R<Void> customResetPassword(
            @NotNull(message = "主键不能为空") @PathVariable Long id
            , @NotNull(message = "密码不能为空") @PathVariable String password
    ) {
        return R.ok(iSysUserService.customResetPassword(id, password));
    }


    /**
     * 删除用户
     *
     * @param ids 主键串
     */
    @PreAuthorize("@pms.hasPermission('sys:user:delete')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysUserService.deleteWithValidByIds(Arrays.asList(ids), true));
    }



//    /**
//     * 新增用户
//     */
//    @Log(title = "用户", businessType = BusinessType.INSERT)
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserBo bo) {
//        return R.ok(iSysUserService.insertByBo(bo));
//    }
//
//
//    /**
//     * 修改用户
//     */
//    @Log(title = "用户", businessType = BusinessType.UPDATE)
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysUserBo bo) {
//        return R.ok(iSysUserService.updateByBo(bo));
//    }


    /**
     * 导出用户列表
     */
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PreAuthorize("pms.hasPermission('sys:user:download')")
    @PostMapping("/export")
    public void export(SysUserBo bo, HttpServletResponse response) {
        List<SysUserVo> list = iSysUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", SysUserVo.class, response);
    }



}
