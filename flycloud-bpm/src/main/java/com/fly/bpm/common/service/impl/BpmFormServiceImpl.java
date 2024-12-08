package com.fly.bpm.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.fly.bpm.api.domain.dto.BpmFormFieldRespDTO;
import com.fly.common.constant.bpm.ErrorCodeConstants;
import com.fly.common.exception.utils.ServiceExceptionUtils;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmFormBo;
import com.fly.bpm.api.domain.vo.BpmFormVo;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.common.mapper.BpmFormMapper;
import com.fly.bpm.common.service.IBpmFormService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * BPM 单定义Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmFormServiceImpl extends BaseServiceImpl<BpmFormMapper, BpmForm> implements IBpmFormService {

    private final BpmFormMapper baseMapper;

    /**
     * 查询BPM 单定义
     */
    @Override
    public BpmFormVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 获得动态表单列表
     *
     * @param ids
    */
    @Override
    public List<BpmForm> queryListByIds(Collection<Long> ids) {

        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectBatchIds(ids);
    }


    /**
     * 查询BPM 单定义列表
     */
    @Override
    public PageVo<BpmFormVo> queryPageList(BpmFormBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmForm> lqw = buildQueryWrapper(bo);
        Page<BpmFormVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询BPM 单定义列表
     */
    @Override
    public List<BpmFormVo> queryList(BpmFormBo bo) {
        LambdaQueryWrapper<BpmForm> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmForm> buildQueryWrapper(BpmFormBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BpmForm> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), BpmForm::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, BpmForm::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getConf()), BpmForm::getConf, bo.getConf());
//        lqw.eq(StringUtils.isNotBlank(bo.getFields()), BpmForm::getFields, bo.getFields());
        lqw.eq(bo.getIsDeleted() != null, BpmForm::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增BPM 单定义
     */
    @Override
    public Boolean insertByBo(BpmFormBo bo) {

        this.validateFields(bo.getFields());
        BpmForm add = BeanUtil.toBean(bo, BpmForm.class);
        add.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        add.setCreateTime(LocalDateTime.now());

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }

        return flag;
    }


    /**
     * 修改BPM 单定义
     */
    @Override
    public Boolean updateByBo(BpmFormBo bo) {

        this.validateFields(bo.getFields());
        BpmForm update = BeanUtil.toBean(bo, BpmForm.class);
        update.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        update.setUpdateTime(LocalDateTime.now());

        return baseMapper.updateById(update) > 0;
    }


    /**
     * 校验 Field，避免 field 重复
     *
     * @param fields field 数组
     */
    private void validateFields(List<String> fields) {
        
        if (true) { // TODO fly：兼容 Vue3 工作流：因为采用了新的表单设计器，所以暂时不校验
            return;
        }

        Map<String, String> fieldMap = new HashMap<>(); // key 是 vModel，value 是 label
        for (String field : fields) {
            
            BpmFormFieldRespDTO fieldDTO = JsonUtils.parseObject(field, BpmFormFieldRespDTO.class);
            Assert.notNull(fieldDTO);
            String oldLabel = fieldMap.put(fieldDTO.getVModel(), fieldDTO.getLabel());

            // 如果不存在，则直接返回
            if (oldLabel == null) {
                continue;
            }
            
            // 如果存在，则报错
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.FORM_FIELD_REPEAT, oldLabel, fieldDTO.getLabel(), fieldDTO.getVModel());
        }
    }

    /**
     * 表单是否存在
     */
    private void validateFormExists(Long id) {
        if (baseMapper.selectById(id) == null) {
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.FORM_NOT_EXISTS);
        }
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmForm entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 单定义
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {

        // 校验存在
        for (Long id : ids) {
            this.validateFormExists(id);
            baseMapper.deleteBatchIds(ids);
        }

        return true;
    }

}
