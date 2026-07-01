package com.fly.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.mapper.MemberPointRecordMapper;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.MemberPointRecord;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.bo.MemberPointRecordBo;
import com.fly.system.api.member.domain.vo.MemberPointRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 会员积分记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class MemberPointRecordServiceImpl implements IMemberPointRecordService {

    private final MemberPointRecordMapper pointRecordMapper;
    private final IMemberUserService memberUserService;

    @Override
    public PageVo<MemberPointRecordVo> queryPageList(MemberPointRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberPointRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberPointRecord::getIsDeleted, false);
        if (bo != null) {
            lqw.eq(bo.getUserId() != null, MemberPointRecord::getUserId, bo.getUserId());
            lqw.eq(bo.getBizType() != null, MemberPointRecord::getBizType, bo.getBizType());
        }
        lqw.orderByDesc(MemberPointRecord::getId);
        Page<MemberPointRecordVo> page = pointRecordMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberPointRecordVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPointRecord(Long userId, Integer point, Integer bizType, String bizId) {
        if (point == null || point == 0) {
            return;
        }
        MemberUser user = memberUserService.getUser(userId);
        int totalPoint = (user.getPoint() == null ? 0 : user.getPoint()) + point;
        memberUserService.updatePoint(userId, point);
        MemberPointRecord record = new MemberPointRecord();
        record.setUserId(userId);
        record.setBizType(bizType);
        record.setBizId(bizId);
        record.setTitle(point > 0 ? "获得积分" : "扣减积分");
        record.setDescription("会员积分变动：" + point);
        record.setPoint(point);
        record.setTotalPoint(totalPoint);
        record.setIsDeleted(false);
        record.setCreateBy(String.valueOf(userId));
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateBy(String.valueOf(userId));
        record.setUpdateTime(LocalDateTime.now());
        pointRecordMapper.insert(record);
    }

}
