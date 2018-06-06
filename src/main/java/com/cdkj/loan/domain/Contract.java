package com.cdkj.loan.domain;

import java.util.Date;

import com.cdkj.loan.dao.base.ABaseDO;

/**
* 合同管理
* @author: jiafr 
* @since: 2018-06-05 20:10:57
* @history:
*/
public class Contract extends ABaseDO {

    private static final long serialVersionUID = 1L;

    // 编号
    private String code;

    // 合同类型
    private String type;

    // 档案编号
    private String archiveCode;

    // 合同编号
    private String contractNo;

    // 开始日期
    private Date startDatetime;

    // 结束日期
    private Date endDatetime;

    // 合同附件
    private String pdf;

    // 说明
    private String remark;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPdf() {
        return pdf;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}