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
import com.fly.member.mapper.MemberLevelMapper;
import com.fly.member.service.IMemberExperienceRecordService;
import com.fly.member.service.IMemberLevelRecordService;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.MemberLevel;
import com.fly.system.api.member.domain.MemberLevelRecord;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 会员等级 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class MemberLevelServiceImpl implements IMemberLevelService {

    private final MemberLevelMapper memberLevelMapper;
    private final IMemberUserService memberUserService;
    private final IMemberLevelRecordService levelRecordService;
    private final IMemberExperienceRecordService experienceRecordService;

    @Override
    public PageVo<MemberLevelVo> queryPageList(MemberLevelBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberLevel> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(MemberLevel::getLevel);
        Page<MemberLevelVo> page = memberLevelMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberLevelVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<MemberLevelVo> queryList(MemberLevelBo bo) {
        LambdaQueryWrapper<MemberLevel> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(MemberLevel::getLevel);
        return memberLevelMapper.selectVoList(lqw);
    }

    @Override
    public MemberLevelVo queryById(Long id) {
        return memberLevelMapper.selectVoById(id);
    }

    @Override
    public Boolean saveOrUpdate(MemberLevelBo bo) {
        validateLevelConfig(bo.getId(), bo.getName(), bo.getLevel(), bo.getExperience());
        MemberLevel level = BeanUtil.toBean(bo, MemberLevel.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        level.setUpdateBy(userId);
        level.setUpdateTime(now);
        if (level.getId() != null) {
            validateExists(level.getId());
            return memberLevelMapper.updateById(level) > 0;
        }
        level.setIsDeleted(false);
        level.setCreateBy(userId);
        level.setCreateTime(now);
        return memberLevelMapper.insert(level) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        if (memberUserService.countByLevelId(id) > 0) {
            throw new ServiceException("会员等级下存在用户，不能删除");
        }
        MemberLevel level = new MemberLevel();
        level.setId(id);
        level.setIsDeleted(true);
        level.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        level.setUpdateTime(LocalDateTime.now());
        return memberLevelMapper.updateById(level) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExperience(Long userId, Integer experience, Integer bizType, String bizId) {
        if (experience == null || experience == 0) {
            return;
        }
        MemberUser user = memberUserService.getUser(userId);
        if (user == null) {
            throw new ServiceException("会员用户不存在");
        }
        int totalExperience = Math.max((user.getExperience() == null ? 0 : user.getExperience()) + experience, 0);
        MemberLevel newLevel = getMatchedLevel(totalExperience);
        Long newLevelId = newLevel == null ? user.getLevelId() : newLevel.getId();
        memberUserService.updateExperienceAndLevel(userId, experience, newLevelId);
        experienceRecordService.createExperienceRecord(userId, experience, totalExperience, bizType, bizId);
        if (newLevel != null && !newLevel.getId().equals(user.getLevelId())) {
            createLevelRecord(userId, newLevel, experience, totalExperience, "经验变化自动调整等级");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLevel(Long userId, Long levelId, String reason) {
        MemberUser user = memberUserService.getUser(userId);
        if (user == null) {
            throw new ServiceException("会员用户不存在");
        }
        MemberLevel level = levelId == null ? null : validateExists(levelId);
        int targetExperience = level == null ? 0 : level.getExperience();
        int changeExperience = targetExperience - (user.getExperience() == null ? 0 : user.getExperience());
        memberUserService.updateExperienceAndLevel(userId, changeExperience, levelId);
        if (level != null) {
            createLevelRecord(userId, level, changeExperience, targetExperience, reason);
        }
        experienceRecordService.createExperienceRecord(userId, changeExperience, targetExperience, 1,
                String.valueOf(userId));
    }

    private LambdaQueryWrapper<MemberLevel> buildQueryWrapper(MemberLevelBo bo) {
        LambdaQueryWrapper<MemberLevel> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberLevel::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberLevel::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), MemberLevel::getName, bo.getName());
        lqw.eq(bo.getLevel() != null, MemberLevel::getLevel, bo.getLevel());
        lqw.eq(bo.getStatus() != null, MemberLevel::getStatus, bo.getStatus());
        return lqw;
    }

    private MemberLevel validateExists(Long id) {
        MemberLevel level = memberLevelMapper.selectById(id);
        if (level == null || Boolean.TRUE.equals(level.getIsDeleted())) {
            throw new ServiceException("会员等级不存在");
        }
        return level;
    }

    private void validateLevelConfig(Long id, String name, Integer level, Integer experience) {
        List<MemberLevel> list = memberLevelMapper.selectList(Wrappers.<MemberLevel>lambdaQuery()
                .eq(MemberLevel::getIsDeleted, false));
        for (MemberLevel item : list) {
            if (id != null && id.equals(item.getId())) {
                continue;
            }
            if (StringUtils.isNotBlank(name) && name.equals(item.getName())) {
                throw new ServiceException("会员等级名称已存在");
            }
            if (level != null && level.equals(item.getLevel())) {
                throw new ServiceException("会员等级值已存在");
            }
            if (experience != null && experience.equals(item.getExperience())) {
                throw new ServiceException("会员等级经验值已存在");
            }
        }
    }

    private MemberLevel getMatchedLevel(Integer totalExperience) {
        return memberLevelMapper.selectList(Wrappers.<MemberLevel>lambdaQuery()
                        .eq(MemberLevel::getIsDeleted, false)
                        .eq(MemberLevel::getStatus, 0))
                .stream()
                .filter(level -> level.getExperience() != null && level.getExperience() <= totalExperience)
                .max(Comparator.comparing(MemberLevel::getLevel))
                .orElse(null);
    }

    private void createLevelRecord(Long userId, MemberLevel level, Integer experience, Integer userExperience, String reason) {
        MemberLevelRecord record = new MemberLevelRecord();
        record.setUserId(userId);
        record.setLevelId(level.getId());
        record.setLevel(level.getLevel());
        record.setDiscountPercent(level.getDiscountPercent());
        record.setExperience(experience);
        record.setUserExperience(userExperience);
        record.setRemark(reason);
        record.setDescription("会员等级调整为：" + level.getName());
        levelRecordService.createLevelRecord(record);
    }

}
