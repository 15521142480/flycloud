package com.fly.im.service.websocket.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 语音消息内容
 */
@Data
@Accessors(chain = true)
public class AudioMessage {

    /**
     * 语音文件 URL
     */
    private String url;
    /**
     * 语音时长（秒）
     */
    private Integer duration;
    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 引用消息（可选）
     */
    private QuoteMessage quote;

}
