package com.fly.bpm.common.service.impl;

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
import com.fly.bpm.api.domain.bo.BpmProcessDefinitionInfoBo;
import com.fly.bpm.api.domain.vo.BpmProcessDefinitionInfoVo;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.common.mapper.BpmProcessDefinitionInfoMapper;
import com.fly.bpm.common.service.IBpmProcessDefinitionInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * BPM 流程定义的信息Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmProcessDefinitionInfoServiceImpl extends BaseServiceImpl<BpmProcessDefinitionInfoMapper, BpmProcessDefinitionInfo> implements IBpmProcessDefinitionInfoService {

    private final BpmProcessDefinitionInfoMapper baseMapper;

    /**
     * 查询BPM 流程定义的信息
     */
    @Override
    public BpmProcessDefinitionInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询BPM 流程定义的信息列表
     */
    @Override
    public PageVo<BpmProcessDefinitionInfoVo> queryPageList(BpmProcessDefinitionInfoBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmProcessDefinitionInfo> lqw = buildQueryWrapper(bo);
        Page<BpmProcessDefinitionInfoVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询BPM 流程定义的信息列表
     */
    @Override
    public List<BpmProcessDefinitionInfoVo> queryList(BpmProcessDefinitionInfoBo bo) {
        LambdaQueryWrapper<BpmProcessDefinitionInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmProcessDefinitionInfo> buildQueryWrapper(BpmProcessDefinitionInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BpmProcessDefinitionInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProcessDefinitionId()), BpmProcessDefinitionInfo::getProcessDefinitionId, bo.getProcessDefinitionId());
        lqw.eq(StringUtils.isNotBlank(bo.getModelId()), BpmProcessDefinitionInfo::getModelId, bo.getModelId());
        lqw.eq(bo.getModelType() != null, BpmProcessDefinitionInfo::getModelType, bo.getModelType());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), BpmProcessDefinitionInfo::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), BpmProcessDefinitionInfo::getDescription, bo.getDescription());
        lqw.eq(bo.getFormType() != null, BpmProcessDefinitionInfo::getFormType, bo.getFormType());
        lqw.eq(bo.getFormId() != null, BpmProcessDefinitionInfo::getFormId, bo.getFormId());
        lqw.eq(StringUtils.isNotBlank(bo.getFormConf()), BpmProcessDefinitionInfo::getFormConf, bo.getFormConf());
        lqw.eq(StringUtils.isNotBlank(bo.getFormFields()), BpmProcessDefinitionInfo::getFormFields, bo.getFormFields());
        lqw.eq(StringUtils.isNotBlank(bo.getFormCustomCreatePath()), BpmProcessDefinitionInfo::getFormCustomCreatePath, bo.getFormCustomCreatePath());
        lqw.eq(StringUtils.isNotBlank(bo.getFormCustomViewPath()), BpmProcessDefinitionInfo::getFormCustomViewPath, bo.getFormCustomViewPath());
        lqw.eq(StringUtils.isNotBlank(bo.getSimpleModel()), BpmProcessDefinitionInfo::getSimpleModel, bo.getSimpleModel());
        lqw.eq(bo.getVisible() != null, BpmProcessDefinitionInfo::getVisible, bo.getVisible());
        lqw.eq(StringUtils.isNotBlank(bo.getStartUserIds()), BpmProcessDefinitionInfo::getStartUserIds, bo.getStartUserIds());
        lqw.eq(StringUtils.isNotBlank(bo.getManagerUserIds()), BpmProcessDefinitionInfo::getManagerUserIds, bo.getManagerUserIds());
        lqw.eq(bo.getIsDeleted() != null, BpmProcessDefinitionInfo::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增BPM 流程定义的信息
     */
    @Override
    public Boolean insertByBo(BpmProcessDefinitionInfoBo bo) {
        BpmProcessDefinitionInfo add = BeanUtil.toBean(bo, BpmProcessDefinitionInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改BPM 流程定义的信息
     */
    @Override
    public Boolean updateByBo(BpmProcessDefinitionInfoBo bo) {
        BpmProcessDefinitionInfo update = BeanUtil.toBean(bo, BpmProcessDefinitionInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmProcessDefinitionInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 流程定义的信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
