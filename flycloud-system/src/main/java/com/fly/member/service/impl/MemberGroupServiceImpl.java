package com.fly.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.member.mapper.MemberGroupMapper;
import com.fly.member.service.IMemberGroupService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.MemberGroup;
import com.fly.system.api.member.domain.bo.MemberGroupBo;
import com.fly.system.api.member.domain.vo.MemberGroupVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员分组 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class MemberGroupServiceImpl implements IMemberGroupService {

    private final MemberGroupMapper memberGroupMapper;
    private final IMemberUserService memberUserService;

    @Override
    public PageVo<MemberGroupVo> queryPageList(MemberGroupBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberGroup> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberGroup::getId);
        Page<MemberGroupVo> page = memberGroupMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberGroupVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<MemberGroupVo> queryList(MemberGroupBo bo) {
        LambdaQueryWrapper<MemberGroup> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberGroup::getId);
        return memberGroupMapper.selectVoList(lqw);
    }

    @Override
    public MemberGroupVo queryById(Long id) {
        return memberGroupMapper.selectVoById(id);
    }

    @Override
    public Long createGroup(MemberGroupBo bo) {
        MemberGroup group = BeanUtil.toBean(bo, MemberGroup.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        group.setIsDeleted(false);
        group.setCreateBy(userId);
        group.setCreateTime(now);
        group.setUpdateBy(userId);
        group.setUpdateTime(now);
        memberGroupMapper.insert(group);
        return group.getId();
    }

    @Override
    public Boolean saveOrUpdate(MemberGroupBo bo) {
        MemberGroup group = BeanUtil.toBean(bo, MemberGroup.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        group.setUpdateBy(userId);
        group.setUpdateTime(now);
        if (group.getId() != null) {
            validateExists(group.getId());
            return memberGroupMapper.updateById(group) > 0;
        }
        group.setIsDeleted(false);
        group.setCreateBy(userId);
        group.setCreateTime(now);
        return memberGroupMapper.insert(group) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        if (memberUserService.countByGroupId(id) > 0) {
            throw new ServiceException("会员分组下存在用户，不能删除");
        }
        MemberGroup group = new MemberGroup();
        group.setId(id);
        group.setIsDeleted(true);
        group.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        group.setUpdateTime(LocalDateTime.now());
        return memberGroupMapper.updateById(group) > 0;
    }

    private LambdaQueryWrapper<MemberGroup> buildQueryWrapper(MemberGroupBo bo) {
        LambdaQueryWrapper<MemberGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberGroup::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberGroup::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), MemberGroup::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, MemberGroup::getStatus, bo.getStatus());
        return lqw;
    }

    private void validateExists(Long id) {
        MemberGroup group = memberGroupMapper.selectById(id);
        if (group == null || Boolean.TRUE.equals(group.getIsDeleted())) {
            throw new ServiceException("会员分组不存在");
        }
    }

}
