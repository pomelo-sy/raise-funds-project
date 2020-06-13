package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.vo.req.PageReqVO;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 数据字典详情
 *
 * @author manager
 * @email *****@mail.com
 * @date 2020-04-30 15:13:16
 */
@Data
@TableName("sys_dict_detail")
public class SysDictDetailEntity extends PageReqVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId("id")
	private String id;

	/**
	 * 字典标签
	 */
	@TableField("label")
	private String label;

	/**
	 * 字典值
	 */
	@TableField("value")
	private String value;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 字典id
	 */
	@TableField("dict_id")
	private String dictId;

	/**
	 * 创建日期
	 */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 字典name
	 */
	@TableField(exist = false)
	private String dictName;

}
