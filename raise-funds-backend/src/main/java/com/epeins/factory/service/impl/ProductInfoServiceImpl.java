package com.epeins.factory.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.ProductInfoMapper;
import com.epeins.factory.pojo.ProductInfo;
import com.epeins.factory.service.ProductInfoService;

@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService{

}
