package com.fly.member.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberGroupService;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberTagService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.bo.MemberGroupBo;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.bo.MemberTagBo;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberGroupVo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import com.fly.system.api.member.domain.vo.MemberTagVo;
import com.fly.system.api.member.domain.vo.MemberUserRespVo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 后台 - 会员用户控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/user")
public class MemberUserController {

    private static final Integer POINT_BIZ_TYPE_ADMIN = 2;

    private final IMemberUserService memberUserService;
    private final IMemberLevelService memberLevelService;
    private final IMemberPointRecordService memberPointRecordService;
    private final IMemberGroupService memberGroupService;
    private final IMemberTagService memberTagService;

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/list")
    public R<PageVo<MemberUserRespVo>> list(MemberUserBo bo, PageBo pageBo) {
        return R.ok(buildMemberUserRespPage(memberUserService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/page")
    public R<PageVo<MemberUserRespVo>> page(MemberUserBo bo, PageBo pageBo) {
        return R.ok(buildMemberUserRespPage(memberUserService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/allList")
    public R<List<MemberUserVo>> allList(MemberUserBo bo) {
        return R.ok(memberUserService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/get/{id}")
    public R<MemberUserRespVo> getInfo(@PathVariable Long id) {
        return R.ok(buildMemberUserResp(memberUserService.queryById(id)));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/get")
    public R<MemberUserRespVo> get(@RequestParam("id") Long id) {
        return R.ok(buildMemberUserResp(memberUserService.queryById(id)));
    }

    @PreAuthorize("@pms.hasPermission('member:user:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Boolean> saveOrUpdate(@RequestBody MemberUserBo bo) {
        return R.result(memberUserService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:update')")
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody MemberUserBo bo) {
        return R.result(memberUserService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:update-level')")
    @PutMapping("/update-level")
    public R<Boolean> updateLevel(@Valid @RequestBody MemberUserUpdateLevelReq req) {
        memberLevelService.updateUserLevel(req.getId(), req.getLevelId(), req.getReason());
        return R.result(true);
    }

    @PreAuthorize("@pms.hasPermission('member:user:update-point')")
    @PutMapping("/update-point")
    public R<Boolean> updatePoint(@Valid @RequestBody MemberUserUpdatePointReq req) {
        memberPointRecordService.createPointRecord(req.getId(), req.getPoint(),
                POINT_BIZ_TYPE_ADMIN, String.valueOf(UserUtils.getCurUserId()));
        return R.result(true);
    }

    @Data
    public static class MemberUserUpdateLevelReq {
        @NotNull(message = "会员编号不能为空")
        private Long id;
        private Long levelId;
        private String reason;
    }

    /**
     * 会员积分修改请求。
     *
     * @author lxs
     * @date 2026-07-02
     */
    @Data
    public static class MemberUserUpdatePointReq {
        @NotNull(message = "会员编号不能为空")
        private Long id;
        @NotNull(message = "变动积分不能为空")
        private Integer point;
    }

    /**
     * 构建会员用户分页响应对象，补齐等级、分组、标签名称。
     */
    private PageVo<MemberUserRespVo> buildMemberUserRespPage(PageVo<MemberUserVo> pageVo) {
        PageVo<MemberUserRespVo> result = new PageVo<>();
        result.setTotal(pageVo.getTotal());
        result.setPages(pageVo.getPages());
        result.setList(buildMemberUserRespList(pageVo.getList()));
        return result;
    }

    /**
     * 构建会员用户响应对象。
     */
    private MemberUserRespVo buildMemberUserResp(MemberUserVo userVo) {
        List<MemberUserRespVo> list = buildMemberUserRespList(userVo == null ? Collections.emptyList() : List.of(userVo));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 批量构建会员用户响应对象。
     */
    private List<MemberUserRespVo> buildMemberUserRespList(List<MemberUserVo> userList) {
        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, MemberLevelVo> levelMap = memberLevelService.queryList(new MemberLevelBo()).stream()
                .collect(Collectors.toMap(MemberLevelVo::getId, Function.identity(), (a, b) -> a));
        Map<Long, MemberGroupVo> groupMap = memberGroupService.queryList(new MemberGroupBo()).stream()
                .collect(Collectors.toMap(MemberGroupVo::getId, Function.identity(), (a, b) -> a));
        Map<Long, MemberTagVo> tagMap = memberTagService.queryList(new MemberTagBo()).stream()
                .collect(Collectors.toMap(MemberTagVo::getId, Function.identity(), (a, b) -> a));
        return userList.stream().map(userVo -> {
            MemberUserRespVo respVo = BeanUtil.toBean(userVo, MemberUserRespVo.class);
            respVo.setTotalPoint(userVo.getPoint());
            MemberLevelVo level = userVo.getLevelId() == null ? null : levelMap.get(userVo.getLevelId());
            if (level != null) {
                respVo.setLevelName(level.getName());
            }
            MemberGroupVo group = userVo.getGroupId() == null ? null : groupMap.get(userVo.getGroupId());
            if (group != null) {
                respVo.setGroupName(group.getName());
            }
            respVo.setTagNames(parseTagIds(userVo.getTagIds()).stream()
                    .map(tagMap::get)
                    .filter(Objects::nonNull)
                    .map(MemberTagVo::getName)
                    .toList());
            return respVo;
        }).toList();
    }

    /**
     * 解析会员标签编号列表。
     */
    private List<Long> parseTagIds(String tagIds) {
        if (tagIds == null || tagIds.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tagIds.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Long::valueOf)
                .toList();
    }

}
