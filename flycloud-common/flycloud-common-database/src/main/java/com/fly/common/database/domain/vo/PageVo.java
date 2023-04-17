package com.fly.common.database.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页-vo
 *
 * @author lxs
 * @date 2023/3/22
 */
@Data
public class PageVo<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 总条目数
     */
    private Long total;

    /**
     * 结果集
     */
    private List<T> list;

}
