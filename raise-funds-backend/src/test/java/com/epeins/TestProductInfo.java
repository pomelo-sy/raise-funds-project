package com.epeins;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epeins.factory.FactoryApplication;
import com.epeins.factory.controller.ProductInfoController;
import com.epeins.factory.dao.ProductInfoMapper;
import com.epeins.factory.dto.ProductDTO;
import com.epeins.factory.pojo.ProductInfo;
import com.epeins.factory.service.ProductInfoService;
import com.epeins.factory.util.ResultData;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FactoryApplication.class})
public class TestProductInfo {

	
	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private ProductInfoMapper productInfoMapper;
	
	@Autowired
	private ProductInfoController productInfoController;
	
	
	
	@Test
	@Transactional
	@Rollback
	public void testinsert() {
		
		productInfoService.save(ProductInfo.builder().prodName("AFL-1-U/B").memo("the first production").build());
	}
	
	@Test
	public void testinsert2() {
		
		productInfoMapper.insert(ProductInfo.builder().prodName("ZFL-1-T/B").memo("the first production").build());
	}
	
	@Test
	public void testUpdate() {
		
		productInfoService.saveOrUpdate(ProductInfo.builder().id(6).prodName("BFL-1-T/B-S/N").memo("the first production").build());
	}
	
	@Test
	public void testDelete() {
		
		boolean removeById = productInfoService.removeById(5);
	}
	
	@Test
	public void query() {
		QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<ProductInfo>();
		queryWrapper.eq("prod_name", "ZFL-1-T/B");
		List<ProductInfo> listObjs = productInfoService.list(queryWrapper);
		listObjs.forEach(System.out::println);
	}
	
	@Test
	public void query1() {
		
		ResultData<List<ProductInfo>> query = productInfoController.query(ProductDTO.builder().prodName("FL-1-T/").build());
		System.out.println(JSON.toJSONString(query));
	}
	
	@Test
	public void insert1() {
		
		ResultData<List<ProductInfo>> query = productInfoController.query(ProductDTO.builder().prodName("FL-1-T/").build());
		System.out.println(JSON.toJSONString(query));
	}
	
}
