package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.promotion.domain.BargainRecord;
import com.fly.mall.api.promotion.domain.bo.BargainRecordBo;
import com.fly.mall.api.promotion.domain.vo.BargainRecordVo;
import com.fly.mall.promotion.mapper.BargainRecordMapper;
import com.fly.mall.promotion.service.IBargainRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 砍价记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class BargainRecordServiceImpl extends BaseServiceImpl<BargainRecordMapper, BargainRecord> implements IBargainRecordService {

    private final BargainRecordMapper baseMapper;

    /**
     * 查询砍价记录详情。
     */
    @Override
    public BargainRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询砍价记录。
     */
    @Override
    public PageVo<BargainRecordVo> queryPageList(BargainRecordBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BargainRecord> lqw = buildQueryWrapper(bo);
        Page<BargainRecordVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询砍价记录列表。
     */
    @Override
    public List<BargainRecordVo> queryList(BargainRecordBo bo) {
        LambdaQueryWrapper<BargainRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改砍价记录。
     */
    @Override
    public Boolean saveOrUpdate(BargainRecordBo bo) {
        BargainRecord entity = BeanUtil.toBean(bo, BargainRecord.class);
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
     * 校验并批量删除砍价记录。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            BargainRecord entity = new BargainRecord();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建砍价记录查询条件。
     */
    private LambdaQueryWrapper<BargainRecord> buildQueryWrapper(BargainRecordBo bo) {
        LambdaQueryWrapper<BargainRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(BargainRecord::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BargainRecord::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, BargainRecord::getUserId, bo.getUserId());
        lqw.eq(bo.getActivityId() != null, BargainRecord::getActivityId, bo.getActivityId());
        lqw.eq(bo.getSpuId() != null, BargainRecord::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, BargainRecord::getSkuId, bo.getSkuId());
        lqw.eq(bo.getBargainFirstPrice() != null, BargainRecord::getBargainFirstPrice, bo.getBargainFirstPrice());
        lqw.eq(bo.getBargainPrice() != null, BargainRecord::getBargainPrice, bo.getBargainPrice());
        lqw.eq(bo.getStatus() != null, BargainRecord::getStatus, bo.getStatus());
        lqw.eq(bo.getEndTime() != null, BargainRecord::getEndTime, bo.getEndTime());
        lqw.eq(bo.getOrderId() != null, BargainRecord::getOrderId, bo.getOrderId());
        return lqw;
    }

}
