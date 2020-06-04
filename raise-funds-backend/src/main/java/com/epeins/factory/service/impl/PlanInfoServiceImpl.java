package com.epeins.factory.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.PlanInfoMapper;
import com.epeins.factory.pojo.PlanInfo;
import com.epeins.factory.service.PlanInfoService;

@Service
public class PlanInfoServiceImpl extends ServiceImpl<PlanInfoMapper, PlanInfo> implements PlanInfoService{

}
