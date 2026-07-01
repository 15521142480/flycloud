package com.fly.system.api.im.enums.friend;

import com.fly.system.api.im.enums.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * IM 好友添加来源枚举
 * <p>
 * 由发起方调用 apply 接口时传入；同意后同步写入 im_friend.add_source（双向）
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Getter
public enum ImFriendAddSourceEnum implements ArrayValuable<Integer> {

    SEARCH(1, "搜索"), // FriendAddDialog 搜索流程
    GROUP(2, "群聊"), // 群成员主页 → UserInfo「加为好友」入口
    QR_CODE(3, "扫码"), // 预留扫码加好友入口
    CARD(4, "名片"); // 预留名片加好友入口

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ImFriendAddSourceEnum::getSource).toArray(Integer[]::new);

    /**
     * 来源
     */
    private final Integer source;
    /**
     * 名字
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
