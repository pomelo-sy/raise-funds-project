package org.shizhijian.raisefunds.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="t_raise_funds_file_material")
public class RaiseFileMaterial implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private Integer descId;

    private Integer orderby;

    private Boolean isFrontPage;

    private Date createDate;

    private Boolean isDeleted;

    private String url;

    private static final long serialVersionUID = 1L;

}