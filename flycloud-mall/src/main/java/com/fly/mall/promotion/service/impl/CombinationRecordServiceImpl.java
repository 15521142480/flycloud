package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.file.FileUrlFieldConverter;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.CombinationRecord;
import com.fly.mall.api.promotion.domain.bo.CombinationRecordBo;
import com.fly.mall.api.promotion.domain.vo.CombinationRecordVo;
import com.fly.mall.promotion.mapper.CombinationRecordMapper;
import com.fly.mall.promotion.service.ICombinationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 拼团记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class CombinationRecordServiceImpl extends BaseServiceImpl<CombinationRecordMapper, CombinationRecord> implements ICombinationRecordService {

    private final CombinationRecordMapper baseMapper;
    private final FileUrlFieldConverter fileUrlFieldConverter;

    /**
     * 查询拼团记录详情。
     */
    @Override
    public CombinationRecordVo queryById(Long id) {
        return fileUrlFieldConverter.buildUrl(baseMapper.selectVoById(id), "picUrl", "avatar");
    }

    /**
     * 分页查询拼团记录。
     */
    @Override
    public PageVo<CombinationRecordVo> queryPageList(CombinationRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<CombinationRecord> lqw = buildQueryWrapper(bo);
        Page<CombinationRecordVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return fileUrlFieldConverter.buildUrlPage(this.build(result), "picUrl", "avatar");
    }

    /**
     * 查询拼团记录列表。
     */
    @Override
    public List<CombinationRecordVo> queryList(CombinationRecordBo bo) {
        LambdaQueryWrapper<CombinationRecord> lqw = buildQueryWrapper(bo);
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(lqw), "picUrl", "avatar");
    }

    /**
     * 新增或修改拼团记录。
     */
    @Override
    public Boolean saveOrUpdate(CombinationRecordBo bo) {
        fileUrlFieldConverter.toPath(bo, "picUrl", "avatar");
        CombinationRecord entity = BeanUtil.toBean(bo, CombinationRecord.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除拼团记录。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            CombinationRecord entity = new CombinationRecord();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建拼团记录查询条件。
     */
    private LambdaQueryWrapper<CombinationRecord> buildQueryWrapper(CombinationRecordBo bo) {
        LambdaQueryWrapper<CombinationRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(CombinationRecord::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, CombinationRecord::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, CombinationRecord::getActivityId, bo.getActivityId());
        lqw.eq(bo.getCombinationPrice() != null, CombinationRecord::getCombinationPrice, bo.getCombinationPrice());
        lqw.eq(bo.getSpuId() != null, CombinationRecord::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuName()), CombinationRecord::getSpuName, bo.getSpuName());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), CombinationRecord::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getSkuId() != null, CombinationRecord::getSkuId, bo.getSkuId());
        lqw.eq(bo.getCount() != null, CombinationRecord::getCount, bo.getCount());
        lqw.eq(bo.getUserId() != null, CombinationRecord::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), CombinationRecord::getNickname, bo.getNickname());
        lqw.like(StringUtils.isNotBlank(bo.getAvatar()), CombinationRecord::getAvatar, bo.getAvatar());
        lqw.eq(bo.getHeadId() != null, CombinationRecord::getHeadId, bo.getHeadId());
        lqw.eq(bo.getStatus() != null, CombinationRecord::getStatus, bo.getStatus());
        lqw.eq(bo.getOrderId() != null, CombinationRecord::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUserSize() != null, CombinationRecord::getUserSize, bo.getUserSize());
        lqw.eq(bo.getUserCount() != null, CombinationRecord::getUserCount, bo.getUserCount());
        lqw.eq(bo.getVirtualGroup() != null, CombinationRecord::getVirtualGroup, bo.getVirtualGroup());
        lqw.eq(bo.getExpireTime() != null, CombinationRecord::getExpireTime, bo.getExpireTime());
        lqw.eq(bo.getStartTime() != null, CombinationRecord::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, CombinationRecord::getEndTime, bo.getEndTime());
        return lqw;
    }

}
