package com.fly.im.controller.admin.manager.message;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.message.vo.privates.ImPrivateMessageManagerPageReqVo;
import com.fly.im.controller.admin.manager.message.vo.privates.ImPrivateMessageManagerRespVo;
import com.fly.im.dal.dataobject.message.ImPrivateMessageDO;
import com.fly.im.service.message.ImPrivateMessageService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSetByFlatMap;

@Tag(name = "管理后台 - IM 私聊消息")
@RestController
@RequestMapping({"/im/manager/message/private", "/admin/im/manager/message/private"})
@Validated
public class ImPrivateMessageManagerController {

    @Resource
    private ImPrivateMessageService privateMessageService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得私聊消息分页")
    @PreAuthorize("@pms.hasPermission('im:manager:message:query')")
    public R<PageResult<ImPrivateMessageManagerRespVo>> getPrivateMessagePage(
            @Valid ImPrivateMessageManagerPageReqVo pageReqVo) {
        // 1. 分页查询
        PageResult<ImPrivateMessageDO> pageResult = privateMessageService.getPrivateMessagePage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 2.1 一次性批量查询发送人 + 接收人昵称
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(convertSetByFlatMap(pageResult.getList(),
                m -> Stream.of(m.getSenderId(), m.getReceiverId())));
        // 2.2 转换为 VO，填充昵称
        return ok(PageResult.convert(pageResult, ImPrivateMessageManagerRespVo.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getSenderId(), user -> vo.setSenderNickname(user.getName()));
            MapUtils.findAndThen(userMap, vo.getReceiverId(), user -> vo.setReceiverNickname(user.getName()));
        }));
    }

    @GetMapping("/get")
    @Operation(summary = "获得私聊消息详情")
    @Parameter(name = "id", description = "消息编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:message:query')")
    public R<ImPrivateMessageManagerRespVo> getPrivateMessage(@RequestParam("id") Long id) {
        ImPrivateMessageDO message = privateMessageService.getPrivateMessage(id);
        return ok(BeanUtils.toBean(message, ImPrivateMessageManagerRespVo.class));
    }

}
