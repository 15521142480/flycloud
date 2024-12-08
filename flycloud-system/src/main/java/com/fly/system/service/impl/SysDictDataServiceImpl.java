package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysDictDataBo;
import com.fly.system.api.domain.vo.SysDictDataVo;
import com.fly.system.api.domain.SysDictData;
import com.fly.system.mapper.SysDictDataMapper;
import com.fly.system.service.ISysDictDataService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 字典数据Service业务层处理
 *
 * @author fly
 * @date 2024-11-23
 */
@RequiredArgsConstructor
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    private final SysDictDataMapper baseMapper;

    /**
     * 查询字典数据
     */
    @Override
    public SysDictDataVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询字典数据列表
     */
    @Override
    public PageVo<SysDictDataVo> queryPageList(SysDictDataBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysDictData> lqw = buildQueryWrapper(bo);
        Page<SysDictDataVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询字典数据列表
     */
    @Override
    public List<SysDictDataVo> queryList(SysDictDataBo bo) {

        LambdaQueryWrapper<SysDictData> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getDictType()), SysDictData::getDictType, bo.getDictType());
        lqw.eq(bo.getStatus() != null, SysDictData::getStatus, bo.getStatus());
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDictData> buildQueryWrapper(SysDictDataBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysDictData> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSort() != null, SysDictData::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getLabel()), SysDictData::getLabel, bo.getLabel());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), SysDictData::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDictType()), SysDictData::getDictType, bo.getDictType());
        lqw.eq(bo.getStatus() != null, SysDictData::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getColorType()), SysDictData::getColorType, bo.getColorType());
        lqw.eq(StringUtils.isNotBlank(bo.getCssClass()), SysDictData::getCssClass, bo.getCssClass());
        lqw.eq(bo.getIsDeleted() != null, SysDictData::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增字典数据
     */
    @Override
    public Boolean insertByBo(SysDictDataBo bo) {
        SysDictData add = BeanUtil.toBean(bo, SysDictData.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改字典数据
     */
    @Override
    public Boolean updateByBo(SysDictDataBo bo) {
        SysDictData update = BeanUtil.toBean(bo, SysDictData.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysDictData entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除字典数据
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
