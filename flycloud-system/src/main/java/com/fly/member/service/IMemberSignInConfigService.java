package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;

import java.util.List;

/**
 * 会员签到规则 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IMemberSignInConfigService {
    PageVo<MemberSignInConfigVo> queryPageList(MemberSignInConfigBo bo, PageBo pageBo);
    List<MemberSignInConfigVo> queryList(MemberSignInConfigBo bo);
    MemberSignInConfigVo queryById(Long id);
    Long createSignInConfig(MemberSignInConfigBo bo);
    Boolean saveOrUpdate(MemberSignInConfigBo bo);
    Boolean deleteById(Long id);
}
