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
import com.fly.mall.api.domain.promotion.BargainHelp;
import com.fly.mall.api.domain.promotion.bo.BargainHelpBo;
import com.fly.mall.api.domain.promotion.vo.BargainHelpVo;
import com.fly.mall.promotion.mapper.BargainHelpMapper;
import com.fly.mall.promotion.service.IBargainHelpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 砍价助力 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class BargainHelpServiceImpl extends BaseServiceImpl<BargainHelpMapper, BargainHelp> implements IBargainHelpService {

    private final BargainHelpMapper baseMapper;

    /**
     * 查询砍价助力详情。
     */
    @Override
    public BargainHelpVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询砍价助力。
     */
    @Override
    public PageVo<BargainHelpVo> queryPageList(BargainHelpBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BargainHelp> lqw = buildQueryWrapper(bo);
        Page<BargainHelpVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询砍价助力列表。
     */
    @Override
    public List<BargainHelpVo> queryList(BargainHelpBo bo) {
        LambdaQueryWrapper<BargainHelp> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改砍价助力。
     */
    @Override
    public Boolean saveOrUpdate(BargainHelpBo bo) {
        BargainHelp entity = BeanUtil.toBean(bo, BargainHelp.class);
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
     * 校验并批量删除砍价助力。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            BargainHelp entity = new BargainHelp();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建砍价助力查询条件。
     */
    private LambdaQueryWrapper<BargainHelp> buildQueryWrapper(BargainHelpBo bo) {
        LambdaQueryWrapper<BargainHelp> lqw = Wrappers.lambdaQuery();
        lqw.eq(BargainHelp::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BargainHelp::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, BargainHelp::getActivityId, bo.getActivityId());
        lqw.eq(bo.getRecordId() != null, BargainHelp::getRecordId, bo.getRecordId());
        lqw.eq(bo.getUserId() != null, BargainHelp::getUserId, bo.getUserId());
        lqw.eq(bo.getReducePrice() != null, BargainHelp::getReducePrice, bo.getReducePrice());
        return lqw;
    }

}
