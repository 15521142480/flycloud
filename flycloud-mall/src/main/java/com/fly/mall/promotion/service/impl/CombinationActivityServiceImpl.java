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
import com.fly.mall.api.domain.promotion.CombinationActivity;
import com.fly.mall.api.domain.promotion.bo.CombinationActivityBo;
import com.fly.mall.api.domain.promotion.vo.CombinationActivityVo;
import com.fly.mall.promotion.mapper.CombinationActivityMapper;
import com.fly.mall.promotion.service.ICombinationActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 拼团活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class CombinationActivityServiceImpl extends BaseServiceImpl<CombinationActivityMapper, CombinationActivity> implements ICombinationActivityService {

    private final CombinationActivityMapper baseMapper;

    /**
     * 查询拼团活动详情。
     */
    @Override
    public CombinationActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询拼团活动。
     */
    @Override
    public PageVo<CombinationActivityVo> queryPageList(CombinationActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<CombinationActivity> lqw = buildQueryWrapper(bo);
        Page<CombinationActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询拼团活动列表。
     */
    @Override
    public List<CombinationActivityVo> queryList(CombinationActivityBo bo) {
        LambdaQueryWrapper<CombinationActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改拼团活动。
     */
    @Override
    public Boolean saveOrUpdate(CombinationActivityBo bo) {
        CombinationActivity entity = BeanUtil.toBean(bo, CombinationActivity.class);
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
     * 校验并批量删除拼团活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            CombinationActivity entity = new CombinationActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建拼团活动查询条件。
     */
    private LambdaQueryWrapper<CombinationActivity> buildQueryWrapper(CombinationActivityBo bo) {
        LambdaQueryWrapper<CombinationActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(CombinationActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, CombinationActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), CombinationActivity::getName, bo.getName());
        lqw.eq(bo.getSpuId() != null, CombinationActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getTotalLimitCount() != null, CombinationActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getSingleLimitCount() != null, CombinationActivity::getSingleLimitCount, bo.getSingleLimitCount());
        lqw.eq(bo.getStartTime() != null, CombinationActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, CombinationActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getUserSize() != null, CombinationActivity::getUserSize, bo.getUserSize());
        lqw.eq(bo.getVirtualGroup() != null, CombinationActivity::getVirtualGroup, bo.getVirtualGroup());
        lqw.eq(bo.getStatus() != null, CombinationActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getLimitDuration() != null, CombinationActivity::getLimitDuration, bo.getLimitDuration());
        return lqw;
    }

}
