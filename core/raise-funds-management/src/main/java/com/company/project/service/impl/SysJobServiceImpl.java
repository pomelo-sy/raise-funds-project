package com.company.project.service.impl;

import com.company.project.common.exception.BusinessException;
import com.company.project.common.job.utils.ScheduleUtils;
import com.company.project.common.utils.Constant;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.SysJobMapper;
import com.company.project.entity.SysJobEntity;
import com.company.project.service.SysJobService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysJobService")
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJobEntity> implements SysJobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    SysJobMapper sysJobMapper;
    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init(){
        List<SysJobEntity> scheduleJobList = this.list();
        for(SysJobEntity scheduleJob : scheduleJobList){
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    @Override
    public void saveJob(SysJobEntity sysJob) {
        sysJob.setCreateTime(new Date());
        sysJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
        this.save(sysJob);

        ScheduleUtils.createScheduleJob(scheduler, sysJob);
    }

    @Override
    public void updateJobById(SysJobEntity sysJob) {
        SysJobEntity sysJobEntity = this.getById(sysJob.getId());
        if (sysJobEntity == null) {
            throw new BusinessException("获取定时任务异常");
        }
        sysJob.setStatus(sysJobEntity.getStatus());
        ScheduleUtils.updateScheduleJob(scheduler, sysJob);

        this.updateById(sysJob);
    }

    @Override
    public void delete(List<String> ids) {
        for(String jobId : ids){
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
        sysJobMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(List<String> ids) {
        for(String jobId : ids){
            ScheduleUtils.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(List<String> ids) {
        for(String jobId : ids){
            ScheduleUtils.pauseJob(scheduler, jobId);
        }

        updateBatch(ids, Constant.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(List<String> ids) {
        for(String jobId : ids){
            ScheduleUtils.resumeJob(scheduler, jobId);
        }

        updateBatch(ids, Constant.ScheduleStatus.NORMAL.getValue());
    }

    @Override
    public int updateBatch(List<String> ids, int status){
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", ids);
        map.put("status", status);
        return baseMapper.updateBatch(map);
    }
}