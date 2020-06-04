package com.epeins.factory.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.MachineTypeInfoMapper;
import com.epeins.factory.pojo.MachineTypeInfo;
import com.epeins.factory.service.MachineTypeService;

@Service
public class MachineTypeServiceImpl extends ServiceImpl<MachineTypeInfoMapper, MachineTypeInfo> implements MachineTypeService{

}
