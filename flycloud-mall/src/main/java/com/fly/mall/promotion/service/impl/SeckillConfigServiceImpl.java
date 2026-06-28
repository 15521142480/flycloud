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
import com.fly.mall.api.domain.promotion.SeckillConfig;
import com.fly.mall.api.domain.promotion.bo.SeckillConfigBo;
import com.fly.mall.api.domain.promotion.vo.SeckillConfigVo;
import com.fly.mall.promotion.mapper.SeckillConfigMapper;
import com.fly.mall.promotion.service.ISeckillConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 秒杀时段配置 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class SeckillConfigServiceImpl extends BaseServiceImpl<SeckillConfigMapper, SeckillConfig> implements ISeckillConfigService {

    private final SeckillConfigMapper baseMapper;

    /**
     * 查询秒杀时段配置详情。
     */
    @Override
    public SeckillConfigVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询秒杀时段配置。
     */
    @Override
    public PageVo<SeckillConfigVo> queryPageList(SeckillConfigBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SeckillConfig> lqw = buildQueryWrapper(bo);
        Page<SeckillConfigVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询秒杀时段配置列表。
     */
    @Override
    public List<SeckillConfigVo> queryList(SeckillConfigBo bo) {
        LambdaQueryWrapper<SeckillConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改秒杀时段配置。
     */
    @Override
    public Boolean saveOrUpdate(SeckillConfigBo bo) {
        SeckillConfig entity = BeanUtil.toBean(bo, SeckillConfig.class);
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
     * 校验并批量删除秒杀时段配置。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            SeckillConfig entity = new SeckillConfig();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建秒杀时段配置查询条件。
     */
    private LambdaQueryWrapper<SeckillConfig> buildQueryWrapper(SeckillConfigBo bo) {
        LambdaQueryWrapper<SeckillConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(SeckillConfig::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, SeckillConfig::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SeckillConfig::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getStartTime()), SeckillConfig::getStartTime, bo.getStartTime());
        lqw.like(StringUtils.isNotBlank(bo.getEndTime()), SeckillConfig::getEndTime, bo.getEndTime());
        lqw.eq(bo.getStatus() != null, SeckillConfig::getStatus, bo.getStatus());
        return lqw;
    }

}
