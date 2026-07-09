package com.fly.common.websocket.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.websocket.core.listener.WebSocketMessageListener;
import com.fly.common.websocket.core.message.JsonWebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * WebSocket JSON 消息处理器，负责心跳响应和客户端消息分发。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Slf4j
public class JsonWebSocketMessageHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketMessageListener<Object>> listeners = new HashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JsonWebSocketMessageHandler(List<? extends WebSocketMessageListener> listenersList) {
        listenersList.forEach(listener -> listeners.put(listener.getType(), listener));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        if (message.getPayloadLength() == 4 && Objects.equals(message.getPayload(), "ping")) {
            session.sendMessage(new TextMessage("pong"));
            return;
        }
        try {
            JsonWebSocketMessage jsonMessage = JsonUtils.parseObject(message.getPayload(), JsonWebSocketMessage.class);
            if (jsonMessage == null || StrUtil.isBlank(jsonMessage.getType())) {
                log.warn("[handleTextMessage][session({}) message({}) 消息格式不正确]", session.getId(), message.getPayload());
                return;
            }
            WebSocketMessageListener<Object> listener = listeners.get(jsonMessage.getType());
            if (listener == null) {
                log.warn("[handleTextMessage][session({}) type({}) 未找到监听器]", session.getId(), jsonMessage.getType());
                return;
            }
            Type type = TypeUtil.getTypeArgument(listener.getClass(), 0);
            Object messageObj = JsonUtils.getObjectMapper().readValue(jsonMessage.getContent(),
                    JsonUtils.getObjectMapper().getTypeFactory().constructType(type));
            listener.onMessage(session, messageObj);
        } catch (Exception e) {
            log.error("[handleTextMessage][session({}) message({}) 处理失败]", session.getId(), message.getPayload(), e);
        }
    }

}
