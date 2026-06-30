package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员用户控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/user")
public class MemberUserController {

    private static final Integer POINT_BIZ_TYPE_ADMIN = 2;

    private final IMemberUserService memberUserService;
    private final IMemberLevelService memberLevelService;
    private final IMemberPointRecordService memberPointRecordService;

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/list")
    public R<PageVo<MemberUserVo>> list(MemberUserBo bo, PageBo pageBo) {
        return R.ok(memberUserService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/page")
    public R<PageVo<MemberUserVo>> page(MemberUserBo bo, PageBo pageBo) {
        return R.ok(memberUserService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/allList")
    public R<List<MemberUserVo>> allList(MemberUserBo bo) {
        return R.ok(memberUserService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/get/{id}")
    public R<MemberUserVo> getInfo(@PathVariable Long id) {
        return R.ok(memberUserService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/get")
    public R<MemberUserVo> get(@RequestParam("id") Long id) {
        return R.ok(memberUserService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:user:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody MemberUserBo bo) {
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:update')")
    @PutMapping("/update")
    public R<Void> update(@RequestBody MemberUserBo bo) {
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:update-level')")
    @RequestMapping(value = "/update-level", method = {RequestMethod.POST, RequestMethod.PUT})
    public R<Void> updateLevel(@RequestBody MemberUserUpdateLevelReq req) {
        memberLevelService.updateUserLevel(req.getId(), req.getLevelId(), req.getReason());
        return R.ok();
    }

    @PreAuthorize("@pms.hasPermission('member:user:update-point')")
    @PutMapping("/update-point")
    public R<Void> updatePoint(@Valid @RequestBody MemberUserUpdatePointReq req) {
        memberPointRecordService.createPointRecord(req.getId(), req.getPoint(),
                POINT_BIZ_TYPE_ADMIN, String.valueOf(UserUtils.getCurUserId()));
        return R.ok();
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
     * @date 2026-06-30
     */
    @Data
    public static class MemberUserUpdatePointReq {
        @NotNull(message = "会员编号不能为空")
        private Long id;
        @NotNull(message = "变动积分不能为空")
        private Integer point;
    }

}
