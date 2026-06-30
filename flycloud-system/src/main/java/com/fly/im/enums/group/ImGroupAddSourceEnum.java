package com.fly.im.enums.group;

import com.fly.im.framework.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * IM 加入群聊来源枚举
 * <p>
 * 由发起方在申请 / 邀请时传入；同意后同步写入 ImGroupMemberDO 的 addSource 字段
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Getter
public enum ImGroupAddSourceEnum implements ArrayValuable<Integer> {

    SEARCH(1, "搜索"), // TODO ：SEARCH 暂未实现，原因 - 搜群入口尚未开发
    INVITE(2, "邀请"),
    QR_CODE(3, "扫码"), // TODO ：QR_CODE 暂未实现，原因 - 群二维码扫码进群入口尚未开发
    SHARE_LINK(4, "分享链接"); // TODO ：SHARE_LINK 暂未实现，原因 - 群分享链接进群入口尚未开发

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
