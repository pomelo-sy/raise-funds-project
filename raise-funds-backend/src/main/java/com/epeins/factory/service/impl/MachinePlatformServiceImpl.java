package com.epeins.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.MachinePlatformInfoMapper;
import com.epeins.factory.dto.MachinePlatformDTO;
import com.epeins.factory.pojo.MachinePlatformInfo;
import com.epeins.factory.service.MachinePlatformService;

@Service
public class MachinePlatformServiceImpl extends ServiceImpl<MachinePlatformInfoMapper, MachinePlatformInfo> implements MachinePlatformService{

	@Autowired
	private MachinePlatformInfoMapper machinePlatformInfoMapper;
	
	
//	public List<MachinePlatformDTO> findAll() {
//		
//		return machinePlatformInfoMapper.findAll();
//	}
}
