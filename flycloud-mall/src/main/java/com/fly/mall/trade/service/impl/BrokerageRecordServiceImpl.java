package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.BrokerageRecord;
import com.fly.mall.api.trade.domain.bo.BrokerageRecordBo;
import com.fly.mall.api.trade.domain.vo.BrokerageRecordVo;
import com.fly.mall.trade.mapper.BrokerageRecordMapper;
import com.fly.mall.trade.service.IBrokerageRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 佣金记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class BrokerageRecordServiceImpl extends BaseServiceImpl<BrokerageRecordMapper, BrokerageRecord> implements IBrokerageRecordService {

    private final BrokerageRecordMapper baseMapper;

    /**
     * 查询佣金记录详情。
     */
    @Override
    public BrokerageRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询佣金记录。
     */
    @Override
    public PageVo<BrokerageRecordVo> queryPageList(BrokerageRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BrokerageRecord> lqw = buildQueryWrapper(bo);
        Page<BrokerageRecordVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询佣金记录列表。
     */
    @Override
    public List<BrokerageRecordVo> queryList(BrokerageRecordBo bo) {
        LambdaQueryWrapper<BrokerageRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改佣金记录。
     */
    @Override
    public Boolean saveOrUpdate(BrokerageRecordBo bo) {
        BrokerageRecord entity = BeanUtil.toBean(bo, BrokerageRecord.class);
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
     * 校验并批量删除佣金记录。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建佣金记录查询条件。
     */
    private LambdaQueryWrapper<BrokerageRecord> buildQueryWrapper(BrokerageRecordBo bo) {
        LambdaQueryWrapper<BrokerageRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(BrokerageRecord::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BrokerageRecord::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, BrokerageRecord::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getBizId()), BrokerageRecord::getBizId, bo.getBizId());
        lqw.eq(bo.getBizType() != null, BrokerageRecord::getBizType, bo.getBizType());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), BrokerageRecord::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getDescription()), BrokerageRecord::getDescription, bo.getDescription());
        lqw.eq(bo.getPrice() != null, BrokerageRecord::getPrice, bo.getPrice());
        lqw.eq(bo.getTotalPrice() != null, BrokerageRecord::getTotalPrice, bo.getTotalPrice());
        lqw.eq(bo.getStatus() != null, BrokerageRecord::getStatus, bo.getStatus());
        lqw.eq(bo.getFrozenDays() != null, BrokerageRecord::getFrozenDays, bo.getFrozenDays());
        lqw.eq(bo.getUnfreezeTime() != null, BrokerageRecord::getUnfreezeTime, bo.getUnfreezeTime());
        lqw.eq(bo.getSourceUserLevel() != null, BrokerageRecord::getSourceUserLevel, bo.getSourceUserLevel());
        lqw.eq(bo.getSourceUserId() != null, BrokerageRecord::getSourceUserId, bo.getSourceUserId());
        return lqw;
    }

}
