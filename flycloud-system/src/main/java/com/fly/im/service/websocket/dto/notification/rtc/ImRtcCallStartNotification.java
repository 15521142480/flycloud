package com.fly.im.service.websocket.dto.notification.rtc;

import com.fly.system.api.im.domain.rtc.ImRtcCall;
import com.fly.system.api.system.domain.vo.SysUserVo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RTC_CALL_START 通话开始通知
 * <p>
 * 群聊入 im_group_message 全群广播；前端渲染聊天 tip「{inviterNickname} 发起了语音通话」
 * <p>
 * 私聊入 im_private_message 定向给被叫；前端不渲染聊天 tip，仅用于会话列表预览展示「[语音通话]」（刷新后仍可见）
 * <p>
 * 与 {@link ImRtcCallEndNotification} 两段式配对：
 * START 在 invite 接口事务里 INSERT，END 在 cancel / leave 接口事务里 INSERT，
 * 两段位于不同请求 / 事务，自增 id 保证聊天流顺序；后续如果合并到同一事务里 push，需要额外保证 START 先于 END
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Accessors(chain = true)
public class ImRtcCallStartNotification {

    /**
     * 业务通话编号
     */
    private String room;
    /**
     * 会话类型：当前固定 GROUP（私聊无 START）
     */
    private Integer conversationType;
    /**
     * 媒体类型
     */
    private Integer mediaType;
    /**
     * 发起人用户编号
     */
    private Long inviterUserId;
    /**
     * 发起人昵称：用于聊天 tip 文案，可空走前端 fallback
     */
    private String inviterNickname;
    /**
     * 发起人头像：可空，预留给点击 tip 展示发起人卡片
     */
    private String inviterAvatar;

    public static ImRtcCallStartNotification of(ImRtcCall call, SysUserVo inviter) {
        ImRtcCallStartNotification notification = new ImRtcCallStartNotification();
        notification.room = call.getRoom();
        notification.conversationType = call.getConversationType();
        notification.mediaType = call.getMediaType();
        notification.inviterUserId = call.getInviterUserId();
        if (inviter != null) {
            notification.inviterNickname = inviter.getName();
            notification.inviterAvatar = inviter.getAvatar();
        }
        return notification;
    }

}
