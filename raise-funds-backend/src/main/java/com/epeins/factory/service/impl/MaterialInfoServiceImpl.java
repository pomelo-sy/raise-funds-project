package com.epeins.factory.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.MaterialInfoMapper;
import com.epeins.factory.pojo.MaterialInfo;
import com.epeins.factory.service.MaterialInfoService;

@Service
public class MaterialInfoServiceImpl extends ServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService{

}
