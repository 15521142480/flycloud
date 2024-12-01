package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 部门树型视图对象
 *
 * @author fly
 * @date 2024-12-01
 */
@Data
@ExcelIgnoreUnannotated
public class SysDeptTreeVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 部门id
     */
    private Long id;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父部门id
     */
    private Long parentId;

    /**
     * 是否有子节点
     */
    private Boolean hasChildren;

    /**
     * 子节点
     */
    private List<SysDeptVo> children;



}
