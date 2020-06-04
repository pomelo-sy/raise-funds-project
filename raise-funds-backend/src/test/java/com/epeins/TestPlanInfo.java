package com.epeins;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epeins.factory.FactoryApplication;
import com.epeins.factory.dao.PlanInfoMapper;
import com.epeins.factory.pojo.PlanInfo;


@RunWith(value=SpringRunner.class)
@SpringBootTest(classes = {FactoryApplication.class})
public class TestPlanInfo {

	@Autowired
	private PlanInfoMapper planInfoMapper;
	
	@Test
	public void insertPlanInfo() {
		
		System.out.println("start insert...");
		int insert = planInfoMapper.insert(PlanInfo.builder().memo("taolimantianxia").build());
		int insert2 = planInfoMapper.insert(PlanInfo.builder().memo("天下为公").build());
		System.out.println("result:" + insert);
	}
	
	@Test
	public void queryPlanInfo() {
		
		System.out.println("start query...");
		List<PlanInfo> selectList = planInfoMapper.selectList(null);
		selectList.forEach(System.out::println);

	}
	
	
	@Test
	public void queryPlanInfoByWrapper() {
		
		QueryWrapper<PlanInfo> qw = new QueryWrapper<PlanInfo>();
		qw.likeRight(true, "memo", "re");
		List<PlanInfo> planList = planInfoMapper.selectList(qw);
		planList.forEach(System.out::println);
	}
	
	
	@Test
	public void queryPlanInfoByWrapper2() {
		
		QueryWrapper<PlanInfo> qw = new QueryWrapper<PlanInfo>();
		String query = "rema";
		qw.like(StringUtils.isNotEmpty(query), "memo", query);
		List<PlanInfo> planList = planInfoMapper.selectList(qw);
		planList.forEach(System.out::println);
	}
	
	@Test
	public void queryPlanInfoByWrapper3() {
		
		QueryWrapper<PlanInfo> qw = new QueryWrapper<PlanInfo>();
		String query = "rema";
		qw.select("create_time","update_time","memo").like(StringUtils.isNotEmpty(query), "memo", query);
		List<Map<String, Object>> planList = planInfoMapper.selectMaps(qw);
		planList.forEach(System.out::println);
	}
	
	@Test
	public void queryPlanInfoByWrapper4() {
		
		QueryWrapper<PlanInfo> qw = new QueryWrapper<PlanInfo>();
		String query = "";
		qw.select("create_time","update_time","memo").like(StringUtils.isNotEmpty(query), "memo", query).last("limit 1");
		List<Map<String, Object>> planList = planInfoMapper.selectMaps(qw);
		planList.forEach(System.out::println);
	}
	
	@Test
	public void queryPlanInfoByWrapper5() {
		
		QueryWrapper<PlanInfo> qw = new QueryWrapper<PlanInfo>();
		Page<PlanInfo> page = new Page<PlanInfo>(2,2, true); 
		IPage<PlanInfo> ipage = planInfoMapper.selectPage(page, null);
		//当前第几页
		System.out.println(ipage.getCurrent());
		//一共有几页
		System.out.println(ipage.getPages());
		//当前多少数据
		System.out.println(ipage.getSize());
		//一共多少数据
		System.out.println(ipage.getTotal());

		List<PlanInfo> records = ipage.getRecords();
		records.forEach(System.out::println);
	}
	
	 
}
