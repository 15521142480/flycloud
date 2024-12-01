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
import com.fly.bpm.api.domain.vo.BpmUserGroupVo;
import com.fly.bpm.api.domain.bo.BpmUserGroupBo;
import com.fly.bpm.common.service.IBpmUserGroupService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 用户组控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userGroup")
public class BpmUserGroupController extends BaseController {

    private final IBpmUserGroupService iBpmUserGroupService;


    /**
     * 查询BPM 用户组列表
     */
    @GetMapping("/list")
    public R<PageVo<BpmUserGroupVo>> list(BpmUserGroupBo bo, PageBo page) {
        return R.ok(iBpmUserGroupService.queryPageList(bo, page));
    }


    /**
     * 导出BPM 用户组列表
     */
    @Log(title = "BPM 用户组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BpmUserGroupBo bo, HttpServletResponse response) {
        List<BpmUserGroupVo> list = iBpmUserGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "BPM 用户组", BpmUserGroupVo.class, response);
    }


    /**
     * 获取BPM 用户组详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<BpmUserGroupVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmUserGroupService.queryById(id));
    }


    /**
     * 新增BPM 用户组
     */
    @Log(title = "BPM 用户组", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmUserGroupBo bo) {
        return R.ok(iBpmUserGroupService.insertByBo(bo));
    }


    /**
     * 修改BPM 用户组
     */
    @Log(title = "BPM 用户组", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmUserGroupBo bo) {
        return R.ok(iBpmUserGroupService.updateByBo(bo));
    }


    /**
     * 删除BPM 用户组
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 用户组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmUserGroupService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
