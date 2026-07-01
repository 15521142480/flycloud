package com.fly.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.member.mapper.MemberSignInRecordMapper;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.member.service.IMemberSignInConfigService;
import com.fly.member.service.IMemberSignInRecordService;
import com.fly.system.api.member.domain.MemberSignInRecord;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.bo.MemberSignInRecordBo;
import com.fly.system.api.member.domain.vo.AppMemberSignInRecordSummaryRespVo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;
import com.fly.system.api.member.domain.vo.MemberSignInRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 会员签到记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class MemberSignInRecordServiceImpl implements IMemberSignInRecordService {

    private static final int BIZ_TYPE_SIGN = 10;

    private final MemberSignInRecordMapper signInRecordMapper;
    private final IMemberSignInConfigService signInConfigService;
    private final IMemberPointRecordService pointRecordService;
    private final IMemberLevelService levelService;

    @Override
    public PageVo<MemberSignInRecordVo> queryPageList(MemberSignInRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberSignInRecord> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberSignInRecord::getId);
        Page<MemberSignInRecordVo> page = signInRecordMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberSignInRecordVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public PageVo<MemberSignInRecordVo> queryAppPage(Long userId, PageBo pageBo) {
        MemberSignInRecordBo bo = new MemberSignInRecordBo();
        bo.setUserId(userId);
        return queryPageList(bo, pageBo);
    }

    @Override
    public AppMemberSignInRecordSummaryRespVo getSummary(Long userId) {
        AppMemberSignInRecordSummaryRespVo summary = new AppMemberSignInRecordSummaryRespVo();
        summary.setTotalDay(Math.toIntExact(selectCountByUserId(userId)));
        MemberSignInRecord lastRecord = selectLastRecord(userId);
        summary.setTodaySignIn(lastRecord != null && isToday(lastRecord.getCreateTime()));
        summary.setContinuousDay(lastRecord == null ? 0 : lastRecord.getDay());
        if (lastRecord != null && !summary.getTodaySignIn() && !isYesterday(lastRecord.getCreateTime())) {
            summary.setContinuousDay(0);
        }
        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberSignInRecordVo createSignRecord(Long userId) {
        MemberSignInRecord lastRecord = selectLastRecord(userId);
        if (lastRecord != null && isToday(lastRecord.getCreateTime())) {
            throw new ServiceException("今天已经签到");
        }
        int day = lastRecord != null && isYesterday(lastRecord.getCreateTime()) ? lastRecord.getDay() + 1 : 1;
        MemberSignInConfigVo config = getMatchedConfig(day);
        MemberSignInRecord record = new MemberSignInRecord();
        record.setUserId(userId);
        record.setDay(day);
        record.setPoint(config == null || config.getPoint() == null ? 0 : config.getPoint());
        record.setExperience(config == null || config.getExperience() == null ? 0 : config.getExperience());
        record.setIsDeleted(false);
        record.setCreateBy(String.valueOf(userId));
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateBy(String.valueOf(userId));
        record.setUpdateTime(LocalDateTime.now());
        signInRecordMapper.insert(record);
        pointRecordService.createPointRecord(userId, record.getPoint(), BIZ_TYPE_SIGN, String.valueOf(record.getId()));
        levelService.addExperience(userId, record.getExperience(), BIZ_TYPE_SIGN, String.valueOf(record.getId()));
        return BeanUtil.toBean(record, MemberSignInRecordVo.class);
    }

    private LambdaQueryWrapper<MemberSignInRecord> buildQueryWrapper(MemberSignInRecordBo bo) {
        LambdaQueryWrapper<MemberSignInRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberSignInRecord::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberSignInRecord::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, MemberSignInRecord::getUserId, bo.getUserId());
        lqw.eq(bo.getDay() != null, MemberSignInRecord::getDay, bo.getDay());
        return lqw;
    }

    private MemberSignInRecord selectLastRecord(Long userId) {
        LambdaQueryWrapper<MemberSignInRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberSignInRecord::getIsDeleted, false);
        lqw.eq(MemberSignInRecord::getUserId, userId);
        lqw.orderByDesc(MemberSignInRecord::getId);
        lqw.last("LIMIT 1");
        return signInRecordMapper.selectOne(lqw);
    }

    private Long selectCountByUserId(Long userId) {
        LambdaQueryWrapper<MemberSignInRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberSignInRecord::getIsDeleted, false);
        lqw.eq(MemberSignInRecord::getUserId, userId);
        return signInRecordMapper.selectCount(lqw);
    }

    private MemberSignInConfigVo getMatchedConfig(Integer day) {
        MemberSignInConfigBo bo = new MemberSignInConfigBo();
        bo.setStatus(0);
        return signInConfigService.queryList(bo).stream()
                .filter(item -> item.getDay() != null && item.getDay() <= day)
                .max(Comparator.comparing(MemberSignInConfigVo::getDay))
                .orElse(null);
    }

    private boolean isToday(LocalDateTime time) {
        return time != null && LocalDate.now().equals(time.toLocalDate());
    }

    private boolean isYesterday(LocalDateTime time) {
        return time != null && LocalDate.now().minusDays(1).equals(time.toLocalDate());
    }

}
