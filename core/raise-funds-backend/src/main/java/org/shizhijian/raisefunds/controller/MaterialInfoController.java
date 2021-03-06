package org.shizhijian.raisefunds.controller;

import java.util.List;

import org.shizhijian.raisefunds.Enum.ResultCode;
import org.shizhijian.raisefunds.dto.MaterialDTO;
import org.shizhijian.raisefunds.pojo.MaterialInfo;
import org.shizhijian.raisefunds.service.MaterialInfoService;
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
@RequestMapping(value="/MaterialInfo")
@Api(tags = "物料信息接口")
public class MaterialInfoController {

	@Autowired
	private MaterialInfoService materialInfoService;

	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-物料-增加或更新")
	@PostMapping("/addOrUpdate")
	public ResultData<?> addOrUpdate(@RequestBody MaterialDTO md){
		
		MaterialInfo pi = new MaterialInfo();
		BeanUtils.copyProperties(md, pi);
		boolean flag = materialInfoService.saveOrUpdate(pi);
		if(flag) {
			return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "插入成功", null);
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "插入失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-物料-查询")
	@PostMapping("/query")
	public ResultData<List<MaterialInfo>> query(@RequestBody MaterialDTO md){
		
		QueryWrapper<MaterialInfo> qw = new QueryWrapper<MaterialInfo>();
		qw.like(StringUtils.isNotEmpty(md.getMaterialName()),"material_name", md.getMaterialName())
			.like(StringUtils.isNotEmpty(md.getMaterialDisp()),"material_disp", md.getMaterialDisp())
			.like(StringUtils.isNotEmpty(md.getMaterialColor()),"material_color", md.getMaterialColor());
		List<MaterialInfo> list = materialInfoService.list(qw);
		if(list != null && list.size() >0) {
			return new ResultData<List<MaterialInfo>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		
		return new ResultData<List<MaterialInfo>>(true, ResultCode.SUCCESS.getCode(), "没有结果", null);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "GET", value = "基础数据维护-物料-删除")
	@GetMapping("/delete")
	public ResultData<?> delete(@RequestParam Integer id){
		
		if(id != null) {
			boolean flag = materialInfoService.removeById(id);
			if(flag) {
				return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "删除成功", null);	
			}
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "删除失败", null); 
	}
}
