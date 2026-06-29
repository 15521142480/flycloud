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
import com.fly.mall.api.promotion.domain.DiscountActivity;
import com.fly.mall.api.promotion.domain.bo.DiscountActivityBo;
import com.fly.mall.api.promotion.domain.vo.DiscountActivityVo;
import com.fly.mall.promotion.mapper.DiscountActivityMapper;
import com.fly.mall.promotion.service.IDiscountActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 限时折扣活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DiscountActivityServiceImpl extends BaseServiceImpl<DiscountActivityMapper, DiscountActivity> implements IDiscountActivityService {

    private final DiscountActivityMapper baseMapper;

    /**
     * 查询限时折扣活动详情。
     */
    @Override
    public DiscountActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询限时折扣活动。
     */
    @Override
    public PageVo<DiscountActivityVo> queryPageList(DiscountActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiscountActivity> lqw = buildQueryWrapper(bo);
        Page<DiscountActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询限时折扣活动列表。
     */
    @Override
    public List<DiscountActivityVo> queryList(DiscountActivityBo bo) {
        LambdaQueryWrapper<DiscountActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改限时折扣活动。
     */
    @Override
    public Boolean saveOrUpdate(DiscountActivityBo bo) {
        DiscountActivity entity = BeanUtil.toBean(bo, DiscountActivity.class);
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
     * 校验并批量删除限时折扣活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DiscountActivity entity = new DiscountActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建限时折扣活动查询条件。
     */
    private LambdaQueryWrapper<DiscountActivity> buildQueryWrapper(DiscountActivityBo bo) {
        LambdaQueryWrapper<DiscountActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiscountActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DiscountActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DiscountActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, DiscountActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getStartTime() != null, DiscountActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, DiscountActivity::getEndTime, bo.getEndTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), DiscountActivity::getRemark, bo.getRemark());
        return lqw;
    }

}
