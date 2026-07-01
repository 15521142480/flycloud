package com.fly.system.api.im.domain.face;

import com.fly.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * IM 用户私有表情 DO（个人表情包，对照微信「我的表情」）
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_face_user_item")
@KeySequence("im_face_user_item_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImFaceUserItem extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 所属用户编号
     * <p>
     * 关联 AdminUserDO 的 id 编号
     */
    private Long userId;
    /**
     * 表情图 URL
     */
    private String url;
    /**
     * 表情名（可选）
     * <p>
     * 用户私有表情通常不带名字，留字段以备将来「重命名」交互
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

}
