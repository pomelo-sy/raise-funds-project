package com.epeins.factory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.epeins.factory.Enum.ResultCode;
import com.epeins.factory.dto.MachineTypeDTO;
import com.epeins.factory.pojo.MachineTypeInfo;
import com.epeins.factory.service.MachineTypeService;
import com.epeins.factory.util.ResultData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value="/machineType")
@Api(tags = "机型类型接口")
public class MachineTypeController {

	@Autowired
	private MachineTypeService machineTypeService;

	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-机型类型-增加或更新")
	@PostMapping("/addOrUpdate")
	public ResultData<?> addOrUpdate(@RequestBody MachineTypeDTO md){
		
		MachineTypeInfo mi = new MachineTypeInfo();
		BeanUtils.copyProperties(md, mi);
		boolean flag = machineTypeService.saveOrUpdate(mi);
		if(flag) {
			return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "插入成功", null);
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "插入失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-机型类型-查询")
	@PostMapping("/query")
	public ResultData<List<MachineTypeInfo>> query(@RequestBody MachineTypeDTO md){
		
		QueryWrapper<MachineTypeInfo> qw = new QueryWrapper<MachineTypeInfo>();
		qw.like(StringUtils.isNotEmpty(md.getTypeId()),"type_id", md.getTypeId());
		List<MachineTypeInfo> list = machineTypeService.list(qw);
		if(list != null && list.size() >0) {
			return new ResultData<List<MachineTypeInfo>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		return new ResultData<List<MachineTypeInfo>>(true, ResultCode.SUCCESS.getCode(), "没有结果", null);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "GET", value = "基础数据维护-机型类型-删除")
	@GetMapping("/delete")
	public ResultData<?> delete(@RequestParam Integer id){
		
		if(id != null) {
			boolean flag = machineTypeService.removeById(id);
			if(flag) {
				return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "删除成功", null);	
			}
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "删除失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-机型-查询所有机型名")
	@PostMapping("/queryMachineId")
	public ResultData<List<Map<String, Object>>> queryMachineId(){
		
		QueryWrapper<MachineTypeInfo> qw = new QueryWrapper<MachineTypeInfo>();
		qw.select("id","type_id");
		List<Map<String, Object>> list = machineTypeService.listMaps(qw);
		if(list != null && list.size() >0) {
			return new ResultData<List<Map<String, Object>>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		
		return new ResultData<List<Map<String, Object>>>(false, ResultCode.FAIL.getCode(), "没有结果", null);
	}
	
}
