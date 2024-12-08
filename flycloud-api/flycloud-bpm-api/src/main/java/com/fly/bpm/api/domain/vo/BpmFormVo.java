package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BPM 单定义视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmFormVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 表单名
     */
    @ExcelProperty(value = "表单名")
    private String name;

    /**
     * 开启状态
     */
    @ExcelProperty(value = "开启状态")
    private Integer status;

    /**
     * 表单的配置
     */
    @ExcelProperty(value = "表单的配置")
    private String conf;

    /**
     * 表单项的数组
     */
//    @ExcelProperty(value = "表单项的数组")
    private List<String> fields;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    private String createBy;

    private LocalDateTime createTime;


}
