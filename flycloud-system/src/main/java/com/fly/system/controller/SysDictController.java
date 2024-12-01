package com.fly.system.controller;

import com.fly.common.enums.StatusEnum;
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
import com.fly.system.api.domain.vo.SysDictVo;
import com.fly.system.api.domain.bo.SysDictBo;
import com.fly.system.service.ISysDictService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 字典数据控制器
 *
 * @author fly
 * @date 2024-11-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict")
public class SysDictController extends BaseController {

    private final ISysDictService iSysDictService;


    /**
     * 查询字典数据列表-分页
     */
    @GetMapping("/list")
    public R<PageVo<SysDictVo>> list(SysDictBo bo, PageBo page) {
        return R.ok(iSysDictService.queryPageList(bo, page));
    }


    /**
     * 查询字典数据列表
     */
    @GetMapping("/getList")
    public R<List<SysDictVo>> getList(SysDictBo bo) {

        bo.setStatus(StatusEnum.ENABLE.getStatus());
        bo.setIsDeleted(false);
        return R.ok(iSysDictService.queryList(bo));
    }


    /**
     * 导出字典数据列表
     */
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDictBo bo, HttpServletResponse response) {
        List<SysDictVo> list = iSysDictService.queryList(bo);
        ExcelUtil.exportExcel(list, "字典数据", SysDictVo.class, response);
    }


    /**
     * 获取字典数据详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<SysDictVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysDictService.queryById(id));
    }


    /**
     * 新增字典数据
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysDictBo bo) {
        return R.ok(iSysDictService.insertByBo(bo));
    }


    /**
     * 修改字典数据
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysDictBo bo) {
        return R.ok(iSysDictService.updateByBo(bo));
    }


    /**
     * 删除字典数据
     *
     * @param ids 主键串
     */
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysDictService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
