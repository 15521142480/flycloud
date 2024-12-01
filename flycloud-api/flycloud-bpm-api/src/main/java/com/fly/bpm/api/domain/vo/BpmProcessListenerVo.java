package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * BPM 流程监听器视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmProcessListenerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 监听器名字
     */
    @ExcelProperty(value = "监听器名字")
    private String name;

    /**
     * 监听器类型
     */
    @ExcelProperty(value = "监听器类型")
    private String type;

    /**
     * 监听器状态
     */
    @ExcelProperty(value = "监听器状态")
    private Integer status;

    /**
     * 监听事件
     */
    @ExcelProperty(value = "监听事件")
    private String event;

    /**
     * 监听器值类型
     */
    @ExcelProperty(value = "监听器值类型")
    private String valueType;

    /**
     * 监听器值
     */
    @ExcelProperty(value = "监听器值")
    private String value;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
