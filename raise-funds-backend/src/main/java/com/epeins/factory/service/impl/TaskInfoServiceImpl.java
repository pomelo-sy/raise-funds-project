package com.epeins.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epeins.factory.dao.TaskInfoMapper;
import com.epeins.factory.pojo.TaskInfo;
import com.epeins.factory.service.TaskInfoService;

@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService{

	@Autowired
	private TaskInfoMapper taskInfoMapper;

	public List<TaskInfo> findTaskInfos(Wrapper<TaskInfo> wrapper) {
		
		return taskInfoMapper.findTaskInfos(wrapper);
	}
}
