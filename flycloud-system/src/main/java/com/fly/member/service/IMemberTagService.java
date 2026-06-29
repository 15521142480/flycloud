package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.MemberTagBo;
import com.fly.system.api.member.domain.vo.MemberTagVo;

import java.util.List;

/**
 * 会员标签 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberTagService {
    PageVo<MemberTagVo> queryPageList(MemberTagBo bo, PageBo pageBo);
    List<MemberTagVo> queryList(MemberTagBo bo);
    MemberTagVo queryById(Long id);
    Boolean saveOrUpdate(MemberTagBo bo);
    Boolean deleteById(Long id);
}
