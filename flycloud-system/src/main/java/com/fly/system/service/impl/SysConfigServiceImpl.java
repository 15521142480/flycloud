package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.utils.StringUtils;
import com.fly.system.domain.SysConfig;
import com.fly.system.domain.bo.SysConfigBo;
import com.fly.system.domain.vo.SysConfigVo;
import com.fly.system.mapper.SysConfigMapper;
import com.fly.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 参数配置Service业务层处理
 *
 * @author fly
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    private final SysConfigMapper baseMapper;

    /**
     * 查询参数配置
     */
    @Override
    public SysConfigVo queryById(Long configId){
        return baseMapper.selectVoById(configId);
    }


    /**
     * 查询参数配置列表
     */
    @Override
    public PageVo<SysConfigVo> queryPageList(SysConfigBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(bo);
        Page<SysConfigVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询参数配置列表
     */
    @Override
    public List<SysConfigVo> queryList(SysConfigBo bo) {
        LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysConfig> buildQueryWrapper(SysConfigBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getConfigName()), SysConfig::getConfigName, bo.getConfigName());
        lqw.eq(StringUtils.isNotBlank(bo.getConfigKey()), SysConfig::getConfigKey, bo.getConfigKey());
        lqw.eq(StringUtils.isNotBlank(bo.getConfigValue()), SysConfig::getConfigValue, bo.getConfigValue());
        lqw.eq(StringUtils.isNotBlank(bo.getConfigType()), SysConfig::getConfigType, bo.getConfigType());
        return lqw;
    }


    /**
     * 新增参数配置
     */
    @Override
    public Boolean insertByBo(SysConfigBo bo) {
        SysConfig add = BeanUtil.toBean(bo, SysConfig.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setConfigId(add.getConfigId());
        }
        return flag;
    }


    /**
     * 修改参数配置
     */
    @Override
    public Boolean updateByBo(SysConfigBo bo) {
        SysConfig update = BeanUtil.toBean(bo, SysConfig.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysConfig entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除参数配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
