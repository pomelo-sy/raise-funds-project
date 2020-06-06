package org.shizhijian.raisefunds.service.impl;

import java.util.List;

import org.shizhijian.raisefunds.dao.TaskInfoMapper;
import org.shizhijian.raisefunds.pojo.TaskInfo;
import org.shizhijian.raisefunds.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService {

	@Autowired
	private TaskInfoMapper taskInfoMapper;

	public List<TaskInfo> findTaskInfos(Wrapper<TaskInfo> wrapper) {
		
		return taskInfoMapper.findTaskInfos(wrapper);
	}
}
