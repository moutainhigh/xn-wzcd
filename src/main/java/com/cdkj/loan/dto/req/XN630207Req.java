package com.cdkj.loan.dto.req;

/**
 * 列表查询业务团队成员
 * @author: jiafr 
 * @since: 2018年6月8日 下午7:25:10 
 * @history:
 */
public class XN630207Req extends APageReq {

    private static final long serialVersionUID = 1L;

    private String teamCode;

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

}
