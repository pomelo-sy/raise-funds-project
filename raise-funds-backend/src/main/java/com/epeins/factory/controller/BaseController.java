package com.epeins.factory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

public class BaseController<T, S extends IService<T>> {

	@Autowired
	private S service;
	
	public List<T> simpleQuery(T t) {
		
		QueryWrapper<T> qw = new QueryWrapper<T>();
//		BeanUtils.copyProperties(t, target);
	    List<T> list = service.list();
	    
		return list;
	}
}
