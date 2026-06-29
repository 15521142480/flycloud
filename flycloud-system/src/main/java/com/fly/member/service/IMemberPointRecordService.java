package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberPointRecordBo;
import com.fly.system.api.member.domain.vo.MemberPointRecordVo;

/**
 * 会员积分记录 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberPointRecordService {
    PageVo<MemberPointRecordVo> queryPageList(MemberPointRecordBo bo, PageBo pageBo);
    void createPointRecord(Long userId, Integer point, Integer bizType, String bizId);
}
