package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysDeptBo;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.SysDept;
import com.fly.system.mapper.SysDeptMapper;
import com.fly.system.service.ISysDeptService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 部门Service业务层处理
 *
 * @author fly
 * @date 2024-12-01
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    private final SysDeptMapper baseMapper;

    /**
     * 查询部门
     */
    @Override
    public SysDeptVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询部门列表
     */
    @Override
    public PageVo<SysDeptVo> queryPageList(SysDeptBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
        Page<SysDeptVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询部门列表
     */
    @Override
    public List<SysDeptVo> queryList(SysDeptBo bo) {
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDeptBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), SysDept::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysDept::getName, bo.getName());
        lqw.eq(bo.getParentId() != null, SysDept::getParentId, bo.getParentId());
        lqw.eq(bo.getSort() != null, SysDept::getSort, bo.getSort());
        lqw.eq(bo.getLeaderUserId() != null, SysDept::getLeaderUserId, bo.getLeaderUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), SysDept::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), SysDept::getEmail, bo.getEmail());
        lqw.eq(bo.getStatus() != null, SysDept::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDeleted() != null, SysDept::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增部门
     */
    @Override
    public Boolean insertByBo(SysDeptBo bo) {
        SysDept add = BeanUtil.toBean(bo, SysDept.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改部门
     */
    @Override
    public Boolean updateByBo(SysDeptBo bo) {
        SysDept update = BeanUtil.toBean(bo, SysDept.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysDept entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除部门
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
