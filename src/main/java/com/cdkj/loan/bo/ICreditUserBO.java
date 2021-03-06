package com.cdkj.loan.bo;

import java.util.List;

import com.cdkj.loan.bo.base.IPaginableBO;
import com.cdkj.loan.domain.CreditUser;
import com.cdkj.loan.enums.ELoanRole;

public interface ICreditUserBO extends IPaginableBO<CreditUser> {

    // 新增征信人员
    public void saveCreditUser(CreditUser creditUser);

    // 删除征信人员
    public void dropCreditUser(String code);

    // 查询征信人员
    public CreditUser getCreditUser(String code);

    public CreditUser getCreditUserNotNull(String code);

    // 修改征信人员信息
    public void refreshCreditUser(CreditUser creditUser);

    // 修改征信人员是否一审
    public void refreshCreditUserIsFirstAudit(CreditUser creditUser);

    // 录入银行征信结果
    public void inputBankCreditResult(CreditUser creditUser);

    // 修改征信人员信息收入
    public void refreshCreditUserIncome(CreditUser creditUser);

    // 批量查询征信人员
    public List<CreditUser> queryCreditUserList(CreditUser condition);

    // 查询征信单共还人，担保人，本人
    public CreditUser getCreditUserByCreditCode(String creditCode,
            ELoanRole loanRole);

    // 查询征信单共还人，担保人，本人通过是否一审
    public CreditUser getCreditUserByIsFirstAudit(String creditCode,
            ELoanRole loanRole);

    // 查询征信单担保人
    public List<CreditUser> getCreditUserListByCreditCode(String creditCode,
            ELoanRole loanRole);

    // 查询征信单担保人
    public List<CreditUser> getCreditUserListByIsFirstAudit(String creditCode,
            ELoanRole loanRole);

    // 法院网查询结果录入
    public void refreshCourtNetworkResults(String code,
            String courtNetworkResults, String courtNetworkResultsRemark);

    public CreditUser getCreditUserByMobile(String mobile);

}
