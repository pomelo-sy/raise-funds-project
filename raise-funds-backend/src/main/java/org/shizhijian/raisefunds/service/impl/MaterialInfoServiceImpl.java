package org.shizhijian.raisefunds.service.impl;

import org.shizhijian.raisefunds.service.MaterialInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shizhijian.raisefunds.dao.MaterialInfoMapper;
import org.shizhijian.raisefunds.pojo.MaterialInfo;

@Service
public class MaterialInfoServiceImpl extends ServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService {

}
