package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberGroupBo;
import com.fly.system.api.member.domain.vo.MemberGroupVo;

import java.util.List;

/**
 * 会员分组 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberGroupService {
    PageVo<MemberGroupVo> queryPageList(MemberGroupBo bo, PageBo pageBo);
    List<MemberGroupVo> queryList(MemberGroupBo bo);
    MemberGroupVo queryById(Long id);
    Boolean saveOrUpdate(MemberGroupBo bo);
    Boolean deleteById(Long id);
}
