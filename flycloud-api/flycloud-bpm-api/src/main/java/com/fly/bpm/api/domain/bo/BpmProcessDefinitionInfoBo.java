package com.fly.bpm.api.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程定义的信息业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmProcessDefinitionInfoBo extends BaseEntity {

    /**
     * 标识-精准匹配
     */
    @Schema(description = "标识-精准匹配", example = "process1641042089407")
    private String key;


}
