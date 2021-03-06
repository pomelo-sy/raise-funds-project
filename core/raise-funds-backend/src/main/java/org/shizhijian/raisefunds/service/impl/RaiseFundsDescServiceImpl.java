package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.RaiseFundsDescMapper;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("raiseFundsDescService")
public class RaiseFundsDescServiceImpl extends ServiceImpl<RaiseFundsDescMapper, RaiseFundsDesc> implements RaiseFundsDescService {


    @Override
    public List<RaiseFundsDesc> findByOpenId(String openId) {
        return this.findByOpenIdAndStatus(openId, null);
    }

    @Override
    public List<RaiseFundsDesc> findByOpenIdAndStatus(String openId, Integer status) {
        QueryWrapper<RaiseFundsDesc> opEqual = new QueryWrapper<>();
        opEqual.eq("open_id", openId);
        if(status != null){
            opEqual.eq("status", status);
        }
        List<RaiseFundsDesc> raises = list(opEqual);
        return raises;
    }
}
