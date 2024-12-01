package com.fly.bpm.common.controller;

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
import com.fly.bpm.api.domain.vo.BpmProcessDefinitionInfoVo;
import com.fly.bpm.api.domain.bo.BpmProcessDefinitionInfoBo;
import com.fly.bpm.common.service.IBpmProcessDefinitionInfoService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 流程定义的信息控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/processDefinitionInfo")
public class BpmProcessDefinitionInfoController extends BaseController {

    private final IBpmProcessDefinitionInfoService iBpmProcessDefinitionInfoService;


    /**
     * 查询BPM 流程定义的信息列表
     */
    @GetMapping("/list")
    public R<PageVo<BpmProcessDefinitionInfoVo>> list(BpmProcessDefinitionInfoBo bo, PageBo page) {
        return R.ok(iBpmProcessDefinitionInfoService.queryPageList(bo, page));
    }


    /**
     * 导出BPM 流程定义的信息列表
     */
    @Log(title = "BPM 流程定义的信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BpmProcessDefinitionInfoBo bo, HttpServletResponse response) {
        List<BpmProcessDefinitionInfoVo> list = iBpmProcessDefinitionInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "BPM 流程定义的信息", BpmProcessDefinitionInfoVo.class, response);
    }


    /**
     * 获取BPM 流程定义的信息详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<BpmProcessDefinitionInfoVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmProcessDefinitionInfoService.queryById(id));
    }


    /**
     * 新增BPM 流程定义的信息
     */
    @Log(title = "BPM 流程定义的信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmProcessDefinitionInfoBo bo) {
        return R.ok(iBpmProcessDefinitionInfoService.insertByBo(bo));
    }


    /**
     * 修改BPM 流程定义的信息
     */
    @Log(title = "BPM 流程定义的信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmProcessDefinitionInfoBo bo) {
        return R.ok(iBpmProcessDefinitionInfoService.updateByBo(bo));
    }


    /**
     * 删除BPM 流程定义的信息
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 流程定义的信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmProcessDefinitionInfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
