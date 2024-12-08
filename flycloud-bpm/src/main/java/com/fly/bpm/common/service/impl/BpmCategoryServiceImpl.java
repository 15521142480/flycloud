package com.fly.bpm.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmCategoryBo;
import com.fly.bpm.api.domain.vo.BpmCategoryVo;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.common.mapper.BpmCategoryMapper;
import com.fly.bpm.common.service.IBpmCategoryService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * BPM 流程分类Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmCategoryServiceImpl extends BaseServiceImpl<BpmCategoryMapper, BpmCategory> implements IBpmCategoryService {

    private final BpmCategoryMapper baseMapper;

    /**
     * 查询BPM 流程分类
     */
    @Override
    public BpmCategoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 根据编码获得流程分类列表
     *
     * @param codes
    */
    @Override
    public List<BpmCategory> queryCategoryListByCode(Collection<String> codes) {

        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<BpmCategory> lqw = Wrappers.lambdaQuery();
        lqw.in(BpmCategory::getCode, codes);

        return baseMapper.selectList(lqw);
    }


    /**
     * 流程分类分页列表
     */
    @Override
    public PageVo<BpmCategoryVo> queryPageList(BpmCategoryBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmCategory> lqw = buildQueryWrapper(bo);
        Page<BpmCategoryVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 流程分类列表
     */
    @Override
    public List<BpmCategoryVo> queryList(BpmCategoryBo bo) {
        LambdaQueryWrapper<BpmCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmCategory> buildQueryWrapper(BpmCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BpmCategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), BpmCategory::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), BpmCategory::getCode, bo.getCode());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), BpmCategory::getDescription, bo.getDescription());
        lqw.eq(bo.getStatus() != null, BpmCategory::getStatus, bo.getStatus());
        lqw.eq(bo.getSort() != null, BpmCategory::getSort, bo.getSort());
        lqw.eq(bo.getIsDeleted() != null, BpmCategory::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增BPM 流程分类
     */
    @Override
    public Boolean insertByBo(BpmCategoryBo bo) {

        BpmCategory add = BeanUtil.toBean(bo, BpmCategory.class);
        validEntityBeforeSave(add);
        add.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        add.setCreateTime(LocalDateTime.now());

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改BPM 流程分类
     */
    @Override
    public Boolean updateByBo(BpmCategoryBo bo) {

        BpmCategory update = BeanUtil.toBean(bo, BpmCategory.class);
        validEntityBeforeSave(update);
        update.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        update.setCreateTime(LocalDateTime.now());

        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 流程分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
