package com.fly.mall.statistics.controller.admin;

import com.fly.common.domain.model.R;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberAnalyseDataRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberAnalyseRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberAreaStatisticsRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberCountRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberRegisterCountRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberSexStatisticsRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.MemberTerminalStatisticsRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 管理后台 - 会员统计 控制器。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/statistics/member")
public class MemberStatisticsController {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 查询会员统计汇总。
     */
    @GetMapping("/summary")
    public R<MemberSummaryRespVo> summary() {
        MemberSummaryRespVo respVo = new MemberSummaryRespVo();
        respVo.setUserCount(queryInt("select count(1) from member_user where is_deleted = 0"));
        respVo.setRechargeUserCount(queryInt("""
                select count(distinct w.user_id)
                from pay_wallet_recharge r
                join pay_wallet w on w.id = r.wallet_id and w.is_deleted = 0
                where r.is_deleted = 0 and r.pay_status = 1
                """));
        respVo.setRechargePrice(queryInt("select coalesce(sum(pay_price), 0) from pay_wallet_recharge where is_deleted = 0 and pay_status = 1"));
        respVo.setExpensePrice(queryInt("select coalesce(sum(total_expense), 0) from pay_wallet where is_deleted = 0"));
        return R.ok(respVo);
    }

    /**
     * 查询会员分析数据。
     */
    @GetMapping("/analyse")
    public R<MemberAnalyseRespVo> analyse(StatisticsTimeRangeBo bo) {
        MemberAnalyseRespVo respVo = buildAnalyseResp(bo);
        respVo.setComparison(new DataComparisonRespVo<>(buildAnalyseData(bo), buildAnalyseData(previousRange(bo))));
        return R.ok(respVo);
    }

    /**
     * 查询会员地区统计。
     */
    @GetMapping("/area-statistics-list")
    public R<List<MemberAreaStatisticsRespVo>> areaStatisticsList() {
        return R.ok(queryList("""
                select area_id, count(1) user_count
                from member_user
                where is_deleted = 0
                group by area_id
                """, (rs, rowNum) -> {
            MemberAreaStatisticsRespVo vo = new MemberAreaStatisticsRespVo();
            vo.setAreaId(rs.getObject("area_id") == null ? null : rs.getInt("area_id"));
            vo.setAreaName(vo.getAreaId() == null ? "未知" : String.valueOf(vo.getAreaId()));
            vo.setUserCount(rs.getInt("user_count"));
            vo.setOrderCreateUserCount(0);
            vo.setOrderPayUserCount(0);
            vo.setOrderPayPrice(0);
            return vo;
        }));
    }

    /**
     * 查询会员性别统计。
     */
    @GetMapping("/sex-statistics-list")
    public R<List<MemberSexStatisticsRespVo>> sexStatisticsList() {
        return R.ok(queryList("""
                select sex, count(1) user_count
                from member_user
                where is_deleted = 0
                group by sex
                """, (rs, rowNum) -> {
            MemberSexStatisticsRespVo vo = new MemberSexStatisticsRespVo();
            vo.setSex(rs.getObject("sex") == null ? null : rs.getInt("sex"));
            vo.setUserCount(rs.getInt("user_count"));
            return vo;
        }));
    }

    /**
     * 查询会员终端统计。
     */
    @GetMapping("/terminal-statistics-list")
    public R<List<MemberTerminalStatisticsRespVo>> terminalStatisticsList() {
        return R.ok(queryList("""
                select register_terminal, count(1) user_count
                from member_user
                where is_deleted = 0
                group by register_terminal
                """, (rs, rowNum) -> {
            MemberTerminalStatisticsRespVo vo = new MemberTerminalStatisticsRespVo();
            vo.setTerminal(rs.getObject("register_terminal") == null ? null : rs.getInt("register_terminal"));
            vo.setUserCount(rs.getInt("user_count"));
            return vo;
        }));
    }

    /**
     * 查询会员数量对照。
     */
    @GetMapping("/user-count-comparison")
    public R<DataComparisonRespVo<MemberCountRespVo>> userCountComparison() {
        LocalDateTime todayBegin = LocalDate.now().atStartOfDay();
        StatisticsTimeRangeBo today = range(todayBegin, LocalDateTime.now());
        StatisticsTimeRangeBo yesterday = range(todayBegin.minusDays(1), todayBegin.minusSeconds(1));
        return R.ok(new DataComparisonRespVo<>(buildMemberCount(today), buildMemberCount(yesterday)));
    }

