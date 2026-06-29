package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberSignInRecordBo;
import com.fly.system.api.member.domain.vo.AppMemberSignInRecordSummaryRespVo;
import com.fly.system.api.member.domain.vo.MemberSignInRecordVo;

/**
 * 会员签到记录 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberSignInRecordService {
    PageVo<MemberSignInRecordVo> queryPageList(MemberSignInRecordBo bo, PageBo pageBo);
    PageVo<MemberSignInRecordVo> queryAppPage(Long userId, PageBo pageBo);
    AppMemberSignInRecordSummaryRespVo getSummary(Long userId);
    MemberSignInRecordVo createSignRecord(Long userId);
}
