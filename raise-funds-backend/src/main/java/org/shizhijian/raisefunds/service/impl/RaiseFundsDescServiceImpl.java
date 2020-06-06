package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.RaiseFundsDescMapper;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.springframework.stereotype.Service;

@Service("raiseFundsDescService")
public class RaiseFundsDescServiceImpl extends ServiceImpl<RaiseFundsDescMapper, RaiseFundsDesc> implements RaiseFundsDescService {
}
