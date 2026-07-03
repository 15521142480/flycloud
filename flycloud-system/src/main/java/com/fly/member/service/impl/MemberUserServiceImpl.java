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
import com.fly.member.mapper.MemberUserMapper;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员用户 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class MemberUserServiceImpl implements IMemberUserService {

    private final MemberUserMapper memberUserMapper;

    @Override
    public PageVo<MemberUserVo> queryPageList(MemberUserBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberUser> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberUser::getId);
        Page<MemberUserVo> page = memberUserMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberUserVo> pageVo = new PageVo<>();
        List<MemberUserVo> list = page.getRecords();
        list.forEach(this::formatAvatar);
        pageVo.setList(list);
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<MemberUserVo> queryList(MemberUserBo bo) {
        LambdaQueryWrapper<MemberUser> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberUser::getId);
        List<MemberUserVo> list = memberUserMapper.selectVoList(lqw);
        list.forEach(this::formatAvatar);
        return list;
    }

    @Override
    public MemberUserVo queryById(Long id) {
        MemberUserVo user = memberUserMapper.selectVoById(id);
        formatAvatar(user);
        return user;
    }

    @Override
    public MemberUser getUser(Long id) {
        if (id == null) {
            return null;
        }
        MemberUser user = memberUserMapper.selectById(id);
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            return null;
        }
        user.setAvatar(user.getAvatar());
        return user;
    }

    @Override
    public Boolean saveOrUpdate(MemberUserBo bo) {
        validateMobileUnique(bo.getId(), bo.getMobile());
        MemberUser user = BeanUtil.toBean(bo, MemberUser.class);
        user.setAvatar(user.getAvatar());
        LocalDateTime now = LocalDateTime.now();
        String loginUserId = String.valueOf(UserUtils.getCurUserId());
        user.setUpdateBy(loginUserId);
        user.setUpdateTime(now);
        if (user.getId() != null) {
            validateExists(user.getId());
            return memberUserMapper.updateById(user) > 0;
        }
        if (user.getStatus() == null) {
            user.setStatus(0);
        }
        if (user.getPoint() == null) {
            user.setPoint(0);
        }
        if (user.getExperience() == null) {
            user.setExperience(0);
        }
        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname("会员" + System.currentTimeMillis());
        }
        user.setIsDeleted(false);
        user.setCreateBy(loginUserId);
        user.setCreateTime(now);
        return memberUserMapper.insert(user) > 0;
    }

    @Override
    public Long countByGroupId(Long groupId) {
        LambdaQueryWrapper<MemberUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberUser::getIsDeleted, false);
        lqw.eq(MemberUser::getGroupId, groupId);
        return memberUserMapper.selectCount(lqw);
    }

    @Override
    public Long countByLevelId(Long levelId) {
        LambdaQueryWrapper<MemberUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberUser::getIsDeleted, false);
        lqw.eq(MemberUser::getLevelId, levelId);
        return memberUserMapper.selectCount(lqw);
    }

    @Override
    public Long countByTagId(Long tagId) {
        LambdaQueryWrapper<MemberUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberUser::getIsDeleted, false);
        lqw.and(wrapper -> wrapper.eq(MemberUser::getTagIds, String.valueOf(tagId))
                .or().like(MemberUser::getTagIds, tagId + ",")
                .or().like(MemberUser::getTagIds, "," + tagId + ",")
                .or().like(MemberUser::getTagIds, "," + tagId));
        return memberUserMapper.selectCount(lqw);
    }

    @Override
    public void updatePoint(Long userId, Integer point) {
        MemberUser user = validateExists(userId);
        int totalPoint = (user.getPoint() == null ? 0 : user.getPoint()) + point;
        if (totalPoint < 0) {
            throw new ServiceException("会员积分不足");
        }
        MemberUser updateUser = new MemberUser();
        updateUser.setId(userId);
        updateUser.setPoint(totalPoint);
        updateUser.setUpdateBy(String.valueOf(userId));
        updateUser.setUpdateTime(LocalDateTime.now());
        memberUserMapper.updateById(updateUser);
    }

    @Override
    public void updateExperienceAndLevel(Long userId, Integer experience, Long levelId) {
        MemberUser user = validateExists(userId);
        int totalExperience = Math.max((user.getExperience() == null ? 0 : user.getExperience()) + experience, 0);
        MemberUser updateUser = new MemberUser();
        updateUser.setId(userId);
        updateUser.setExperience(totalExperience);
        updateUser.setLevelId(levelId);
        updateUser.setUpdateBy(String.valueOf(userId));
        updateUser.setUpdateTime(LocalDateTime.now());
        memberUserMapper.updateById(updateUser);
    }

    /**
     * 构建会员用户查询条件。
     */
    private LambdaQueryWrapper<MemberUser> buildQueryWrapper(MemberUserBo bo) {
        LambdaQueryWrapper<MemberUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberUser::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberUser::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getMobile()), MemberUser::getMobile, bo.getMobile());
        lqw.like(StringUtils.isNotBlank(bo.getEmail()), MemberUser::getEmail, bo.getEmail());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), MemberUser::getNickname, bo.getNickname());
        lqw.eq(bo.getStatus() != null, MemberUser::getStatus, bo.getStatus());
        lqw.eq(bo.getGroupId() != null, MemberUser::getGroupId, bo.getGroupId());
        lqw.eq(bo.getLevelId() != null, MemberUser::getLevelId, bo.getLevelId());
        return lqw;
    }

    /**
     * 校验会员存在。
     */
    private MemberUser validateExists(Long id) {
        MemberUser user = getUser(id);
        if (user == null) {
            throw new ServiceException("会员用户不存在");
        }
        return user;
    }

    /**
     * 校验手机号唯一。
     */
    private void validateMobileUnique(Long id, String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        LambdaQueryWrapper<MemberUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberUser::getIsDeleted, false);
        lqw.eq(MemberUser::getMobile, mobile);
        lqw.last("LIMIT 1");
        MemberUser user = memberUserMapper.selectOne(lqw);
        if (user != null && !user.getId().equals(id)) {
            throw new ServiceException("手机号已被会员使用");
        }
    }

    private void formatAvatar(MemberUserVo user) {
        if (user == null) {
            return;
        }
        user.setAvatar(user.getAvatar());
    }

}
