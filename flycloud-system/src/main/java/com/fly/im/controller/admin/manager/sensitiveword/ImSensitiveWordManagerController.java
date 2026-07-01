package com.fly.im.controller.admin.manager.sensitiveword;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.im.framework.util.NumberUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordPageReqVo;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordRespVo;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordSaveReqVo;
import com.fly.im.dal.dataobject.sensitiveword.ImSensitiveWordDO;
import com.fly.im.service.sensitiveword.ImSensitiveWordService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - IM 敏感词")
@RestController
@RequestMapping({"/im/manager/sensitive-word", "/admin/im/manager/sensitive-word"})
@Validated
public class ImSensitiveWordManagerController {

    @Resource
    private ImSensitiveWordService sensitiveWordService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "新增敏感词")
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:create')")
    public R<Long> createSensitiveWord(@Valid @RequestBody ImSensitiveWordSaveReqVo reqVo) {
        return ok(sensitiveWordService.createSensitiveWord(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改敏感词")
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:update')")
    public R<Boolean> updateSensitiveWord(@Valid @RequestBody ImSensitiveWordSaveReqVo reqVo) {
        sensitiveWordService.updateSensitiveWord(reqVo);
        return ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除敏感词")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:delete')")
    public R<Boolean> deleteSensitiveWord(@RequestParam("id") Long id) {
        sensitiveWordService.deleteSensitiveWord(id);
        return ok(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除敏感词")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:delete')")
    public R<Boolean> deleteSensitiveWordList(
            @RequestParam("ids")
            @Size(max = 100, message = "批量删除最多 100 条") List<Long> ids) {
        sensitiveWordService.deleteSensitiveWordList(ids);
        return ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得敏感词分页")
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:query')")
    public R<PageResult<ImSensitiveWordRespVo>> getSensitiveWordPage(
            @Valid ImSensitiveWordPageReqVo pageReqVo) {
        // 1. 分页查询
        PageResult<ImSensitiveWordDO> pageResult = sensitiveWordService.getSensitiveWordPage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 2.1 批量查询创建人昵称
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(convertSet(pageResult.getList(),
                word -> NumberUtils.parseLong(word.getCreator())));
        // 2.2 转换为 VO，填充创建人昵称
        return ok(PageResult.convert(pageResult, ImSensitiveWordRespVo.class, vo ->
                MapUtils.findAndThen(userMap, NumberUtils.parseLong(vo.getCreator()),
                        user -> vo.setCreatorName(user.getName()))));
    }

    @GetMapping("/get")
    @Operation(summary = "获得敏感词详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:sensitive-word:query')")
    public R<ImSensitiveWordRespVo> getSensitiveWord(@RequestParam("id") Long id) {
        ImSensitiveWordDO word = sensitiveWordService.getSensitiveWord(id);
        return ok(BeanUtils.toBean(word, ImSensitiveWordRespVo.class));
    }

}
