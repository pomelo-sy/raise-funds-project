package org.shizhijian.raisefunds.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.shizhijian.raisefunds.pojo.TaskInfo;

public interface TaskInfoMapper extends BaseMapper<TaskInfo>{

	
	@Select("select t.*, p.prod_name as prodName FROM mt_task_info t LEFT JOIN mt_prod_info p ON t.prod_id = p.id "
			+ "${ew.customSqlSegment}")
	List<TaskInfo> findTaskInfos(@Param(Constants.WRAPPER)Wrapper<TaskInfo> wrapper);
   
}