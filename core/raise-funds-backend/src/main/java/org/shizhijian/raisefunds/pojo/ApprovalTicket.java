package org.shizhijian.raisefunds.pojo;

import java.io.Serializable;
import java.util.Date;

public class ApprovalTicket implements Serializable {
    private Integer id;

    private Integer fundsDescId;

    private Integer approvalStatus;

    private Date createDate;

    private Date updateDate;

    private Integer owner;

    private static final long serialVersionUID = 1L;

}