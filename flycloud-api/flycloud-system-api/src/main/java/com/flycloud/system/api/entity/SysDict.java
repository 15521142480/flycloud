
package com.flycloud.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.database.entity.BaseEntity;

/**
 * 字典表实体类
 *
 * @author pangu
 * @since 2020-07-11
 */
@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysDict对象", description = "字典表")
public class SysDict extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 父主键
	*/
	@Schema(description = "父主键")
	private Long parentId;
	/**
	* 字典码
	*/
	@Schema(description = "字典码")
	private String code;
	/**
	* 字典值
	*/
	@Schema(description = "字典值")
	private String dictKey;
	/**
	* 字典名称
	*/
	@Schema(description = "字典名称")
	private String dictValue;
	/**
	* 排序
	*/
	@Schema(description = "排序")
	private Integer sort;
	/**
	* 字典备注
	*/
	@Schema(description = "字典备注")
	private String remark;


}
