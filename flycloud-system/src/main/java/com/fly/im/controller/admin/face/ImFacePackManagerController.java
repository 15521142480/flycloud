package com.fly.im.controller.admin.face;

import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.face.pack.ImFacePackPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.face.pack.ImFacePackRespVo;
import com.fly.system.api.im.domain.vo.admin.manager.face.pack.ImFacePackSaveReqVo;
import com.fly.system.api.im.domain.face.ImFacePack;
import com.fly.im.service.face.ImFacePackService;
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

@Tag(name = "管理后台 - IM 表情包")
@RestController
@RequestMapping({"/im/manager/face-pack", "/admin/im/manager/face-pack"})
@Validated
public class ImFacePackManagerController {

    @Resource
    private ImFacePackService facePackService;

    @PostMapping("/create")
    @Operation(summary = "新增表情包")
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:saveOrUpdate')")
    public R<Long> createFacePack(@Valid @RequestBody ImFacePackSaveReqVo reqVo) {
        return ok(facePackService.createFacePack(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改表情包")
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:saveOrUpdate')")
    public R<Boolean> updateFacePack(@Valid @RequestBody ImFacePackSaveReqVo reqVo) {
        facePackService.updateFacePack(reqVo);
        return R.result(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除表情包")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:delete')")
    public R<Boolean> deleteFacePack(@RequestParam("id") Long id) {
        facePackService.deleteFacePack(id);
        return R.result(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除表情包")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:delete')")
    public R<Boolean> deleteFacePackList(@RequestParam("ids")
                                                    @Size(max = 100, message = "批量删除最多 100 条") List<Long> ids) {
        facePackService.deleteFacePackList(ids);
        return R.result(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得表情包分页")
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:list')")
    public R<PageResult<ImFacePackRespVo>> getFacePackPage(@Valid ImFacePackPageReqVo pageReqVo) {
        PageResult<ImFacePack> pageResult = facePackService.getFacePackPage(pageReqVo);
        return ok(PageResult.convert(pageResult, ImFacePackRespVo.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得表情包详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:face-pack:sys:list')")
    public R<ImFacePackRespVo> getFacePack(@RequestParam("id") Long id) {
        ImFacePack pack = facePackService.getFacePack(id);
        return ok(BeanUtils.toBean(pack, ImFacePackRespVo.class));
    }

}
