package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.SeckillConfig;
import com.fly.mall.api.promotion.domain.bo.SeckillConfigBo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillConfigRespVo;
import com.fly.mall.api.promotion.domain.vo.SeckillConfigVo;
import com.fly.mall.promotion.mapper.SeckillConfigMapper;
import com.fly.mall.promotion.service.ISeckillConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 秒杀时段配置 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
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
     * 查询启用的移动端秒杀时间段列表。
     */
    @Override
    public List<AppSeckillConfigRespVo> queryAppList() {
        SeckillConfigBo bo = new SeckillConfigBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return queryList(bo).stream()
                .map(this::convertAppConfig)
                .toList();
    }

    /**
     * 查询当前正在进行的秒杀时间段。
     */
    @Override
    public SeckillConfigVo queryCurrentConfig() {
        LocalTime now = LocalTime.now();
        return queryList(new SeckillConfigBo()).stream()
                .filter(config -> StatusEnum.isEnable(config.getStatus()))
                .filter(config -> isCurrentTime(config.getStartTime(), config.getEndTime(), now))
                .findFirst()
                .orElse(null);
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
        lqw.orderByAsc(SeckillConfig::getStartTime);
        return lqw;
    }

    /**
     * 转换移动端秒杀时间段返回对象。
     */
    private AppSeckillConfigRespVo convertAppConfig(SeckillConfigVo config) {
        if (config == null) {
            return null;
        }
        AppSeckillConfigRespVo respVo = new AppSeckillConfigRespVo();
        respVo.setId(config.getId());
        respVo.setStartTime(config.getStartTime());
        respVo.setEndTime(config.getEndTime());
        respVo.setSliderPicUrls(config.getSliderPicUrls());
        return respVo;
    }

    /**
     * 判断当前时间是否落在配置时段内。
     */
    private boolean isCurrentTime(String startTime, String endTime, LocalTime now) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) || now == null) {
            return false;
        }
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        if (Objects.equals(start, end)) {
            return true;
        }
        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        }
        return !now.isBefore(start) || !now.isAfter(end);
    }

}
