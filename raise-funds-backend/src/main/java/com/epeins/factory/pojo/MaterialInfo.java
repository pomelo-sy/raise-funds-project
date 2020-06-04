package com.epeins.factory.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("mt_material_info")
public class MaterialInfo implements Serializable {
	
	@TableId(type=IdType.AUTO)
    private Integer id;

    private String materialName;

    private String materialDisp;

    private String materialColor;

    private String memo;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    
}