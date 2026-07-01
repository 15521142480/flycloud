package com.fly.mall.api.statistics.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据对照响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataComparisonRespVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前数据。
     */
    private T value;

    /**
     * 参照数据。
     */
    private T reference;

}
