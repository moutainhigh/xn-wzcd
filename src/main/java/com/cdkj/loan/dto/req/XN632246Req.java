package com.cdkj.loan.dto.req;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 返点详情查询
 * @author: jiafr 
 * @since: 2018年6月18日 下午3:32:58 
 * @history:
 */
public class XN632246Req {

    // 返点编号
    @NotBlank
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
