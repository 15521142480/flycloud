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
import com.fly.system.domain.bo.SysConfigBo;
import com.fly.system.domain.vo.SysConfigVo;
import com.fly.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 参数配置控制器
 *
 * @author fly
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController {

    private final ISysConfigService iSysConfigService;


    /**
     * 查询参数配置列表
     */
    @GetMapping("/list")
    public R<PageVo<SysConfigVo>> list(SysConfigBo bo, PageBo page) {
        return R.ok(iSysConfigService.queryPageList(bo, page));
    }


    /**
     * 导出参数配置列表
     */
    @Log(title = "参数配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysConfigBo bo, HttpServletResponse response) {
        List<SysConfigVo> list = iSysConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "参数配置", SysConfigVo.class, response);
    }


    /**
     * 获取参数配置详细信息
     *
     * @param configId 主键
     */
    @GetMapping("/{configId}")
    public R<SysConfigVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long configId) {
        return R.ok(iSysConfigService.queryById(configId));
    }


    /**
     * 新增参数配置
     */
    @Log(title = "参数配置", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysConfigBo bo) {
        return R.ok(iSysConfigService.insertByBo(bo));
    }


    /**
     * 修改参数配置
     */
    @Log(title = "参数配置", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysConfigBo bo) {
        return R.ok(iSysConfigService.updateByBo(bo));
    }


    /**
     * 删除参数配置
     *
     * @param configIds 主键串
     */
    @Log(title = "参数配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] configIds) {
        return R.ok(iSysConfigService.deleteWithValidByIds(Arrays.asList(configIds), true));
    }
}
