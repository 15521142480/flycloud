package com.fly.system.api.im.enums.rtc;

import com.fly.system.api.im.enums.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * IM 通话参与者角色枚举
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Getter
public enum ImRtcParticipantRoleEnum implements ArrayValuable<Integer> {

    INVITER(1, "发起人"),
    INVITEE(2, "被邀请者"),
    JOINER(3, "主动加入者"); // 仅群通话场景：旁观者点胶囊条加入已有通话

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ImRtcParticipantRoleEnum::getRole).toArray(Integer[]::new);

    /**
     * 角色
     */
    private final Integer role;
    /**
     * 名字
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
