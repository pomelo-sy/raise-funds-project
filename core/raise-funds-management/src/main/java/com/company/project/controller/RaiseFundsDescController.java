package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.exception.BusinessException;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.RaiseStatus;
import com.company.project.entity.RaiseFundsDesc;
import com.company.project.entity.SysContentEntity;
import com.company.project.service.RaiseFundsDescService;
import com.company.project.service.SysContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author manager
 * @email *****@mail.com
 * @date 2020-05-26 17:00:59
 */
@Api(tags = "筹款管理")
@RestController
@RequestMapping("/raiseManagement")
public class RaiseFundsDescController {

    @Autowired
    private RaiseFundsDescService raiseFundsDescService;



    @ApiOperation(value = "新增")
    @PostMapping("/add")
    @RequiresPermissions("sysContent:add")
    public DataResult add(@RequestBody RaiseFundsDesc raiseFundsDesc) {
        raiseFundsDesc.setCreateDate(new Date());
        raiseFundsDescService.save(raiseFundsDesc);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    @RequiresPermissions("sysContent:delete")
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        raiseFundsDescService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("/update")
    @RequiresPermissions("sysContent:update")
    public DataResult update(@RequestBody RaiseFundsDesc raiseFundsDesc) {
        raiseFundsDescService.updateById(raiseFundsDesc);
        return DataResult.success();
    }

    @ApiOperation(value = "审核通过")
    @PostMapping("/approval")
    @RequiresPermissions("sysContent:update")
    public DataResult update(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        for(String id : ids){
            raiseFundsDescService.saveOrUpdate(RaiseFundsDesc.builder().id(Integer.valueOf(id)).status(RaiseStatus.shenpi.getStatus()).build());
        }
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("/listByPage")
    @RequiresPermissions("sysContent:list")
    public DataResult findListByPage(@RequestBody RaiseFundsDesc raiseFundsDesc) {
        Page page = new Page(raiseFundsDesc.getPage(), raiseFundsDesc.getLimit());
        QueryWrapper queryWrapper = new QueryWrapper();
        //查询条件示例
        if (!StringUtils.isEmpty(raiseFundsDesc.getTitle())) {
            queryWrapper.like("title", raiseFundsDesc.getTitle());
        }
        if (raiseFundsDesc.getStatus() != null) {
            queryWrapper.eq("status", raiseFundsDesc.getStatus());
        }
        IPage<RaiseFundsDesc> iPage = raiseFundsDescService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @ApiOperation(value = "富文本中图片上传")
    @RequiresPermissions(value = {"sysContent:update", "sysContent:add"}, logical = Logical.OR)
    @RequestMapping(value = "/picture/upload", method = RequestMethod.POST)
    public DataResult upload(MultipartFile file) {
        String data = null;
        try {
            //判断是否有文件且是否为图片文件
            BASE64Encoder encoder = new BASE64Encoder();
            // 通过base64来转化图片
            data = encoder.encode(file.getBytes());
        } catch (Exception e) {
            throw new BusinessException("图片上传失败");
        }
        if (!StringUtils.isEmpty(data)) {
            data = "data:image/jpeg;base64," + data;
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("src", data);
            return DataResult.success(resultMap);
        } else {
            return DataResult.fail("上传图片失败");
        }

    }

}
