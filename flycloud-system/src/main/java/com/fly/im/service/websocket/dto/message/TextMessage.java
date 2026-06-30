package com.fly.im.service.websocket.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文本消息内容
 */
@Data
@Accessors(chain = true)
public class TextMessage {

    /**
     * 文本内容
     */
    private String content;

    /**
     * 引用消息（可选）
     */
    private QuoteMessage quote;

}
