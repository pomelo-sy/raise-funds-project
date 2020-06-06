package org.shizhijian.raisefunds.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.shizhijian.raisefunds.pojo.TaskInfo;

public interface TaskInfoService extends IService<TaskInfo>{

	
	public List<TaskInfo> findTaskInfos(Wrapper<TaskInfo> wrapper);
}
