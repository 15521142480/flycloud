package com.fly.test.controller;

import com.fly.common.database.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 *
 * @author fly
 * @date 2023-04-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userTest")
public class SysUserTestController extends BaseController {

//    private final ISysUserTestService iSysUserService;
//
//
//    /**
//     * 查询用户信息列表
//     */
//    @GetMapping("/list")
//    public R<PageVo<SysUserTestVo>> list(SysUserTestBo bo, PageBo page) {
//        return R.ok(iSysUserService.queryPageList(bo, page));
//    }
//
//
//    /**
//     * 导出用户信息列表
//     */
//    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(SysUserTestBo bo, HttpServletResponse response) {
//        List<SysUserTestVo> list = iSysUserService.queryList(bo);
//        ExcelUtil.exportExcel(list, "用户信息", SysUserTestVo.class, response);
//    }
//
//
//    /**
//     * 获取用户信息详细信息
//     *
//     * @param userId 主键
//     */
//    @GetMapping("/{userId}")
//    public R<SysUserTestVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long userId) {
//        return R.ok(iSysUserService.queryById(userId));
//    }
//
//
//    /**
//     * 新增用户信息
//     */
//    @Log(title = "用户信息", businessType = BusinessType.INSERT)
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserTestBo bo) {
//        return R.ok(iSysUserService.insertByBo(bo));
//    }
//
//
//    /**
//     * 修改用户信息
//     */
//    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysUserTestBo bo) {
//        return R.ok(iSysUserService.updateByBo(bo));
//    }
//
//
//    /**
//     * 删除用户信息
//     *
//     * @param userIds 主键串
//     */
//    @Log(title = "用户信息", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{userIds}")
//    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] userIds) {
//        return R.ok(iSysUserService.deleteWithValidByIds(Arrays.asList(userIds), true));
//    }
}
