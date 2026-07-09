package com.fly.system.websocket.feign;

import cn.hutool.core.util.StrUtil;
import com.fly.common.domain.model.R;
import com.fly.common.websocket.core.sender.WebSocketMessageSender;
import com.fly.system.api.websocket.bo.WebSocketSendBo;
import com.fly.system.api.websocket.feign.WebSocketSenderApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统内部接口 - WebSocket 消息发送控制器。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Validated
@RestController
@RequiredArgsConstructor
public class WebSocketSenderApiController implements WebSocketSenderApi {

    private final WebSocketMessageSender webSocketMessageSender;

    /**
     * 按会话、用户或用户类型发送 WebSocket 消息。
     */
    @Override
    @PostMapping("/provider/sys/websocket/send")
    public R<Boolean> send(@Valid @RequestBody WebSocketSendBo bo) {
        if (StrUtil.isNotBlank(bo.getSessionId())) {
            webSocketMessageSender.send(bo.getSessionId(), bo.getMessageType(), bo.getMessageContent());
            return R.ok(Boolean.TRUE);
        }
        if (bo.getUserId() != null) {
            webSocketMessageSender.send(bo.getUserType(), bo.getUserId(), bo.getMessageType(), bo.getMessageContent());
            return R.ok(Boolean.TRUE);
        }
        webSocketMessageSender.send(bo.getUserType(), bo.getMessageType(), bo.getMessageContent());
        return R.ok(Boolean.TRUE);
    }

}
