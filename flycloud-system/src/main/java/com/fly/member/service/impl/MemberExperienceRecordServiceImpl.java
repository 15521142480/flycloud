package com.fly.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.mapper.MemberExperienceRecordMapper;
import com.fly.member.service.IMemberExperienceRecordService;
import com.fly.system.api.member.domain.MemberExperienceRecord;
import com.fly.system.api.member.domain.bo.MemberExperienceRecordBo;
import com.fly.system.api.member.domain.vo.MemberExperienceRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 会员经验记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class MemberExperienceRecordServiceImpl implements IMemberExperienceRecordService {

    private final MemberExperienceRecordMapper experienceRecordMapper;

    @Override
    public PageVo<MemberExperienceRecordVo> queryPageList(MemberExperienceRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberExperienceRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberExperienceRecord::getIsDeleted, false);
        if (bo != null) {
            lqw.eq(bo.getUserId() != null, MemberExperienceRecord::getUserId, bo.getUserId());
            lqw.eq(bo.getBizType() != null, MemberExperienceRecord::getBizType, bo.getBizType());
        }
        lqw.orderByDesc(MemberExperienceRecord::getId);
        Page<MemberExperienceRecordVo> page = experienceRecordMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberExperienceRecordVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public void createExperienceRecord(Long userId, Integer experience, Integer totalExperience, Integer bizType, String bizId) {
        if (experience == null || experience == 0) {
            return;
        }
        MemberExperienceRecord record = new MemberExperienceRecord();
        record.setUserId(userId);
        record.setBizType(bizType);
        record.setBizId(bizId);
        record.setTitle(experience > 0 ? "获得经验" : "扣减经验");
        record.setDescription("会员经验变动：" + experience);
        record.setExperience(experience);
        record.setTotalExperience(totalExperience);
        record.setIsDeleted(false);
        record.setCreateBy(String.valueOf(userId));
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateBy(String.valueOf(userId));
        record.setUpdateTime(LocalDateTime.now());
        experienceRecordMapper.insert(record);
    }

}
