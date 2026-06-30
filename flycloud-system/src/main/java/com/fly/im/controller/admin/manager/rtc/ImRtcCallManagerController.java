package com.fly.im.controller.admin.manager.rtc;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.rtc.vo.ImRtcCallManagerPageReqVO;
import com.fly.im.controller.admin.manager.rtc.vo.ImRtcCallManagerRespVO;
import com.fly.im.controller.admin.manager.rtc.vo.ImRtcParticipantManagerRespVO;
import com.fly.im.dal.dataobject.group.ImGroupDO;
import com.fly.im.dal.dataobject.rtc.ImRtcCallDO;
import com.fly.im.dal.dataobject.rtc.ImRtcParticipantDO;
import com.fly.im.service.group.ImGroupService;
import com.fly.im.service.rtc.ImRtcCallService;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - IM 通话记录")
@RestController
@RequestMapping("/im/manager/rtc")
@Validated
public class ImRtcCallManagerController {

    @Resource
    private ImRtcCallService rtcCallService;
    @Resource
    private ImGroupService groupService;

    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得通话记录分页")
    @PreAuthorize("@pms.hasPermission('im:manager:rtc:query')")
    public R<PageResult<ImRtcCallManagerRespVO>> getCallPage(@Valid ImRtcCallManagerPageReqVO pageReqVO) {
        PageResult<ImRtcCallDO> pageResult = rtcCallService.getCallPage(pageReqVO);
        return ok(buildCallRespVOPage(pageResult));
    }

    @GetMapping("/get")
    @Operation(summary = "获得通话记录详情")
    @Parameter(name = "id", description = "通话编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:rtc:query')")
    public R<ImRtcCallManagerRespVO> getCall(@RequestParam("id") Long id) {
        ImRtcCallDO call = rtcCallService.getCall(id);
        if (call == null) {
            return ok((ImRtcCallManagerRespVO) null);
        }
        return ok(CollUtil.getFirst(buildCallRespVOList(Collections.singletonList(call))));
    }

    @GetMapping("/participant-list")
    @Operation(summary = "获得通话参与者列表")
    @Parameter(name = "id", description = "通话编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:rtc:query')")
    public R<List<ImRtcParticipantManagerRespVO>> getCallParticipantList(@RequestParam("id") Long id) {
        List<ImRtcParticipantDO> participants = rtcCallService.getCallParticipantListByCallId(id);
        if (CollUtil.isEmpty(participants)) {
            return ok(Collections.emptyList());
        }
        // 查询用户信息
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertSet(participants, ImRtcParticipantDO::getUserId));
        // 组装返回
        return ok(BeanUtils.toBean(participants, ImRtcParticipantManagerRespVO.class, vo ->
                MapUtils.findAndThen(userMap, vo.getUserId(),
                        user -> vo.setUserNickname(user.getName()))));
    }

    // ========== 私有方法：VO 组装 ==========

    private PageResult<ImRtcCallManagerRespVO> buildCallRespVOPage(PageResult<ImRtcCallDO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        return new PageResult<>(buildCallRespVOList(pageResult.getList()), pageResult.getTotal());
    }

    private List<ImRtcCallManagerRespVO> buildCallRespVOList(List<ImRtcCallDO> calls) {
        // 查询用户信息
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertSet(calls, ImRtcCallDO::getInviterUserId));
        Map<Long, ImGroupDO> groupMap = groupService.getGroupMap(
                convertSet(calls, ImRtcCallDO::getGroupId));
        // 组装返回
        return BeanUtils.toBean(calls, ImRtcCallManagerRespVO.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getInviterUserId(),
                    user -> vo.setInviterNickname(user.getName()));
            MapUtils.findAndThen(groupMap, vo.getGroupId(),
                    group -> vo.setGroupName(group.getName()));
        });
    }

}
