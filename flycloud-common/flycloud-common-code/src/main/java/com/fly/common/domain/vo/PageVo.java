package com.fly.common.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
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
    private Long pages;

    /**
     * 总条目数
     */
    private Long total;

    /**
     * 结果集
     */
    private List<T> list;



    public PageVo() {
    }

    public PageVo(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public PageVo(Long total) {
        this.list = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageVo<T> empty() {
        return new PageVo<>(0L);
    }


    public static <T> PageVo<T> empty(Long total) {
        return new PageVo<>(total);
    }

}
