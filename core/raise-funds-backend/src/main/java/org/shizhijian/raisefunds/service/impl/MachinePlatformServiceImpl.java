package org.shizhijian.raisefunds.service.impl;

import org.shizhijian.raisefunds.dao.MachinePlatformInfoMapper;
import org.shizhijian.raisefunds.pojo.MachinePlatformInfo;
import org.shizhijian.raisefunds.service.MachinePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class MachinePlatformServiceImpl extends ServiceImpl<MachinePlatformInfoMapper, MachinePlatformInfo> implements MachinePlatformService {

	@Autowired
	private MachinePlatformInfoMapper machinePlatformInfoMapper;
	
	
//	public List<MachinePlatformDTO> findAll() {
//		
//		return machinePlatformInfoMapper.findAll();
//	}
}
