package org.shizhijian.raisefunds.controller;

import java.util.List;
import java.util.Map;

import org.shizhijian.raisefunds.Enum.ResultCode;
import org.shizhijian.raisefunds.dto.ProductDTO;
import org.shizhijian.raisefunds.pojo.ProductInfo;
import org.shizhijian.raisefunds.service.ProductInfoService;
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
@RequestMapping(value="/productInfo")
@Api(tags = "产品信息接口")
public class ProductInfoController {

	@Autowired
	private ProductInfoService productInfoService;

	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-产品-增加或更新")
	@PostMapping("/addOrUpdate")
	public ResultData<?> addOrUpdate(@RequestBody ProductDTO prod){
		
		ProductInfo pi = new ProductInfo();
		BeanUtils.copyProperties(prod, pi);
		boolean flag = productInfoService.saveOrUpdate(pi);
		if(flag) {
			return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "插入成功", null);
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "插入失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-产品-查询")
	@PostMapping("/query")
	public ResultData<List<ProductInfo>> query(@RequestBody ProductDTO prod){
		
		QueryWrapper<ProductInfo> qw = new QueryWrapper<ProductInfo>();
		qw.like(StringUtils.isNotEmpty(prod.getProdName()),"prod_name", prod.getProdName());
		List<ProductInfo> list = productInfoService.list(qw);
		if(list != null && list.size() >0) {
			return new ResultData<List<ProductInfo>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		
		return new ResultData<List<ProductInfo>>(true, ResultCode.SUCCESS.getCode(), "没有结果", null);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "GET", value = "基础数据维护-产品-删除")
	@GetMapping("/delete")
	public ResultData<?> delete(@RequestParam Integer id){
		
		if(id != null) {
			boolean flag = productInfoService.removeById(id);
			if(flag) {
				return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "删除成功", null);	
			}
		}
		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "删除失败", null); 
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
	@ApiOperation(httpMethod = "POST", value = "基础数据维护-产品-查询所有机型名")
	@PostMapping("/queryProductId")
	public ResultData<List<Map<String, Object>>> queryProductId(){
		
		QueryWrapper<ProductInfo> qw = new QueryWrapper<ProductInfo>();
		qw.select("id","prod_name");
		List<Map<String, Object>> list = productInfoService.listMaps(qw);
		if(list != null && list.size() >0) {
			return new ResultData<List<Map<String, Object>>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
		}
		
		return new ResultData<List<Map<String, Object>>>(false, ResultCode.FAIL.getCode(), "没有结果", null);
	}
	
	
}
