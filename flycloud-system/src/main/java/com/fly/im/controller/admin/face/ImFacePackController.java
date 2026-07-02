package com.fly.im.controller.admin.face;

import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.file.service.FileUrlService;
import com.fly.system.api.im.domain.vo.admin.face.pack.ImFacePackUserRespVo;
import com.fly.system.api.im.domain.face.ImFacePack;
import com.fly.system.api.im.domain.face.ImFacePackItem;
import com.fly.im.service.face.ImFacePackItemService;
import com.fly.im.service.face.ImFacePackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.convertMultiMap;

@Tag(name = "管理后台 - IM 表情包")
@RestController
@RequestMapping({"/im/face-pack", "/admin/im/face-pack"})
@Validated
public class ImFacePackController {

    @Resource
    private ImFacePackService facePackService;
    @Resource
    private ImFacePackItemService facePackItemService;
    @Resource
    private FileUrlService fileUrlService;

    @GetMapping("/list")
    @Operation(summary = "获得启用的表情包列表（含表情）")
    public R<List<ImFacePackUserRespVo>> getFacePackList() {
        // 1.1 拉所有启用表情包
        List<ImFacePack> packs = facePackService.getEnabledFacePackList();
        if (packs.isEmpty()) {
            return ok(List.of());
        }
        // 1.2 拉这些包下所有启用表情，按 packId 分组
        List<ImFacePackItem> items = facePackItemService.getEnabledItemListByPackIds(
                convertList(packs, ImFacePack::getId));
        Map<Long, List<ImFacePackItem>> itemsByPackId = convertMultiMap(items, ImFacePackItem::getPackId);

        // 2. 拼装：BeanUtils 把 pack 字段映射 + 自己塞 items
        List<ImFacePackUserRespVo> result = convertList(packs, pack -> {
            ImFacePackUserRespVo vo = BeanUtils.toBean(pack, ImFacePackUserRespVo.class);
            vo.setItems(BeanUtils.toBean(itemsByPackId.getOrDefault(pack.getId(), List.of()), ImFacePackUserRespVo.Item.class));
            vo.getItems().forEach(item -> item.setUrl(fileUrlService.buildUrl(item.getUrl())));
            return vo;
        });
        return ok(result);
    }

}
