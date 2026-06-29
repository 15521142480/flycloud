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
import com.fly.mall.api.promotion.domain.RewardActivity;
import com.fly.mall.api.promotion.domain.bo.RewardActivityBo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;
import com.fly.mall.promotion.mapper.RewardActivityMapper;
import com.fly.mall.promotion.service.IRewardActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 满减送活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class RewardActivityServiceImpl extends BaseServiceImpl<RewardActivityMapper, RewardActivity> implements IRewardActivityService {

    private final RewardActivityMapper baseMapper;

    /**
     * 查询满减送活动详情。
     */
    @Override
    public RewardActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询满减送活动。
     */
    @Override
    public PageVo<RewardActivityVo> queryPageList(RewardActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<RewardActivity> lqw = buildQueryWrapper(bo);
        Page<RewardActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询满减送活动列表。
     */
    @Override
    public List<RewardActivityVo> queryList(RewardActivityBo bo) {
        LambdaQueryWrapper<RewardActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改满减送活动。
     */
    @Override
    public Boolean saveOrUpdate(RewardActivityBo bo) {
        RewardActivity entity = BeanUtil.toBean(bo, RewardActivity.class);
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
     * 校验并批量删除满减送活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            RewardActivity entity = new RewardActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建满减送活动查询条件。
     */
    private LambdaQueryWrapper<RewardActivity> buildQueryWrapper(RewardActivityBo bo) {
        LambdaQueryWrapper<RewardActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RewardActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, RewardActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), RewardActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, RewardActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getStartTime() != null, RewardActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, RewardActivity::getEndTime, bo.getEndTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), RewardActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getConditionType() != null, RewardActivity::getConditionType, bo.getConditionType());
        lqw.eq(bo.getProductScope() != null, RewardActivity::getProductScope, bo.getProductScope());
        lqw.eq(bo.getLimit() != null, RewardActivity::getLimit, bo.getLimit());
        lqw.eq(bo.getDiscountPrice() != null, RewardActivity::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getFreeDelivery() != null, RewardActivity::getFreeDelivery, bo.getFreeDelivery());
        lqw.eq(bo.getPoint() != null, RewardActivity::getPoint, bo.getPoint());
        return lqw;
    }

}
