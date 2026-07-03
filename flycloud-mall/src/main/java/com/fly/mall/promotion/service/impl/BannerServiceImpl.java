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
import com.fly.mall.api.promotion.domain.Banner;
import com.fly.mall.api.promotion.domain.bo.BannerBo;
import com.fly.mall.api.promotion.domain.vo.BannerVo;
import com.fly.mall.promotion.mapper.BannerMapper;
import com.fly.mall.promotion.service.IBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 轮播图 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class BannerServiceImpl extends BaseServiceImpl<BannerMapper, Banner> implements IBannerService {

    private final BannerMapper baseMapper;

    /**
     * 查询轮播图详情。
     */
    @Override
    public BannerVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询轮播图。
     */
    @Override
    public PageVo<BannerVo> queryPageList(BannerBo bo, PageBo pageBo) {
        LambdaQueryWrapper<Banner> lqw = buildQueryWrapper(bo);
        Page<BannerVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询轮播图列表。
     */
    @Override
    public List<BannerVo> queryList(BannerBo bo) {
        LambdaQueryWrapper<Banner> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(Banner::getSort).orderByDesc(Banner::getId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据位置查询轮播图列表。
     */
    @Override
    public List<BannerVo> queryListByPosition(Integer position) {
        BannerBo bo = new BannerBo();
        bo.setPosition(position);
        return queryList(bo);
    }

    /**
     * 增加轮播图浏览次数。
     */
    @Override
    public Boolean addBrowseCount(Long id) {
        if (id == null || baseMapper.selectById(id) == null) {
            return false;
        }
        baseMapper.updateBrowseCount(id);
        return true;
    }

    /**
     * 新增或修改轮播图。
     */
    @Override
    public Boolean saveOrUpdate(BannerBo bo) {
        Banner entity = BeanUtil.toBean(bo, Banner.class);
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
     * 校验并批量删除轮播图。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            Banner entity = new Banner();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建轮播图查询条件。
     */
    private LambdaQueryWrapper<Banner> buildQueryWrapper(BannerBo bo) {
        LambdaQueryWrapper<Banner> lqw = Wrappers.lambdaQuery();
        lqw.eq(Banner::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, Banner::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), Banner::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getUrl()), Banner::getUrl, bo.getUrl());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), Banner::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getSort() != null, Banner::getSort, bo.getSort());
        lqw.eq(bo.getStatus() != null, Banner::getStatus, bo.getStatus());
        lqw.eq(bo.getPosition() != null, Banner::getPosition, bo.getPosition());
        lqw.like(StringUtils.isNotBlank(bo.getMemo()), Banner::getMemo, bo.getMemo());
        lqw.eq(bo.getBrowseCount() != null, Banner::getBrowseCount, bo.getBrowseCount());
        return lqw;
    }

}
