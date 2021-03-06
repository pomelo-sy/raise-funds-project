package com.epeins;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.shizhijian.raisefunds.RaiseFundsApplication;
import org.shizhijian.raisefunds.dao.TaskInfoMapper;
import org.shizhijian.raisefunds.pojo.TaskInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RaiseFundsApplication.class})
public class TestTaskInfo {

	@Autowired
	private TaskInfoMapper taskInfoMapper;
	
	@Test
	public void testTask() {
		
		QueryWrapper<TaskInfo> qw = new QueryWrapper<TaskInfo>();
		qw.eq("task_no", "002");
		List<TaskInfo> list = taskInfoMapper.findTaskInfos(qw);
		list.forEach(System.out::println);
	}
}
