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
import com.fly.mall.api.promotion.domain.BargainActivity;
import com.fly.mall.api.promotion.domain.bo.BargainActivityBo;
import com.fly.mall.api.promotion.domain.vo.BargainActivityVo;
import com.fly.mall.promotion.mapper.BargainActivityMapper;
import com.fly.mall.promotion.service.IBargainActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 砍价活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class BargainActivityServiceImpl extends BaseServiceImpl<BargainActivityMapper, BargainActivity> implements IBargainActivityService {

    private final BargainActivityMapper baseMapper;

    /**
     * 查询砍价活动详情。
     */
    @Override
    public BargainActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询砍价活动。
     */
    @Override
    public PageVo<BargainActivityVo> queryPageList(BargainActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BargainActivity> lqw = buildQueryWrapper(bo);
        Page<BargainActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询砍价活动列表。
     */
    @Override
    public List<BargainActivityVo> queryList(BargainActivityBo bo) {
        LambdaQueryWrapper<BargainActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改砍价活动。
     */
    @Override
    public Boolean saveOrUpdate(BargainActivityBo bo) {
        BargainActivity entity = BeanUtil.toBean(bo, BargainActivity.class);
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
     * 校验并批量删除砍价活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            BargainActivity entity = new BargainActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建砍价活动查询条件。
     */
    private LambdaQueryWrapper<BargainActivity> buildQueryWrapper(BargainActivityBo bo) {
        LambdaQueryWrapper<BargainActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(BargainActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BargainActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), BargainActivity::getName, bo.getName());
        lqw.eq(bo.getStartTime() != null, BargainActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, BargainActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getStatus() != null, BargainActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getSpuId() != null, BargainActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, BargainActivity::getSkuId, bo.getSkuId());
        lqw.eq(bo.getBargainFirstPrice() != null, BargainActivity::getBargainFirstPrice, bo.getBargainFirstPrice());
        lqw.eq(bo.getBargainMinPrice() != null, BargainActivity::getBargainMinPrice, bo.getBargainMinPrice());
        lqw.eq(bo.getStock() != null, BargainActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, BargainActivity::getTotalStock, bo.getTotalStock());
        lqw.eq(bo.getHelpMaxCount() != null, BargainActivity::getHelpMaxCount, bo.getHelpMaxCount());
        lqw.eq(bo.getBargainCount() != null, BargainActivity::getBargainCount, bo.getBargainCount());
        lqw.eq(bo.getTotalLimitCount() != null, BargainActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getRandomMinPrice() != null, BargainActivity::getRandomMinPrice, bo.getRandomMinPrice());
        lqw.eq(bo.getRandomMaxPrice() != null, BargainActivity::getRandomMaxPrice, bo.getRandomMaxPrice());
        return lqw;
    }

}
