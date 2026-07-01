package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberUserVo;

import java.util.List;

/**
 * 会员用户 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IMemberUserService {
    PageVo<MemberUserVo> queryPageList(MemberUserBo bo, PageBo pageBo);
    List<MemberUserVo> queryList(MemberUserBo bo);
    MemberUserVo queryById(Long id);
    MemberUser getUser(Long id);
    Boolean saveOrUpdate(MemberUserBo bo);
    Long countByGroupId(Long groupId);
    Long countByLevelId(Long levelId);
    Long countByTagId(Long tagId);
    void updatePoint(Long userId, Integer point);
    void updateExperienceAndLevel(Long userId, Integer experience, Long levelId);
}