    /**
     * 查询会员注册数量列表。
     */
    @GetMapping("/register-count-list")
    public R<List<MemberRegisterCountRespVo>> registerCountList(StatisticsTimeRangeBo bo) {
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        String sql = """
                select date(create_time) register_date, count(1) register_count
                from member_user
                where is_deleted = 0
                """;
        Object[] args = new Object[]{};
        if (times != null && times.length > 1 && times[0] != null && times[1] != null) {
            sql += " and create_time between ? and ?";
            args = new Object[]{times[0], times[1]};
        }
        sql += " group by date(create_time) order by register_date asc";
        return R.ok(queryList(sql, (rs, rowNum) -> {
            MemberRegisterCountRespVo vo = new MemberRegisterCountRespVo();
            vo.setDate(rs.getDate("register_date").toLocalDate());
            vo.setCount(rs.getInt("register_count"));
            return vo;
        }, args));
    }

    /**
     * 构建会员分析响应对象。
     */
    private MemberAnalyseRespVo buildAnalyseResp(StatisticsTimeRangeBo bo) {
        Integer payUserCount = queryIntByRange("select count(distinct user_id) from trade_order where is_deleted = 0 and pay_status = 1", bo);
        Integer payPrice = queryIntByRange("select coalesce(sum(pay_price), 0) from trade_order where is_deleted = 0 and pay_status = 1", bo);
        MemberAnalyseRespVo respVo = new MemberAnalyseRespVo();
        respVo.setVisitUserCount(0);
        respVo.setOrderUserCount(queryIntByRange("select count(distinct user_id) from trade_order where is_deleted = 0", bo));
        respVo.setPayUserCount(payUserCount);
        respVo.setAtv(payUserCount == null || payUserCount == 0 ? 0 : payPrice / payUserCount);
        return respVo;
    }

    /**
     * 构建会员分析对照数据。
     */
    private MemberAnalyseDataRespVo buildAnalyseData(StatisticsTimeRangeBo bo) {
        MemberAnalyseDataRespVo respVo = new MemberAnalyseDataRespVo();
        respVo.setRegisterUserCount(queryIntByRange("select count(1) from member_user where is_deleted = 0", bo));
        respVo.setVisitUserCount(0);
        respVo.setRechargeUserCount(queryIntByRange("""
                select count(distinct w.user_id)
                from pay_wallet_recharge r
                join pay_wallet w on w.id = r.wallet_id and w.is_deleted = 0
                where r.is_deleted = 0 and r.pay_status = 1
                """, bo, "r.create_time"));
        return respVo;
    }

    /**
     * 构建会员数量统计。
     */
    private MemberCountRespVo buildMemberCount(StatisticsTimeRangeBo bo) {
        MemberCountRespVo respVo = new MemberCountRespVo();
        respVo.setVisitUserCount(0);
        respVo.setRegisterUserCount(queryIntByRange("select count(1) from member_user where is_deleted = 0", bo));
        return respVo;
    }

    /**
     * 按创建时间范围查询整数结果。
     */
    private Integer queryIntByRange(String baseSql, StatisticsTimeRangeBo bo) {
        return queryIntByRange(baseSql, bo, "create_time");
    }

    /**
     * 按指定时间字段范围查询整数结果。
     */
    private Integer queryIntByRange(String baseSql, StatisticsTimeRangeBo bo, String timeColumn) {
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times == null || times.length < 2 || times[0] == null || times[1] == null) {
            return queryInt(baseSql);
        }
        return queryInt(baseSql + " and " + timeColumn + " between ? and ?", times[0], times[1]);
    }

    /**
     * 查询整数结果。
     */
    private Integer queryInt(String sql, Object... args) {
        try {
            Integer value = jdbcTemplate.queryForObject(sql, Integer.class, args);
            return value == null ? 0 : value;
        } catch (DataAccessException ex) {
            return 0;
        }
    }

    /**
     * 查询列表，异常时返回空列表避免统计页影响主流程。
     */
    private <T> List<T> queryList(String sql, org.springframework.jdbc.core.RowMapper<T> rowMapper, Object... args) {
        try {
            return jdbcTemplate.query(sql, rowMapper, args);
        } catch (DataAccessException ex) {
            return Collections.emptyList();
        }
    }

    /**
     * 构建时间范围。
     */
    private StatisticsTimeRangeBo range(LocalDateTime beginTime, LocalDateTime endTime) {
        StatisticsTimeRangeBo bo = new StatisticsTimeRangeBo();
        bo.setTimes(new LocalDateTime[]{beginTime, endTime});
        return bo;
    }

    /**
     * 生成对照时间范围。
     */
    private StatisticsTimeRangeBo previousRange(StatisticsTimeRangeBo bo) {
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times == null || times.length < 2 || times[0] == null || times[1] == null) {
            return null;
        }
        long seconds = Math.max(1, Duration.between(times[0], times[1]).getSeconds());
        return range(times[0].minusSeconds(seconds), times[0].minusSeconds(1));
    }

}
