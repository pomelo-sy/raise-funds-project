package org.shizhijian.raisefunds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;

import java.util.List;

public interface RaiseFundsDescService  extends IService<RaiseFundsDesc> {


    List<RaiseFundsDesc> findByOpenId(String openId);
}
