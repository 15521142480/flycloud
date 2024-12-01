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
import com.fly.system.api.domain.bo.SysDictBo;
import com.fly.system.api.domain.vo.SysDictVo;
import com.fly.system.api.domain.SysDict;
import com.fly.system.mapper.SysDictMapper;
import com.fly.system.service.ISysDictService;

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
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    private final SysDictMapper baseMapper;

    /**
     * 查询字典数据
     */
    @Override
    public SysDictVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询字典数据列表
     */
    @Override
    public PageVo<SysDictVo> queryPageList(SysDictBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysDict> lqw = buildQueryWrapper(bo);
        Page<SysDictVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询字典数据列表
     */
    @Override
    public List<SysDictVo> queryList(SysDictBo bo) {

        LambdaQueryWrapper<SysDict> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getDictType()), SysDict::getDictType, bo.getDictType());
        lqw.eq(bo.getStatus() != null, SysDict::getStatus, bo.getStatus());
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDict> buildQueryWrapper(SysDictBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysDict> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSort() != null, SysDict::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getLabel()), SysDict::getLabel, bo.getLabel());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), SysDict::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDictType()), SysDict::getDictType, bo.getDictType());
        lqw.eq(bo.getStatus() != null, SysDict::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getColorType()), SysDict::getColorType, bo.getColorType());
        lqw.eq(StringUtils.isNotBlank(bo.getCssClass()), SysDict::getCssClass, bo.getCssClass());
        lqw.eq(bo.getIsDeleted() != null, SysDict::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增字典数据
     */
    @Override
    public Boolean insertByBo(SysDictBo bo) {
        SysDict add = BeanUtil.toBean(bo, SysDict.class);
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
    public Boolean updateByBo(SysDictBo bo) {
        SysDict update = BeanUtil.toBean(bo, SysDict.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysDict entity){
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
