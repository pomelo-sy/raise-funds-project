package org.shizhijian.raisefunds.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mt_task_info")
public class TaskInfo implements Serializable {
	
	@TableId(type=IdType.AUTO)
    private Integer id;

    private String taskNo;

    private Integer prodId;

    @TableField(exist=false)
    private String prodName;
    
    private Integer taskCount;

    private Date createTime;

    private Date updateTime;
    
    private static final long serialVersionUID = 1L;

}