package com.fly.common.rocketmq.producer;

/** 生产消息异步投递回调。 */
public interface RocketMqSendCallback {

    /** Broker 成功确认消息。 */
    void onSuccess();

    /** Broker 投递失败或客户端发送异常。 */
    void onFailure(Throwable throwable);
}
