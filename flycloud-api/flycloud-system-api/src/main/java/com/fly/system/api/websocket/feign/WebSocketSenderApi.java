package com.fly.system.api.websocket.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.common.utils.json.JsonUtils;
import com.fly.system.api.websocket.bo.WebSocketSendBo;
import com.fly.system.api.websocket.path.WebsocketApiPaths;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * WebSocket 消息发送远程调用接口。
 *
 * @author lxs
 * @date 2026-07-09
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "WebSocketSenderApi")
public interface WebSocketSenderApi {

    /**
     * 发送 WebSocket 消息。
     *
     * @param bo 发送参数
     * @return 操作结果
     */
    @PostMapping(WebsocketApiPaths.PROVIDER_WEBSOCKET_SEND_API)
    R<Boolean> send(@Valid @RequestBody WebSocketSendBo bo);

    /**
     * 发送对象消息给指定用户。
     */
    default R<Boolean> sendObject(Integer userType, Long userId, String messageType, Object messageContent) {
        return send(new WebSocketSendBo()
                .setUserType(userType)
                .setUserId(userId)
                .setMessageType(messageType)
                .setMessageContent(JsonUtils.toJsonString(messageContent)));
    }

    /**
     * 发送对象消息给指定用户类型下的全部在线用户。
     */
    default R<Boolean> sendObject(Integer userType, String messageType, Object messageContent) {
        return send(new WebSocketSendBo()
                .setUserType(userType)
                .setMessageType(messageType)
                .setMessageContent(JsonUtils.toJsonString(messageContent)));
    }

    /**
     * 发送对象消息给指定会话。
     */
    default R<Boolean> sendObject(String sessionId, String messageType, Object messageContent) {
        return send(new WebSocketSendBo()
                .setUserType(0)
                .setSessionId(sessionId)
                .setMessageType(messageType)
                .setMessageContent(JsonUtils.toJsonString(messageContent)));
    }

}
