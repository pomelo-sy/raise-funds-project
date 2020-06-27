package org.shizhijian.raisefunds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.RaiseFileMaterialMapper;
import org.shizhijian.raisefunds.dao.RaiseFundsDescMapper;
import org.shizhijian.raisefunds.pojo.RaiseFileMaterial;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.service.RaiseFileMaterialService;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("raiseFileMaterialService")
public class RaiseFileMaterialServiceImpl extends ServiceImpl<RaiseFileMaterialMapper, RaiseFileMaterial> implements RaiseFileMaterialService {


}
