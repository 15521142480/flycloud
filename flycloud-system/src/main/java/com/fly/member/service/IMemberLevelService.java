package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;

import java.util.List;

/**
 * 会员等级 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberLevelService {
    PageVo<MemberLevelVo> queryPageList(MemberLevelBo bo, PageBo pageBo);
    List<MemberLevelVo> queryList(MemberLevelBo bo);
    MemberLevelVo queryById(Long id);
    Long createLevel(MemberLevelBo bo);
    Boolean saveOrUpdate(MemberLevelBo bo);
    Boolean deleteById(Long id);
    void addExperience(Long userId, Integer experience, Integer bizType, String bizId);
    void updateUserLevel(Long userId, Long levelId, String reason);
}
