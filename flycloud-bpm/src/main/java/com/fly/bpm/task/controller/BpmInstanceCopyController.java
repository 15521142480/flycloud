package com.fly.bpm.task.controller;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.api.domain.BpmProcessInstanceCopy;
import com.fly.bpm.api.domain.bo.BpmProcessInstanceCopyBo;
import com.fly.bpm.api.domain.vo.task.BpmProcessInstanceCopyRespVO;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.DateUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.utils.collection.MapUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysUserApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.task.service.IBpmInstanceCopyService;

import java.util.Map;
import java.util.stream.Stream;

/**
 * BPM 流程实例抄送控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Tag(name = "管理后台 - 流程实例抄送")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/instanceCopy")
public class BpmInstanceCopyController extends BaseController {

    private final IBpmInstanceCopyService instanceCopyService;

    private final BpmInstanceService processInstanceService;

    private final ISysUserApi sysUserProvider;



    /**
     * 查询BPM 流程实例抄送列表
     *
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('bpm:audit:todo:list')")
    public R<PageVo<BpmProcessInstanceCopyRespVO>> list(BpmProcessInstanceCopyBo bo, PageBo page) {

        PageVo<BpmProcessInstanceCopy> pageData = instanceCopyService.getProcessInstanceCopyPageByEntity(
                UserUtils.getCurUserId(),
                bo,
                page
        );
        if (CollUtil.isEmpty(pageData.getList())) {
            return R.ok(new PageVo<>(pageData.getTotal()));
        }

        // 拼接返回
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                CollectionUtils.convertSet(pageData.getList(), BpmProcessInstanceCopy::getProcessInstanceId));

        Map<Long, SysUserVo> userMap = sysUserProvider.getUserMapByIds(CollectionUtils.convertListByFlatMap(pageData.getList(),
                copy -> Stream.of(copy.getStartUserId(), Long.parseLong(copy.getCreateBy()))));

        return R.ok(BeanUtils.toBean(pageData, BpmProcessInstanceCopyRespVO.class, copyVO -> {
            MapUtils.findAndThen(userMap, Long.valueOf(copyVO.getCreateBy()), user -> copyVO.setCreateByName(user.getName()));
            MapUtils.findAndThen(userMap, copyVO.getStartUserId(), user -> copyVO.setStartUserName(user.getName()));
            MapUtils.findAndThen(processInstanceMap, copyVO.getProcessInstanceId(),
                    processInstance -> copyVO.setProcessInstanceStartTime(DateUtils.of(processInstance.getStartTime())));
        }));
    }

}
