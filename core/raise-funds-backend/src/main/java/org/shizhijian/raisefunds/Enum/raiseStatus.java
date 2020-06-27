package org.shizhijian.raisefunds.Enum;

public enum raiseStatus{

    //刚提交第一步
    init(0),
    //提交第二部
    step2(5),
    //提交第三部
    step3(10),
    //审批完成,可展示
    shenpi(20),
    //筹集目标达成
    finish(30),
    //到期，结束
    end(50);
    private int status;
    raiseStatus(Integer status){
        this.status = status;
    }
    public Integer getStatus(){
        return status;
    }

}
