package com.fly.im.controller.admin.statistics;

import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.im.framework.util.LocalDateTimeUtils;
import com.fly.system.api.im.domain.vo.admin.manager.statistics.*;
import com.fly.im.service.statistics.ImStatisticsManagerService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - IM 数据看板")
@RestController
@RequestMapping({"/im/manager/statistics", "/admin/im/manager/statistics"})
@Validated
public class ImStatisticsManagerController {

    /**
     * 群规模分桶名称的展示顺序
     */
    private static final List<String> GROUP_SIZE_BUCKETS = Arrays.asList("1-9 人", "10-49 人", "50-199 人", "200+ 人");
    /**
     * 看板分布默认时间窗口（天）
     */
    private static final int DISTRIBUTION_WINDOW_DAYS = 30;
    /**
     * TOP 发送者数量
     */
    private static final int TOP_SENDER_LIMIT = 10;

    @Resource
    private ImStatisticsManagerService statisticsService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/overview")
    @Operation(summary = "获得数据概览")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<ImStatisticsManagerOverviewRespVo> getOverview() {
        LocalDateTime todayBegin = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrowBegin = todayBegin.plusDays(1);
        LocalDateTime yesterdayBegin = todayBegin.minusDays(1);
        // 周活/月活定义为 N 天滚动窗口（含今天）
        LocalDateTime weekBegin = todayBegin.minusDays(6);
        LocalDateTime monthBegin = todayBegin.minusDays(29);
        return ok(new ImStatisticsManagerOverviewRespVo()
                .setTotalUser(statisticsService.getTotalUserCount())
                .setNewUserToday(statisticsService.getNewUserCount(todayBegin, tomorrowBegin))
                .setTotalGroup(statisticsService.getTotalGroupCount())
                .setNewGroupToday(statisticsService.getNewGroupCount(todayBegin, tomorrowBegin))
                .setActiveUserDaily(statisticsService.getActiveUserCount(todayBegin, tomorrowBegin))
                .setActiveUserWeekly(statisticsService.getActiveUserCount(weekBegin, tomorrowBegin))
                .setActiveUserMonthly(statisticsService.getActiveUserCount(monthBegin, tomorrowBegin))
                .setPrivateMessageToday(statisticsService.getPrivateMessageCount(todayBegin, tomorrowBegin))
                .setGroupMessageToday(statisticsService.getGroupMessageCount(todayBegin, tomorrowBegin))
                .setPrivateMessageYesterday(statisticsService.getPrivateMessageCount(yesterdayBegin, todayBegin))
                .setGroupMessageYesterday(statisticsService.getGroupMessageCount(yesterdayBegin, todayBegin)));
    }

    @GetMapping("/message-trend")
    @Operation(summary = "获得消息趋势（私聊 / 群聊双线）")
    @Parameter(name = "days", description = "回看天数（含今日）", example = "7")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<ImStatisticsManagerTrendRespVo> getMessageTrend(
            @RequestParam(value = "days", defaultValue = "7") @Min(1) @Max(90) int days) {
        List<LocalDateTime> dates = LocalDateTimeUtils.getLatestDays(days);
        LocalDateTime beginTime = dates.get(0);
        LocalDateTime endTime = dates.get(days - 1).plusDays(1);
        Map<LocalDateTime, Long> privateMap = statisticsService.getPrivateMessageDailyCountMap(beginTime, endTime);
        Map<LocalDateTime, Long> groupMap = statisticsService.getGroupMessageDailyCountMap(beginTime, endTime);
        // 转换格式
        Map<String, List<Long>> series = new LinkedHashMap<>();
        series.put("private", alignSeries(dates, privateMap));
        series.put("group", alignSeries(dates, groupMap));
        return ok(new ImStatisticsManagerTrendRespVo().setDates(dates).setSeries(series));
    }

    @GetMapping("/user-trend")
    @Operation(summary = "获得用户趋势（新增注册 / 日活双线）")
    @Parameter(name = "days", description = "回看天数（含今日）", example = "7")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<ImStatisticsManagerTrendRespVo> getUserTrend(
            @RequestParam(value = "days", defaultValue = "7") @Min(1) @Max(90) int days) {
        List<LocalDateTime> dates = LocalDateTimeUtils.getLatestDays(days);
        LocalDateTime beginTime = dates.get(0);
        LocalDateTime endTime = dates.get(days - 1).plusDays(1);
        Map<LocalDateTime, Long> registerMap = statisticsService.getNewUserDailyCountMap(beginTime, endTime);
        Map<LocalDateTime, Long> activeMap = statisticsService.getActiveUserDailyCountMap(beginTime, endTime);
        // 转换格式
        Map<String, List<Long>> series = new LinkedHashMap<>();
        series.put("register", alignSeries(dates, registerMap));
        series.put("active", alignSeries(dates, activeMap));
        return ok(new ImStatisticsManagerTrendRespVo().setDates(dates).setSeries(series));
    }

    @GetMapping("/message-type-distribution")
    @Operation(summary = "获得消息类型分布（最近 30 天）")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<List<ImStatisticsManagerMessageTypeRespVo>> getMessageTypeDistribution() {
        LocalDateTime endTime = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime beginTime = endTime.minusDays(DISTRIBUTION_WINDOW_DAYS);
        Map<Integer, Long> typeCountMap = statisticsService.getMessageTypeCountMap(beginTime, endTime);
        // 转换格式
        return ok(convertList(typeCountMap.entrySet(), entry -> new ImStatisticsManagerMessageTypeRespVo()
                .setType(entry.getKey()).setValue(entry.getValue())));
    }

    @GetMapping("/group-size-distribution")
    @Operation(summary = "获得群规模分布")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<List<ImStatisticsManagerGroupSizeRespVo>> getGroupSizeDistribution() {
        Map<String, Long> groupSizeMap = statisticsService.getGroupSizeCountMap();
        // 转换格式
        return ok(convertList(GROUP_SIZE_BUCKETS, bucket -> new ImStatisticsManagerGroupSizeRespVo()
                .setRange(bucket).setCount(groupSizeMap.getOrDefault(bucket, 0L))));
    }

    @GetMapping("/top-senders")
    @Operation(summary = "获得消息 TOP 发送者（最近 30 天）")
    @PreAuthorize("@pms.hasPermission('im:manager:statistics:list')")
    public R<List<ImStatisticsManagerTopSenderRespVo>> getTopSenders() {
        LocalDateTime endTime = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime beginTime = endTime.minusDays(DISTRIBUTION_WINDOW_DAYS);
        Map<Long, Long> topSenderMap = statisticsService.getTopSenderCountMap(beginTime, endTime, TOP_SENDER_LIMIT);
        // TOP 发送者：批量回填昵称
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(topSenderMap.keySet());
        return ok(convertList(topSenderMap.entrySet(), entry -> {
            ImStatisticsManagerTopSenderRespVo item = new ImStatisticsManagerTopSenderRespVo()
                    .setUserId(entry.getKey()).setMessageCount(entry.getValue());
            MapUtils.findAndThen(userMap, entry.getKey(), user -> item.setNickname(user.getName()));
            return item;
        }));
    }

    /**
     * 把每日聚合 Map 对齐到 dates 序列；缺失天补 0
     */
    private static List<Long> alignSeries(List<LocalDateTime> dates, Map<LocalDateTime, Long> dailyMap) {
        return convertList(dates, date -> dailyMap.getOrDefault(date, 0L));
    }

}
