package com.fly.member.controller.app;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.AppMemberUserInfoRespVo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端 - 会员用户控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/user")
public class AppMemberUserController {

    private final IMemberUserService memberUserService;
    private final IMemberLevelService memberLevelService;

    /**
     * 获取当前会员用户信息。
     */
    @GetMapping("/get")
    public R<AppMemberUserInfoRespVo> getUser() {
        return R.ok(buildAppMemberUserInfo(memberUserService.queryById(UserUtils.getCurUserId())));
    }

    /**
     * 更新当前会员用户信息。
     */
    @PostMapping("/update")
    public R<Boolean> updateUser(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 更新当前会员用户信息，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdateUser(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 修改当前会员手机号。
     */
    @PutMapping("/update-mobile")
    public R<Boolean> updateMobile(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 通过微信修改当前会员手机号。
     */
    @PutMapping("/update-mobile-by-weixin")
    public R<Boolean> updateMobileByWeixin(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 修改当前会员密码。
     */
    @PutMapping("/update-password")
    public R<Boolean> updatePassword(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 重置当前会员密码。
     */
    @PutMapping("/reset-password")
    public R<Boolean> resetPassword(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 构建移动端会员个人信息响应对象。
     */
    private AppMemberUserInfoRespVo buildAppMemberUserInfo(MemberUserVo userVo) {
        if (userVo == null) {
            return null;
        }
        AppMemberUserInfoRespVo respVo = BeanUtil.toBean(userVo, AppMemberUserInfoRespVo.class);
        respVo.setBrokerageEnabled(false);
        if (userVo.getLevelId() != null) {
            MemberLevelVo levelVo = memberLevelService.queryById(userVo.getLevelId());
            if (levelVo != null) {
                AppMemberUserInfoRespVo.Level level = new AppMemberUserInfoRespVo.Level();
                level.setId(levelVo.getId());
                level.setName(levelVo.getName());
                level.setLevel(levelVo.getLevel());
                level.setIcon(levelVo.getIcon());
                respVo.setLevel(level);
            }
        }
        return respVo;
    }

}
