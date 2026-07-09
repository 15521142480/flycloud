package com.fly.im.controller.client.friend;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.framework.util.StrUtils;
import com.fly.system.api.im.domain.vo.ImFriendVo;
import com.fly.system.api.im.domain.bo.ImFriendBo;
import com.fly.system.api.im.domain.ImFriend;
import com.fly.im.service.friend.ImFriendService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.singleton;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "管理后台 - IM 好友")
@RestController
@RequestMapping({"/im/friend", "/admin/im/friend"})
@Validated
public class ImFriendController {

    @Resource
    private ImFriendService friendService;

    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/list")
    @Operation(summary = "获得当前登录用户的好友列表")
    public R<List<ImFriendVo>> getMyFriendList() {
        // 含 DISABLE 历史好友：保留给前端展示「已删除好友」的历史对话信息；前端按 status 决定会话级联清理
        List<ImFriend> friends = friendService.getFriendList(getCurUserId());
        return ok(buildFriendVoList(friends));
    }

    @GetMapping("/get")
    @Operation(summary = "获得好友详情")
    @Parameter(name = "friendUserId", description = "好友的用户编号", required = true, example = "2048")
    public R<ImFriendVo> getFriend(@RequestParam("friendUserId") Long friendUserId) {
        ImFriend friend = friendService.getFriend(getCurUserId(), friendUserId);
        return ok(buildFriendVo(friend));
    }

    @GetMapping("/pull")
    @Operation(summary = "增量拉取当前用户的好友关系（重连 / 离线补偿）")
    @Parameters({
            @Parameter(name = "lastUpdateTime", description = "上次拉取到的最新更新时间（毫秒时间戳）；首次拉取不传"),
            @Parameter(name = "lastId", description = "上次拉取到的最后一条记录 id；首次拉取不传"),
            @Parameter(name = "limit", description = "单次拉取条数", required = true)
    })
    public R<List<ImFriendVo>> pullMyFriendList(
            @RequestParam(value = "lastUpdateTime", required = false) Long lastUpdateTime,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam("limit") @Min(1) @Max(200) Integer limit) {
        List<ImFriend> list = friendService.pullFriendList(getCurUserId(), lastUpdateTime, lastId, limit);
        return R.ok(buildFriendVoList(list));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除好友（单向软删除）")
    @Parameters({
            @Parameter(description = "好友的用户编号", required = true, example = "2048"),
            @Parameter(description = "是否级联清理本端相关数据（如私聊会话）")
    })
    public R<Boolean> deleteFriend(
            @RequestParam("friendUserId") @NotNull(message = "好友用户编号不能为空") Long friendUserId,
            @RequestParam(value = "clear", required = false) Boolean clear) {
        friendService.deleteFriend(getCurUserId(), friendUserId, clear);
        return R.result(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新好友单边属性（备注 / 免打扰 / 联系人置顶）")
    public R<Boolean> updateFriend(@Valid @RequestBody ImFriendBo reqVo) {
        friendService.updateFriend(getCurUserId(), reqVo);
        return R.result(true);
    }

    @PutMapping("/block")
    @Operation(summary = "拉黑好友（必须先是好友；单边屏蔽对方私聊消息）")
    @Parameter(name = "friendUserId", description = "好友的用户编号", required = true, example = "2048")
    public R<Boolean> blockFriend(
            @RequestParam("friendUserId") @NotNull(message = "好友用户编号不能为空") Long friendUserId) {
        friendService.blockFriend(getCurUserId(), friendUserId);
        return R.result(true);
    }

    @PutMapping("/unblock")
    @Operation(summary = "移出黑名单")
    @Parameter(name = "friendUserId", description = "好友的用户编号", required = true, example = "2048")
    public R<Boolean> unblockFriend(
            @RequestParam("friendUserId") @NotNull(message = "好友用户编号不能为空") Long friendUserId) {
        friendService.unblockFriend(getCurUserId(), friendUserId);
        return R.result(true);
    }

    // ========== 私有方法：VO 组装 ==========

    private List<ImFriendVo> buildFriendVoList(Collection<ImFriend> friends) {
        if (CollUtil.isEmpty(friends)) {
            return Collections.emptyList();
        }
        // 批量聚合 AdminUser 信息（昵称 / 头像），避免 N+1
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertList(friends, ImFriend::getFriendUserId));
        return convertList(friends, friend -> {
            ImFriendVo vo = BeanUtils.toBean(friend, ImFriendVo.class);
            MapUtils.findAndThen(userMap, friend.getFriendUserId(), user ->
                    vo.setNickname(user.getName()).setAvatar(user.getAvatar()));
            // 备注 / 昵称的拼音，给前端做字母分桶 + 拼音搜索
            vo.setDisplayNamePinyin(StrUtils.toPinyin(vo.getDisplayName()))
                    .setNicknamePinyin(StrUtils.toPinyin(vo.getNickname()));
            return vo;
        });
    }

    private ImFriendVo buildFriendVo(ImFriend friend) {
        if (friend == null) {
            return null;
        }
        return CollUtil.getFirst(buildFriendVoList(singleton(friend)));
    }

}
