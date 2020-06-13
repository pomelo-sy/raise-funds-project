package org.shizhijian.raisefunds.controller;

import java.util.List;

import org.shizhijian.raisefunds.Enum.ResultCode;
import org.shizhijian.raisefunds.dto.MachinePlatformDTO;
import org.shizhijian.raisefunds.pojo.MachinePlatformInfo;
import org.shizhijian.raisefunds.pojo.MachineTypeInfo;
import org.shizhijian.raisefunds.service.MachinePlatformService;
import org.shizhijian.raisefunds.service.MachineTypeService;
import org.shizhijian.raisefunds.util.ResultData;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value="/machinePlatform")
@Api(tags = "机台接口")
public class machinePlatformController {

	@Autowired
	private MachinePlatformService machinePlatformService;
	
	@Autowired
	private MachineTypeService machineTypeService;

	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-机台-增加或更新")
	@PostMapping("/addOrUpdate")
	public ResultData<?> addOrUpdate(@RequestBody MachinePlatformDTO md){
		
		MachinePlatformInfo mi = new MachinePlatformInfo();
		BeanUtils.copyProperties(md, mi);
		boolean flag = machinePlatformService.saveOrUpdate(mi);
		if(flag) {
			return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "插入成功", null);
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "插入失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-机台-查询")
	@PostMapping("/query")
	public ResultData<List<MachinePlatformInfo>> query(@RequestBody MachinePlatformDTO mp){
		
		QueryWrapper<MachinePlatformInfo> qw = new QueryWrapper<MachinePlatformInfo>();
		qw.like(StringUtils.isNotEmpty(mp.getPlatId()),"plat_id", mp.getPlatId())
			.like(StringUtils.isNotEmpty(mp.getPlatDisp()), "plat_disp", mp.getPlatDisp())
			.like(mp.getPlatNo()!=null, "plat_no", mp.getPlatNo());
		List<MachinePlatformInfo> list = machinePlatformService.list(qw);
		
		for(MachinePlatformInfo mpi: list) {
			if(mp.getTypeId() != null) {
				QueryWrapper<MachineTypeInfo> qw2 = new QueryWrapper<MachineTypeInfo>();
				qw2.eq("id", mp.getTypeId());
				MachineTypeInfo mi = machineTypeService.getOne(qw2);
				if(mi != null) {
					mpi.setTypeName(mi.getTypeId());
				}else {
					list.remove(mpi);
				}
			}
		}
		if(list != null && list.size() >0) {
			return new ResultData<List<MachinePlatformInfo>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		return new ResultData<List<MachinePlatformInfo>>(true, ResultCode.SUCCESS.getCode(), "没有结果", null);
	}
	
	
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "GET", value = "基础数据维护-机台-删除")
	@GetMapping("/delete")
	public ResultData<?> delete(@RequestParam Integer id){
		
		if(id != null) {
			boolean flag = machinePlatformService.removeById(id);
			if(flag) {
				return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "删除成功", null);	
			}
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "删除失败", null); 
	}
	
}
