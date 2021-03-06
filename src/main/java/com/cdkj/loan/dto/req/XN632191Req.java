package com.cdkj.loan.dto.req;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 确认抵押完成
 * @author: silver 
 * @since: 2018年6月13日 下午2:48:35 
 * @history:
 */
public class XN632191Req {

    // 预算单编号
    @NotBlank
    private String code;

    // 车牌号码
    @NotBlank
    private String carNumber;

    // 绿大本编号
    @NotBlank
    private String greenBigCode;

    // 绿大本扫描件
    @NotBlank
    private String greenBigSmj;

    // 操作人
    @NotBlank
    private String operator;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getGreenBigCode() {
        return greenBigCode;
    }

    public void setGreenBigCode(String greenBigCode) {
        this.greenBigCode = greenBigCode;
    }

    public String getGreenBigSmj() {
        return greenBigSmj;
    }

    public void setGreenBigSmj(String greenBigSmj) {
        this.greenBigSmj = greenBigSmj;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
