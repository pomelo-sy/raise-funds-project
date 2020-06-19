package com.company.project.common.utils;

public enum RaiseStatus {

    //刚提交，待审批
    init(0),
    //审批完成,可展示
    shenpi(1),
    //筹集目标达成
    finish(2),
    //到期，结束
    end(3);
    private int status;
    RaiseStatus(Integer status){
        this.status = status;
    }
    public Integer getStatus(){
        return status;
    }

}
