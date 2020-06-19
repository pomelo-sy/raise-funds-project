package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.RaiseFundsDesc;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.mapper.RaiseFundsDescMapper;
import com.company.project.mapper.SysDictDetailMapper;
import com.company.project.service.RaiseFundsDescService;
import com.company.project.service.SysDictDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("raiseFundsDescService")
public class RaiseFundsDescServiceImpl extends ServiceImpl<RaiseFundsDescMapper, RaiseFundsDesc> implements RaiseFundsDescService {

}