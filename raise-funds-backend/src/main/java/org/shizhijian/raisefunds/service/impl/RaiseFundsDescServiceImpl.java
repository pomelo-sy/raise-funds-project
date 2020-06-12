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
    public List<RaiseFundsDesc> findByUserId(Integer userId) {
        QueryWrapper<RaiseFundsDesc> opEqual = new QueryWrapper<>();
        opEqual.eq("sponsor_user_id", userId);
        List<RaiseFundsDesc> raises = list(opEqual);
        return raises;
    }
}
