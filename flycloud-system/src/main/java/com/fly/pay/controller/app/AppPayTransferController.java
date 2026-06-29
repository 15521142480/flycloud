package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 支付转账控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/transfer")
public class AppPayTransferController {

    /**
     * 同步转账结果。
     */
    @GetMapping("/sync")
    public R<Boolean> syncTransfer(@RequestParam Long id) {
        return R.ok(Boolean.TRUE);
    }

}
