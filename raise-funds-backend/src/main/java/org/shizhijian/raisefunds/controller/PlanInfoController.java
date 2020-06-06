//package com.epeins.factory.controller;
//
//import java.util.List;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.epeins.factory.Enum.ResultCode;
//import com.epeins.factory.dao.PlanInfoMapper;
//import com.epeins.factory.dto.PlanInfoDTO;
//import com.epeins.factory.dto.TaskInfoDTO;
//import com.epeins.factory.pojo.PlanInfo;
//import com.epeins.factory.pojo.TaskInfo;
//import com.epeins.factory.service.planInfoService;
//import com.epeins.factory.util.PageRequest;
//import com.epeins.factory.util.Pagination;
//import com.epeins.factory.util.ResultData;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//
//@RestController
//@RequestMapping("/planInfo")
//@Api(tags = "生产计划接口")
//public class PlanInfoController {
//
//	@Autowired
//	private PlanInfoService planInfoService;
//
//	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
//	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
//	@ApiOperation(httpMethod = "POST", value = "基础数据维护-生产计划-增加或更新")
//	@PostMapping("/addOrUpdate")
//	public ResultData<?> addOrUpdate(@RequestBody TaskInfoDTO tid){
//		
//		TaskInfo ti = new TaskInfo();
//		BeanUtils.copyProperties(tid, ti);
//		boolean flag = planInfoService.saveOrUpdate(ti);
//		if(flag) {
//			return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "插入成功", null);
//		}
//		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "插入失败", null); 
//	}
//	
//	
//	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
//	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
//	@ApiOperation(httpMethod = "POST", value = "基础数据维护-生产计划-查询")
//	@PostMapping("/query")
//	public ResultData<List<TaskInfo>> query(@RequestBody TaskInfoDTO tid){
//		
//		QueryWrapper<TaskInfo> qw = new QueryWrapper<TaskInfo>();
//		qw.like(StringUtils.isNotEmpty(tid.getTaskNo()), "task_no", tid.getTaskNo())
//			.eq(tid.getTaskCount() != null, "task_count", tid.getTaskCount())
//			.eq(tid.getProdId() != null, "prod_id", tid.getProdId());
//		List<TaskInfo> list = planInfoService.findTaskInfos(qw);
//		if(list != null && list.size() >0) {
//			return new ResultData<List<TaskInfo>>(true, ResultCode.SUCCESS.getCode(), "正确返回", list);
//		}
//		
//		return new ResultData<List<TaskInfo>>(true, ResultCode.SUCCESS.getCode(), "没有结果", null);
//	}
//	
//	
//	@ApiResponses(value = { @ApiResponse(code = 500, message = "系统错误"),
//	@ApiResponse(code = 200, message = "success 成功,其它为错误") })
//	@ApiOperation(httpMethod = "GET", value = "基础数据维护-生产计划-删除")
//	@GetMapping("/delete")
//	public ResultData<?> delete(@RequestParam Integer id){
//		
//		if(id != null) {
//			boolean flag = planInfoService.removeById(id);
//			if(flag) {
//				return new ResultData<Object>(true, ResultCode.SUCCESS.getCode(), "删除成功", null);	
//			}
//		}
//		return new ResultData<Object>(false, ResultCode.FAIL.getCode(), "删除失败", null); 
//	}
//}
