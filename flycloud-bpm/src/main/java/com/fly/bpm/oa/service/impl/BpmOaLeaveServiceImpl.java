package com.fly.bpm.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fly.bpm.api.domain.bo.BpmOALeaveCreateBo;
import com.fly.bpm.api.domain.dto.instance.BpmProcessInstanceCreateReqDTO;
import com.fly.bpm.api.feign.IBpmInstanceApi;
import com.fly.common.enums.bpm.BpmTaskStatusEnum;
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
import com.fly.bpm.api.domain.bo.BpmOaLeaveBo;
import com.fly.bpm.api.domain.vo.BpmOaLeaveVo;
import com.fly.bpm.api.domain.BpmOaLeave;
import com.fly.bpm.common.mapper.BpmOaLeaveMapper;
import com.fly.bpm.oa.service.IBpmOaLeaveService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * OA 请假申请Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmOaLeaveServiceImpl extends BaseServiceImpl<BpmOaLeaveMapper, BpmOaLeave> implements IBpmOaLeaveService {

    private final BpmOaLeaveMapper baseMapper;

    private final IBpmInstanceApi bpmInstanceApi;



    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_leave";



    /**
     * 查询OA 请假申请
     */
    @Override
    public BpmOaLeaveVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询OA 请假申请列表
     */
    @Override
    public PageVo<BpmOaLeaveVo> queryPageList(BpmOaLeaveBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmOaLeave> lqw = buildQueryWrapper(bo);
        Page<BpmOaLeaveVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询OA 请假申请列表
     */
    @Override
    public List<BpmOaLeaveVo> queryList(BpmOaLeaveBo bo) {
        LambdaQueryWrapper<BpmOaLeave> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmOaLeave> buildQueryWrapper(BpmOaLeaveBo bo) {

        Map<String, Object> params = bo.getParams();
        bo.setIsDeleted(false);

        LambdaQueryWrapper<BpmOaLeave> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, BpmOaLeave::getUserId, bo.getUserId());
        lqw.eq(bo.getType() != null, BpmOaLeave::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getReason()), BpmOaLeave::getReason, bo.getReason());
        lqw.eq(bo.getStartTime() != null, BpmOaLeave::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, BpmOaLeave::getEndTime, bo.getEndTime());
        lqw.eq(bo.getDay() != null, BpmOaLeave::getDay, bo.getDay());
        lqw.eq(bo.getStatus() != null, BpmOaLeave::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessInstanceId()), BpmOaLeave::getProcessInstanceId, bo.getProcessInstanceId());
        lqw.eq(bo.getIsDeleted() != null, BpmOaLeave::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增OA 请假申请
     */
    @Override
    public Boolean insertByBo(BpmOALeaveCreateBo bo) {

        // 1. 插入 OA 请假单
        Long curUserId = UserUtils.getCurUserId();
        BpmOaLeave add = BeanUtil.toBean(bo, BpmOaLeave.class);
        validEntityBeforeSave(add);

        long day = LocalDateTimeUtil.between(bo.getStartTime(), bo.getEndTime()).toDays();
        add.setUserId(curUserId);
        add.setDay((int) day);
        add.setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        boolean flag = baseMapper.insert(add) > 0;

        // 2. 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("day", day);
        String processInstanceId = bpmInstanceApi.createInstance(curUserId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables)
                        .setBusinessKey(String.valueOf(add.getId()))
                        .setStartUserSelectAssignees(bo.getStartUserSelectAssignees())).getCheckedData();

        // 3. 将流程实例编号更新到 OA 请假单中
        baseMapper.updateById(new BpmOaLeave().setId(add.getId()).setProcessInstanceId(processInstanceId));

        return flag;
    }


    /**
     * 修改OA 请假申请
     */
    @Override
    public Boolean updateByBo(BpmOaLeaveBo bo) {

        BpmOaLeave update = BeanUtil.toBean(bo, BpmOaLeave.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmOaLeave entity){
        //TODO 做一些数据校验,如唯一约束
    }



    /**
     * 批量删除OA 请假申请
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
