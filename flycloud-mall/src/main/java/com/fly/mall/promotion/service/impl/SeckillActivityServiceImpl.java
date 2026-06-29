package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.SeckillActivity;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;
import com.fly.mall.promotion.mapper.SeckillActivityMapper;
import com.fly.mall.promotion.service.ISeckillActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 秒杀活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class SeckillActivityServiceImpl extends BaseServiceImpl<SeckillActivityMapper, SeckillActivity> implements ISeckillActivityService {

    private final SeckillActivityMapper baseMapper;

    /**
     * 查询秒杀活动详情。
     */
    @Override
    public SeckillActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询秒杀活动。
     */
    @Override
    public PageVo<SeckillActivityVo> queryPageList(SeckillActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SeckillActivity> lqw = buildQueryWrapper(bo);
        Page<SeckillActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询秒杀活动列表。
     */
    @Override
    public List<SeckillActivityVo> queryList(SeckillActivityBo bo) {
        LambdaQueryWrapper<SeckillActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改秒杀活动。
     */
    @Override
    public Boolean saveOrUpdate(SeckillActivityBo bo) {
        SeckillActivity entity = BeanUtil.toBean(bo, SeckillActivity.class);
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
     * 校验并批量删除秒杀活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            SeckillActivity entity = new SeckillActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建秒杀活动查询条件。
     */
    private LambdaQueryWrapper<SeckillActivity> buildQueryWrapper(SeckillActivityBo bo) {
        LambdaQueryWrapper<SeckillActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(SeckillActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, SeckillActivity::getId, bo.getId());
        lqw.eq(bo.getSpuId() != null, SeckillActivity::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SeckillActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, SeckillActivity::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), SeckillActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getStartTime() != null, SeckillActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, SeckillActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getSort() != null, SeckillActivity::getSort, bo.getSort());
        lqw.eq(bo.getTotalLimitCount() != null, SeckillActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getSingleLimitCount() != null, SeckillActivity::getSingleLimitCount, bo.getSingleLimitCount());
        lqw.eq(bo.getStock() != null, SeckillActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, SeckillActivity::getTotalStock, bo.getTotalStock());
        return lqw;
    }

}
