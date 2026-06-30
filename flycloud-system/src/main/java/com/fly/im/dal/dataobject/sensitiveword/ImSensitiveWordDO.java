package com.fly.im.dal.dataobject.sensitiveword;

import com.fly.im.framework.enums.CommonStatusEnum;
import com.fly.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * IM 敏感词 DO
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("im_sensitive_word")
@KeySequence("im_sensitive_word_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImSensitiveWordDO extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 敏感词
     */
    private String word;
    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    public String getCreator() {
        return getCreateBy();
    }

    public void setCreator(String creator) {
        setCreateBy(creator);
    }

}
