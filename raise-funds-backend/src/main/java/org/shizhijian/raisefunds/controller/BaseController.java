package org.shizhijian.raisefunds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

public class BaseController<S extends IService<T>, T> {

	@Autowired
	private S service;
	
	public <T>  T queryById(int id) {

		T object = (T) service.getById(id);
		return object;
	}
}
