package com.fly.system.controller;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.api.domain.vo.SysDictTypeVo;
import com.fly.system.api.domain.bo.SysDictTypeBo;
import com.fly.system.service.ISysDictTypeService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 字典类型控制器
 *
 * @author fly
 * @date 2024-12-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dictType")
public class SysDictTypeController extends BaseController {

    private final ISysDictTypeService iSysDictTypeService;


    /**
     * 查询字典类型分页列表
     */
    @GetMapping("/page")
    public R<PageVo<SysDictTypeVo>> page(SysDictTypeBo bo, PageBo page) {
        return R.ok(iSysDictTypeService.queryPageList(bo, page));
    }

    /**
     * 获取字典类型详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysDictTypeVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysDictTypeService.queryById(id));
    }


    /**
     * 新增字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysDictTypeBo bo) {
        return R.ok(iSysDictTypeService.insertByBo(bo));
    }


    /**
     * 修改字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysDictTypeBo bo) {
        return R.ok(iSysDictTypeService.updateByBo(bo));
    }


    /**
     * 删除字典类型
     *
     * @param ids 主键串
     */
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysDictTypeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出字典类型列表
     */
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDictTypeBo bo, HttpServletResponse response) {
        List<SysDictTypeVo> list = iSysDictTypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "字典类型", SysDictTypeVo.class, response);
    }

}
