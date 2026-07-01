package com.fly.mall.statistics.controller.admin;

import com.fly.common.domain.model.R;
import com.fly.mall.api.statistics.domain.vo.PaySummaryRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理后台 - 支付统计 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/statistics/pay")
public class PayStatisticsController {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 查询支付充值金额汇总。
     */
    @GetMapping("/summary")
    public R<PaySummaryRespVo> summary() {
        PaySummaryRespVo respVo = new PaySummaryRespVo();
        respVo.setRechargePrice(queryInt("select coalesce(sum(pay_price), 0) from pay_wallet_recharge where is_deleted = 0 and pay_status = 1"));
        return R.ok(respVo);
    }

    /**
     * 查询整数结果。
     */
    private Integer queryInt(String sql) {
        try {
            Integer value = jdbcTemplate.queryForObject(sql, Integer.class);
            return value == null ? 0 : value;
        } catch (DataAccessException ex) {
            return 0;
        }
    }

}
