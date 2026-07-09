package com.fly.im.controller.client.face;

import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.ImFaceUserItemVo;
import com.fly.system.api.im.domain.bo.ImFaceUserItemBo;
import com.fly.system.api.im.domain.ImFaceUserItem;
import com.fly.im.service.face.ImFaceUserItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "管理后台 - IM 个人表情")
@RestController
@RequestMapping({"/im/face-user-item", "/admin/im/face-user-item"})
@Validated
public class ImFaceUserItemController {

    @Resource
    private ImFaceUserItemService faceUserItemService;


    @GetMapping("/list")
    @Operation(summary = "获得我的个人表情列表")
    public R<List<ImFaceUserItemVo>> getFaceUserItemList() {
        List<ImFaceUserItem> items = faceUserItemService.getFaceUserItemList(getCurUserId());
        List<ImFaceUserItemVo> result = BeanUtils.toBean(items, ImFaceUserItemVo.class);
        result.forEach(vo -> vo.setUrl(vo.getUrl()));
        return ok(result);
    }

    @PostMapping("/create")
    @Operation(summary = "添加个人表情")
    public R<Long> createFaceUserItem(@Valid @RequestBody ImFaceUserItemBo reqVo) {
        return ok(faceUserItemService.createFaceUserItem(getCurUserId(), reqVo));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除个人表情")
    @Parameter(name = "id", description = "编号", required = true, example = "4096")
    public R<Boolean> deleteFaceUserItem(@RequestParam("id") Long id) {
        faceUserItemService.deleteFaceUserItem(getCurUserId(), id);
        return R.result(true);
    }

}
