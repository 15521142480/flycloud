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
import com.fly.mall.api.promotion.domain.PointActivity;
import com.fly.mall.api.promotion.domain.bo.PointActivityBo;
import com.fly.mall.api.promotion.domain.vo.PointActivityVo;
import com.fly.mall.promotion.mapper.PointActivityMapper;
import com.fly.mall.promotion.service.IPointActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 积分商城活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PointActivityServiceImpl extends BaseServiceImpl<PointActivityMapper, PointActivity> implements IPointActivityService {

    private final PointActivityMapper baseMapper;

    /**
     * 查询积分商城活动详情。
     */
    @Override
    public PointActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询积分商城活动。
     */
    @Override
    public PageVo<PointActivityVo> queryPageList(PointActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PointActivity> lqw = buildQueryWrapper(bo);
        Page<PointActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询积分商城活动列表。
     */
    @Override
    public List<PointActivityVo> queryList(PointActivityBo bo) {
        LambdaQueryWrapper<PointActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改积分商城活动。
     */
    @Override
    public Boolean saveOrUpdate(PointActivityBo bo) {
        PointActivity entity = BeanUtil.toBean(bo, PointActivity.class);
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
     * 校验并批量删除积分商城活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            PointActivity entity = new PointActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建积分商城活动查询条件。
     */
    private LambdaQueryWrapper<PointActivity> buildQueryWrapper(PointActivityBo bo) {
        LambdaQueryWrapper<PointActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(PointActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PointActivity::getId, bo.getId());
        lqw.eq(bo.getSpuId() != null, PointActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getStatus() != null, PointActivity::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), PointActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getSort() != null, PointActivity::getSort, bo.getSort());
        lqw.eq(bo.getStock() != null, PointActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, PointActivity::getTotalStock, bo.getTotalStock());
        return lqw;
    }

}
