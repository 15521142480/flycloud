package com.fly.system.api.im.domain;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * IM 表情包项 DO（系统表情包内的单张表情图）
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_face_pack_item")
@KeySequence("im_face_pack_item_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImFacePackItem extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 所属表情包编号
     */
    private Long packId;
    /**
     * 表情图 URL
     */
    private String url;
    /**
     * 表情名（可选；如「狗头」「捂脸」）
     */
    private String name;
    /**
     * 渲染宽度（像素）
     */
    private Integer width;
    /**
     * 渲染高度（像素）
     */
    private Integer height;
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
