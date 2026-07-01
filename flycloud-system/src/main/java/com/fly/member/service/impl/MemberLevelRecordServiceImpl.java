package com.fly.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.mapper.MemberLevelRecordMapper;
import com.fly.member.service.IMemberLevelRecordService;
import com.fly.system.api.member.domain.MemberLevelRecord;
import com.fly.system.api.member.domain.bo.MemberLevelRecordBo;
import com.fly.system.api.member.domain.vo.MemberLevelRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 会员等级记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class MemberLevelRecordServiceImpl implements IMemberLevelRecordService {

    private final MemberLevelRecordMapper levelRecordMapper;

    @Override
    public PageVo<MemberLevelRecordVo> queryPageList(MemberLevelRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberLevelRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberLevelRecord::getIsDeleted, false);
        if (bo != null) {
            lqw.eq(bo.getUserId() != null, MemberLevelRecord::getUserId, bo.getUserId());
            lqw.eq(bo.getLevelId() != null, MemberLevelRecord::getLevelId, bo.getLevelId());
        }
        lqw.orderByDesc(MemberLevelRecord::getId);
        Page<MemberLevelRecordVo> page = levelRecordMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberLevelRecordVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public MemberLevelRecordVo queryById(Long id) {
        return levelRecordMapper.selectVoById(id);
    }

    @Override
    public void createLevelRecord(MemberLevelRecord levelRecord) {
        levelRecord.setIsDeleted(false);
        levelRecord.setCreateBy(String.valueOf(levelRecord.getUserId()));
        levelRecord.setCreateTime(LocalDateTime.now());
        levelRecord.setUpdateBy(String.valueOf(levelRecord.getUserId()));
        levelRecord.setUpdateTime(LocalDateTime.now());
        levelRecordMapper.insert(levelRecord);
    }

}
