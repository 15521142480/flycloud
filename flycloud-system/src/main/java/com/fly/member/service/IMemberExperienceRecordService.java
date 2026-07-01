package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberExperienceRecordBo;
import com.fly.system.api.member.domain.vo.MemberExperienceRecordVo;

/**
 * 会员经验记录 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IMemberExperienceRecordService {
    PageVo<MemberExperienceRecordVo> queryPageList(MemberExperienceRecordBo bo, PageBo pageBo);
    MemberExperienceRecordVo queryById(Long id);
    void createExperienceRecord(Long userId, Integer experience, Integer totalExperience, Integer bizType, String bizId);
}
