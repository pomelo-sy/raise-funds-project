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
@TableName("t_user")
public class User implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;

    private String name;

    private Integer sex;

    private Date lastLogin;

    private String openId;

    private String nickName;

    private String province;

    private String city;

    private String country;

    private String headImgUrl;

    private String unionId;

    private static final long serialVersionUID = 1L;

}