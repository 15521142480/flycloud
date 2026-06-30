package com.fly.im.service.websocket.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置消息内容
 */
@Data
@Accessors(chain = true)
public class LocationMessage {

    /**
     * 位置描述
     */
    private String description;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

}
