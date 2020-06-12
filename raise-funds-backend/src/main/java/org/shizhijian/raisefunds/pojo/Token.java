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
@TableName("t_token")
public class Token implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private String appId;

    private String token;

    private String tokenType;

    private Date createTime;

    private Date updateTime;
}
