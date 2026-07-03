package com.fly.im.controller.admin.manager.face;

import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemRespVo;
import com.fly.system.api.im.domain.vo.admin.manager.face.item.ImFacePackItemSaveReqVo;
import com.fly.system.api.im.domain.face.ImFacePackItem;
import com.fly.im.service.face.ImFacePackItemService;
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

import static com.fly.common.domain.model.R.ok;

@Tag(name = "管理后台 - IM 表情包项")
@RestController
@RequestMapping({"/im/manager/face-pack-item", "/admin/im/manager/face-pack-item"})
@Validated
public class ImFacePackItemManagerController {

    @Resource
    private ImFacePackItemService facePackItemService;




    @PostMapping("/create")
    @Operation(summary = "新增表情")
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:create')")
    public R<Long> createFacePackItem(@Valid @RequestBody ImFacePackItemSaveReqVo reqVo) {
        return ok(facePackItemService.createFacePackItem(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改表情")
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:update')")
    public R<Boolean> updateFacePackItem(@Valid @RequestBody ImFacePackItemSaveReqVo reqVo) {
        facePackItemService.updateFacePackItem(reqVo);
        return ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除表情")
    @Parameter(name = "id", description = "编号", required = true, example = "2048")
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:delete')")
    public R<Boolean> deleteFacePackItem(@RequestParam("id") Long id) {
        facePackItemService.deleteFacePackItem(id);
        return ok(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除表情")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:delete')")
    public R<Boolean> deleteFacePackItemList(
            @RequestParam("ids") @Size(max = 100, message = "批量删除最多 100 条") List<Long> ids) {
        facePackItemService.deleteFacePackItemList(ids);
        return ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得表情分页")
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:query')")
    public R<PageResult<ImFacePackItemRespVo>> getFacePackItemPage(@Valid ImFacePackItemPageReqVo pageReqVo) {
        PageResult<ImFacePackItem> pageResult = facePackItemService.getFacePackItemPage(pageReqVo);
        PageResult<ImFacePackItemRespVo> result = PageResult.convert(pageResult, ImFacePackItemRespVo.class);
        return ok(result);
    }

    @GetMapping("/get")
    @Operation(summary = "获得表情详情")
    @Parameter(name = "id", description = "编号", required = true, example = "2048")
    @PreAuthorize("@pms.hasPermission('im:manager:face-pack-item:query')")
    public R<ImFacePackItemRespVo> getFacePackItem(@RequestParam("id") Long id) {
        ImFacePackItem item = facePackItemService.getFacePackItem(id);
        return ok(BeanUtils.toBean(item, ImFacePackItemRespVo.class));
    }

}
