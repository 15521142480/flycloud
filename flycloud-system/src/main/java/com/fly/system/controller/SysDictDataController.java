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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.api.domain.vo.SysDictDataVo;
import com.fly.system.api.domain.bo.SysDictDataBo;
import com.fly.system.service.ISysDictDataService;

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
@RequestMapping("/dictData")
public class SysDictDataController extends BaseController {

    private final ISysDictDataService iSysDictService;


    /**
     * 查询字典数据列表-分页
     */
    @GetMapping("/list")
    public R<PageVo<SysDictDataVo>> list(SysDictDataBo bo, PageBo page) {
        return R.ok(iSysDictService.queryPageList(bo, page));
    }


    /**
     * 查询字典数据列表
     */
    @GetMapping("/getList")
    public R<List<SysDictDataVo>> getList(SysDictDataBo bo) {

        bo.setStatus(StatusEnum.ENABLE.getStatus());
        bo.setIsDeleted(false);
        return R.ok(iSysDictService.queryList(bo));
    }


    /**
     * 导出字典数据列表
     */
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDictDataBo bo, HttpServletResponse response) {
        List<SysDictDataVo> list = iSysDictService.queryList(bo);
        ExcelUtil.exportExcel(list, "字典数据", SysDictDataVo.class, response);
    }


    /**
     * 获取字典数据详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysDictDataVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysDictService.queryById(id));
    }


    /**
     * 新增/修改字典数据
     */
    @Log(title = "字典数据")
    @PreAuthorize("@pms.hasPermission('sys:dict:optionDictData')") // todo 二级页面：操作字典数据， 后续可修改
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@Validated @RequestBody SysDictDataBo bo) {
        return R.ok(iSysDictService.saveOrUpdate(bo));
    }



    /**
     * 删除字典数据
     *
     * @param ids 主键串
     */
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysDictService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
