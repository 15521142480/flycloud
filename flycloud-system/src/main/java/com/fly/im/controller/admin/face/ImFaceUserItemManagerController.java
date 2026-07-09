package com.fly.im.controller.admin.face;

import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.bo.ImFaceUserItemManagerPageBo;
import com.fly.system.api.im.domain.vo.ImFaceUserItemManagerVo;
import com.fly.system.api.im.domain.ImFaceUserItem;
import com.fly.im.service.face.ImFaceUserItemService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;

@Tag(name = "管理后台 - IM 用户表情")
@RestController
@RequestMapping({"/im/manager/face-user-item", "/admin/im/manager/face-user-item"})
@Validated
public class ImFaceUserItemManagerController {

    @Resource
    private ImFaceUserItemService faceUserItemService;
    @Resource
    private AdminUserApi adminUserApi;



    @GetMapping("/page")
    @Operation(summary = "获得用户表情分页")
    @PreAuthorize("@pms.hasPermission('im:face-pack:user:list')")
    public R<PageResult<ImFaceUserItemManagerVo>> getFaceUserItemPage(
            @Valid ImFaceUserItemManagerPageBo pageReqVo) {
        PageResult<ImFaceUserItem> pageResult = faceUserItemService.getFaceUserItemPage(pageReqVo);
        // 关联回填用户昵称
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                CollectionUtils.convertSet(pageResult.getList(), ImFaceUserItem::getUserId));
        List<ImFaceUserItemManagerVo> voList = CollectionUtils.convertList(pageResult.getList(), item -> {
            ImFaceUserItemManagerVo vo = BeanUtils.toBean(item, ImFaceUserItemManagerVo.class);
            SysUserVo user = userMap.get(item.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getName());
            }
            vo.setUrl(vo.getUrl());
            return vo;
        });
        return ok(new PageResult<>(voList, pageResult.getTotal()));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户表情")
    @Parameter(name = "id", description = "编号", required = true, example = "4096")
    @PreAuthorize("@pms.hasPermission('im:face-pack:user:delete')")
    public R<Boolean> deleteFaceUserItem(@RequestParam("id") Long id) {
        faceUserItemService.deleteFaceUserItem(id);
        return R.result(true);
    }

}
