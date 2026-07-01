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
import com.fly.member.mapper.MemberTagMapper;
import com.fly.member.service.IMemberTagService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.MemberTag;
import com.fly.system.api.member.domain.bo.MemberTagBo;
import com.fly.system.api.member.domain.vo.MemberTagVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员标签 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class MemberTagServiceImpl implements IMemberTagService {

    private final MemberTagMapper memberTagMapper;
    private final IMemberUserService memberUserService;

    @Override
    public PageVo<MemberTagVo> queryPageList(MemberTagBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberTag> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberTag::getId);
        Page<MemberTagVo> page = memberTagMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberTagVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<MemberTagVo> queryList(MemberTagBo bo) {
        LambdaQueryWrapper<MemberTag> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberTag::getId);
        return memberTagMapper.selectVoList(lqw);
    }

    @Override
    public MemberTagVo queryById(Long id) {
        return memberTagMapper.selectVoById(id);
    }

    @Override
    public Long createTag(MemberTagBo bo) {
        validateNameUnique(null, bo.getName());
        MemberTag tag = BeanUtil.toBean(bo, MemberTag.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        tag.setIsDeleted(false);
        tag.setCreateBy(userId);
        tag.setCreateTime(now);
        tag.setUpdateBy(userId);
        tag.setUpdateTime(now);
        memberTagMapper.insert(tag);
        return tag.getId();
    }

    @Override
    public Boolean saveOrUpdate(MemberTagBo bo) {
        validateNameUnique(bo.getId(), bo.getName());
        MemberTag tag = BeanUtil.toBean(bo, MemberTag.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        tag.setUpdateBy(userId);
        tag.setUpdateTime(now);
        if (tag.getId() != null) {
            validateExists(tag.getId());
            return memberTagMapper.updateById(tag) > 0;
        }
        tag.setIsDeleted(false);
        tag.setCreateBy(userId);
        tag.setCreateTime(now);
        return memberTagMapper.insert(tag) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        if (memberUserService.countByTagId(id) > 0) {
            throw new ServiceException("会员标签下存在用户，不能删除");
        }
        MemberTag tag = new MemberTag();
        tag.setId(id);
        tag.setIsDeleted(true);
        tag.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        tag.setUpdateTime(LocalDateTime.now());
        return memberTagMapper.updateById(tag) > 0;
    }

    private LambdaQueryWrapper<MemberTag> buildQueryWrapper(MemberTagBo bo) {
        LambdaQueryWrapper<MemberTag> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberTag::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberTag::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), MemberTag::getName, bo.getName());
        return lqw;
    }

    private void validateExists(Long id) {
        MemberTag tag = memberTagMapper.selectById(id);
        if (tag == null || Boolean.TRUE.equals(tag.getIsDeleted())) {
            throw new ServiceException("会员标签不存在");
        }
    }

    private void validateNameUnique(Long id, String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        LambdaQueryWrapper<MemberTag> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberTag::getIsDeleted, false);
        lqw.eq(MemberTag::getName, name);
        lqw.last("LIMIT 1");
        MemberTag tag = memberTagMapper.selectOne(lqw);
        if (tag != null && !tag.getId().equals(id)) {
            throw new ServiceException("会员标签名称已存在");
        }
    }

}
