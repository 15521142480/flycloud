package com.fly.bpm.task.controller;

import com.fly.bpm.api.domain.vo.activity.BpmActivityRespVO;
import com.fly.bpm.flowable.convert.BpmActivityConvert;
import com.fly.bpm.task.service.BpmActivityService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BPM 流程活动实例
 *
 * @author fly
 * @date 2024-11-24
 */
@Tag(name = "管理后台 - 流程活动实例")
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class BpmActivityController {

    private final BpmActivityService activityService;


    /**
     * todo 获得指定流程实例的活动实例列表
     *
     * 注意: 生成指定流程实例的高亮流程图; 只高亮进行中的任务。不过要注意，该接口暂时没用，通过前端的 ProcessViewer.vue 界面的 highlightDiagram 方法生成
     *
     */
    @GetMapping("/list/{processInstanceId}")
    public R<List<BpmActivityRespVO>> getActivityList(@PathVariable String processInstanceId) {
        return R.ok(BpmActivityConvert.INSTANCE.convertList(activityService.getActivityListByProcessInstanceId(processInstanceId)));
    }
}
