package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysDictTypeBo;
import com.fly.system.api.domain.vo.SysDictTypeVo;
import com.fly.system.api.domain.SysDictType;
import com.fly.system.mapper.SysDictTypeMapper;
import com.fly.system.service.ISysDictTypeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 字典类型 业务层
 *
 * @author fly
 * @date 2024-12-08
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final SysDictTypeMapper baseMapper;

    /**
     * 查询字典类型
     */
    @Override
    public SysDictTypeVo queryById(Long id) {

        return baseMapper.selectVoById(id);
    }


    /**
     * 查询字典类型分页列表
     */
    @Override
    public PageVo<SysDictTypeVo> queryPageList(SysDictTypeBo bo, PageBo pageBo) {

        LambdaQueryWrapper<SysDictType> lqw = buildQueryWrapper(bo);
        Page<SysDictTypeVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询字典类型列表
     */
    @Override
    public List<SysDictTypeVo> queryList(SysDictTypeBo bo) {

        LambdaQueryWrapper<SysDictType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDictType> buildQueryWrapper(SysDictTypeBo bo) {

        LambdaQueryWrapper<SysDictType> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysDictType::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), SysDictType::getType, bo.getType());
        lqw.eq(bo.getStatus() != null, SysDictType::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDeleted() != null, SysDictType::getIsDeleted, bo.getIsDeleted());
        lqw.eq(bo.getDeletedTime() != null, SysDictType::getDeletedTime, bo.getDeletedTime());

        return lqw;
    }


    /**
     * 新增字典类型
     */
    @Override
    public Boolean insertByBo(SysDictTypeBo bo) {

        SysDictType add = BeanUtil.toBean(bo, SysDictType.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改字典类型
     */
    @Override
    public Boolean updateByBo(SysDictTypeBo bo) {

        SysDictType update = BeanUtil.toBean(bo, SysDictType.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysDictType entity) {

        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除字典类型
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {

        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
