package com.fly.system.api.im.enums.group;

import com.fly.system.api.im.enums.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * IM 加入群聊来源枚举
 * <p>
 * 由发起方在申请 / 邀请时传入；同意后同步写入 ImGroupMember 的 addSource 字段
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Getter
public enum ImGroupAddSourceEnum implements ArrayValuable<Integer> {

    SEARCH(1, "搜索"), // 预留搜群入口
    INVITE(2, "邀请"),
    QR_CODE(3, "扫码"), // 预留群二维码入口
    SHARE_LINK(4, "分享链接"); // 预留群分享链接入口

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ImGroupAddSourceEnum::getSource).toArray(Integer[]::new);

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
