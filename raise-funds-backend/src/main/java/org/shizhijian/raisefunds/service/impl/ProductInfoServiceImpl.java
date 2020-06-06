package org.shizhijian.raisefunds.service.impl;

import org.shizhijian.raisefunds.dao.ProductInfoMapper;
import org.shizhijian.raisefunds.pojo.ProductInfo;
import org.shizhijian.raisefunds.service.ProductInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

}
