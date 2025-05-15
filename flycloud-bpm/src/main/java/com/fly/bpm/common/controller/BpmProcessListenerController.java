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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.api.domain.vo.BpmProcessListenerVo;
import com.fly.bpm.api.domain.bo.BpmProcessListenerBo;
import com.fly.bpm.common.service.IBpmProcessListenerService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 流程监听器控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/processListener")
public class BpmProcessListenerController extends BaseController {

    private final IBpmProcessListenerService iBpmProcessListenerService;


    /**
     * 查询BPM 流程监听器列表
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('bpm:manage:listener:list')")
    public R<PageVo<BpmProcessListenerVo>> list(BpmProcessListenerBo bo, PageBo page) {
        return R.ok(iBpmProcessListenerService.queryPageList(bo, page));
    }


    /**
     * 获取BPM 流程监听器详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<BpmProcessListenerVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmProcessListenerService.queryById(id));
    }


    /**
     * 新增BPM 流程监听器
     */
    @Log(title = "BPM 流程监听器", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @PreAuthorize("@pms.hasPermission('bpm:manage:listener:saveOrUpdate')")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmProcessListenerBo bo) {
        return R.ok(iBpmProcessListenerService.insertByBo(bo));
    }


    /**
     * 修改BPM 流程监听器
     */
    @Log(title = "BPM 流程监听器", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @PreAuthorize("@pms.hasPermission('bpm:manage:listener:saveOrUpdate')")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmProcessListenerBo bo) {
        return R.ok(iBpmProcessListenerService.updateByBo(bo));
    }


    /**
     * 删除BPM 流程监听器
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 流程监听器", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("@pms.hasPermission('bpm:manage:listener:delete')")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmProcessListenerService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
