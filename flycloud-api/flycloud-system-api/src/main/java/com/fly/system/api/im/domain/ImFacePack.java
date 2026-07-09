package com.fly.system.api.im.domain;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * IM 表情包 DO（运营配置的系统表情包元数据）
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_face_pack")
@KeySequence("im_face_pack_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImFacePack extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 表情包名称
     */
    private String name;
    /**
     * 表情包图标
     * <p>
     * 面板底部 tab 栏显示
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
