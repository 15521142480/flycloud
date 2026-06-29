package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 支付渠道控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RestController
@RequestMapping("/app/pay/channel")
public class AppPayChannelController {

    /**
     * 获得指定应用启用的支付渠道编码列表。
     */
    @GetMapping("/get-enable-code-list")
    public R<List<String>> getEnableChannelCodeList(@RequestParam("appId") Long appId) {
        return R.ok(List.of("wallet", "mock"));
    }

}
