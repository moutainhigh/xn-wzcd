package com.cdkj.loan.ao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdkj.loan.ao.IOverdueMenuAO;
import com.cdkj.loan.bo.IBankBO;
import com.cdkj.loan.bo.IBudgetOrderBO;
import com.cdkj.loan.bo.IOverdueMenuBO;
import com.cdkj.loan.bo.IRepayBizBO;
import com.cdkj.loan.bo.IRepayPlanBO;
import com.cdkj.loan.bo.ISYSBizLogBO;
import com.cdkj.loan.bo.IUserBO;
import com.cdkj.loan.bo.base.Paginable;
import com.cdkj.loan.common.DateUtil;
import com.cdkj.loan.core.StringValidater;
import com.cdkj.loan.domain.BudgetOrder;
import com.cdkj.loan.domain.OverdueMenu;
import com.cdkj.loan.domain.RepayBiz;
import com.cdkj.loan.domain.RepayPlan;
import com.cdkj.loan.dto.req.XN632300ReqOverdue;
import com.cdkj.loan.dto.req.XN632301Res;
import com.cdkj.loan.enums.EBizErrorCode;
import com.cdkj.loan.enums.EBizLogType;
import com.cdkj.loan.enums.ENotMateResult;
import com.cdkj.loan.enums.EOverdueMenuStatus;
import com.cdkj.loan.enums.ERepayBizNode;
import com.cdkj.loan.enums.ERepayPlanNode;
import com.cdkj.loan.exception.BizException;

@Service
public class OverdueMenuAOImpl implements IOverdueMenuAO {

    @Autowired
    private IOverdueMenuBO overdueMenuBO;

    @Autowired
    private IBankBO bankBO;

    @Autowired
    private IUserBO userBO;

    @Autowired
    private IBudgetOrderBO budgetOrderBO;

    @Autowired
    private IRepayBizBO repayBizBO;

    @Autowired
    private IRepayPlanBO repayPlanBO;

    @Autowired
    private ISYSBizLogBO sysBizLogBO;

    @Override
    @Transactional
    public void importOverdueMenu(String loanBankCode,
            List<XN632300ReqOverdue> list) {
        // OverdueMenu om = new OverdueMenu();
        // om.setImportDatetimeStart(DateUtil.getCurrentMonthFirstDay());
        // om.setImportDatetimeEnd(DateUtil.getCurrentMonthLastDay());
        // List<OverdueMenu> queryOverdueMenuList = overdueMenuBO
        // .queryOverdueMenuList(om);
        // if (CollectionUtils.isNotEmpty(queryOverdueMenuList)) {
        // throw new BizException(EBizErrorCode.DEFAULT.getCode(),
        // "逾期客户一个月只能导入一次!");
        // }
        // 遍历循环导入
        for (XN632300ReqOverdue overdue : list) {
            // 当条数据判断是否匹配，匹配条件：姓名、证件号、贷款银行、贷款金额、总期数、放款日期查询准入单
            RepayBiz condition = new RepayBiz();
            condition.setRealName(overdue.getRealName());
            condition.setIdNo(overdue.getIdNo());
            String bankName = bankBO.getBankBySubbranch(loanBankCode)
                .getBankName();
            condition.setLoanBank(loanBankCode);
            condition.setLoanAmount(StringValidater.toLong(overdue
                .getLoanAmount()));
            condition
                .setPeriods(StringValidater.toInteger(overdue.getPeriods()));
            condition.setBankFkDatetimeStart(DateUtil.getFrontDate(
                overdue.getFkDatetime(), false));
            condition.setBankFkDatetimeEnd(DateUtil.getFrontDate(
                overdue.getFkDatetime(), true));
            condition.setCurNodeCode(ERepayBizNode.TO_REPAY.getCode());
            List<RepayBiz> repayBizList = repayBizBO
                .queryRepayBizList(condition);
            OverdueMenu overdueMenu = new OverdueMenu();
            overdueMenu.setStatus(EOverdueMenuStatus.DCL.getCode());
            overdueMenu.setOverdueAmount(StringValidater.toLong(overdue
                .getOverdueAmount()));
            // 判断是否有准入单，没有，状态设置为待处理，原因是信息不匹配,
            if (CollectionUtils.isNotEmpty(repayBizList)) {
                // 匹配到一条
                if (repayBizList.size() == 1) {
                    RepayBiz repayBiz = repayBizList.get(0);
                    RepayPlan overDueRepayPlan = repayPlanBO
                        .getRepayPlanCurMonth(repayBiz.getCode());
                    if (!ERepayPlanNode.OVERDUE.getCode().equals(
                        overDueRepayPlan.getCurNodeCode())) {
                        repayBiz.setRestPeriods(repayBiz.getRestPeriods() - 1);
                        repayBizBO.refreshRestPeriods(repayBiz);
                    }
                    if (overDueRepayPlan != null
                            && overDueRepayPlan.getPeriods() == StringValidater
                                .toInteger(overdue.getPeriods())) {
                        overDueRepayPlan.setCurNodeCode(ERepayPlanNode.OVERDUE
                            .getCode());
                        // 日志
                        sysBizLogBO.saveSYSBizLog(
                            overDueRepayPlan.getRepayBizCode(),
                            EBizLogType.REPAY_PLAN, overDueRepayPlan.getCode(),
                            ERepayPlanNode.OVERDUE.getCode());

                        // 更新逾期还款信息
                        refreshRepayInfo(overdueMenu, repayBiz,
                            overDueRepayPlan);

                        overdueMenu.setStatus(EOverdueMenuStatus.YCL.getCode());
                        overdueMenu.setBudgetOrderCode(repayBiz.getRefCode());
                        overdueMenu.setRepayBizCode(overDueRepayPlan
                            .getRepayBizCode());

                        overdueMenu
                            .setRepayPlanCode(overDueRepayPlan.getCode());
                        overdueMenu.setOverdueDatetime(overDueRepayPlan
                            .getRepayDatetime());
                    }
                } else if (repayBizList.size() > 1) {// 匹配到一条以上
                    overdueMenu
                        .setNotMateResult(ENotMateResult.ONE_CARD_MORE_LOAN
                            .getCode());
                }
            } else {// 一条都没匹配上
                overdueMenu.setNotMateResult(ENotMateResult.INFO_NOT_MATE
                    .getCode());
            }
            // 最后逾期数据填充入库
            overdueMenu.setBatchDatetime(DateUtil.strToDate(
                overdue.getBatchDatetime(), DateUtil.DB_DATE_FORMAT_STRING));
            overdueMenu.setRealName(overdue.getRealName());
            overdueMenu.setIdNo(overdue.getIdNo());
            overdueMenu.setLoanAmount(StringValidater.toLong(overdue
                .getLoanAmount()));
            overdueMenu.setFkDatetime(DateUtil.strToDate(
                overdue.getFkDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
            overdueMenu.setPeriods(StringValidater.toInteger(overdue
                .getPeriods()));

            overdueMenu.setRemainAmount(StringValidater.toLong(overdue
                .getRemainAmount()));
            overdueMenu.setLoanBankCode(loanBankCode);
            overdueMenu.setLoanBankName(bankName);

            overdueMenu.setImportDatetime(new Date());
            overdueMenuBO.saveOverdueMenu(overdueMenu);
        }
    }

    /** 
     * @param overdueMenu
     * @param repayBiz
     * @param overDueRepayPlan 
     * @create: 2018年6月12日 上午6:17:54 xieyj
     * @history: 
     */
    private void refreshRepayInfo(OverdueMenu overdueMenu, RepayBiz repayBiz,
            RepayPlan overDueRepayPlan) {
        Long preOverdueAmount = 0L;// 上一期逾期金额
        Long overdueAmount = 0L;// 本期逾期金额
        if (overdueMenu.getOverdueAmount() > overDueRepayPlan.getRepayAmount()) {
            preOverdueAmount = overdueMenu.getOverdueAmount()
                    - overDueRepayPlan.getRepayAmount();
            overdueAmount = overDueRepayPlan.getRepayAmount();
        } else {
            preOverdueAmount = 0L;
            overdueAmount = overdueMenu.getOverdueAmount();
        }
        RepayPlan condition = new RepayPlan();
        condition.setRepayBizCode(repayBiz.getCode());
        condition.setCurPeriods(overDueRepayPlan.getCurPeriods() - 1);
        List<RepayPlan> list = repayPlanBO.queryRepayPlanList(condition);
        RepayPlan preRepayPlan = list.get(0);
        preRepayPlan.setOverdueAmount(preOverdueAmount);
        if (preOverdueAmount != 0) {
            preRepayPlan.setCurNodeCode(ERepayPlanNode.OVERDUE.getCode());
        }
        repayPlanBO.refreshRepayPlanOverdue(preRepayPlan);// 更新上一期还款计划逾期金额
        overDueRepayPlan.setOverdueAmount(overdueAmount);
        repayPlanBO.refreshRepayPlanOverdue(overDueRepayPlan);// 更新当月期的还款计划逾期金额

        // 更新还款业务逾期金额
        repayBiz.setRestOverdueAmount(overdueMenu.getOverdueAmount());
        repayBiz.setTotalOverdueCount(repayBiz.getTotalOverdueCount() + 1);
        repayBiz.setCurOverdueCount(repayBiz.getCurOverdueCount() + 1);
        repayBizBO.repayOverdue(repayBiz);
    }

    // 一卡多贷
    private void refreshRepayInfo(OverdueMenu overdueMenu, RepayBiz repayBiz,
            RepayPlan overDueRepayPlan, String overAmount) {
        Long preOverdueAmount = 0L;// 上一期逾期金额
        Long overdueAmount = 0L;// 本期逾期金额
        if (StringValidater.toLong(overAmount) > overDueRepayPlan
            .getRepayAmount()) {
            preOverdueAmount = StringValidater.toLong(overAmount)
                    - overDueRepayPlan.getRepayAmount();
            overdueAmount = overDueRepayPlan.getRepayAmount();
        } else {
            preOverdueAmount = 0L;
            overdueAmount = StringValidater.toLong(overAmount);
        }
        RepayPlan condition = new RepayPlan();
        condition.setRepayBizCode(repayBiz.getCode());
        condition.setCurPeriods(overDueRepayPlan.getCurPeriods() - 1);
        List<RepayPlan> list = repayPlanBO.queryRepayPlanList(condition);
        RepayPlan preRepayPlan = list.get(0);
        preRepayPlan.setOverdueAmount(preOverdueAmount);
        if (preOverdueAmount != 0) {
            preRepayPlan.setCurNodeCode(ERepayPlanNode.OVERDUE.getCode());
        }
        repayPlanBO.refreshRepayPlanOverdue(preRepayPlan);// 更新上一期还款计划逾期金额
        overDueRepayPlan.setOverdueAmount(overdueAmount);
        repayPlanBO.refreshRepayPlanOverdue(overDueRepayPlan);// 更新当月期的还款计划逾期金额

        // 更新还款业务逾期金额
        repayBiz.setRestOverdueAmount(overdueMenu.getOverdueAmount());
        repayBiz.setTotalOverdueCount(repayBiz.getTotalOverdueCount() + 1);
        repayBiz.setCurOverdueCount(repayBiz.getCurOverdueCount() + 1);
        repayBizBO.repayOverdue(repayBiz);
    }

    // 处理逻辑
    // 1、前提条件判断
    // 2、逾期名单状态更改为已处理
    // 3、还款计划更改为已逾期
    @Override
    @Transactional
    public void handleOverdueMenu(String code, String repayBizCode,
            List<XN632301Res> repaybizList, String operator) {
        OverdueMenu overdueMenu = overdueMenuBO.getOverdueMenu(code);
        if (EOverdueMenuStatus.YCL.getCode().equals(overdueMenu.getStatus())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(), "当前逾期名单已处理");
        }
        if (ENotMateResult.INFO_NOT_MATE.getCode().equals(
            overdueMenu.getNotMateResult())) {
            RepayBiz repayBiz = repayBizBO.getRepayBiz(repayBizCode);
            RepayPlan overDueRepayPlan = repayPlanBO
                .getRepayPlanCurMonth(repayBiz.getCode());
            // 还款计划状态是否更新
            overDueRepayPlan.setCurNodeCode(ERepayPlanNode.OVERDUE.getCode());

            // 更新逾期还款信息
            refreshRepayInfo(overdueMenu, repayBiz, overDueRepayPlan);

            overdueMenu.setStatus(EOverdueMenuStatus.YCL.getCode());
            BudgetOrder budgetOrder = budgetOrderBO
                .getBudgetOrderByRepayBizCode(repayBizCode);
            overdueMenu.setBudgetOrderCode(budgetOrder.getCode());
            overdueMenu.setRepayBizCode(overDueRepayPlan.getRepayBizCode());
            overdueMenu.setRepayPlanCode(overDueRepayPlan.getCode());

            overdueMenu.setHandleDatetime(new Date());
        } else if (ENotMateResult.ONE_CARD_MORE_LOAN.getCode().equals(
            overdueMenu.getNotMateResult())) {
            Long overdueAmount = 0L;
            for (XN632301Res res : repaybizList) {
                RepayBiz repayBiz = repayBizBO.getRepayBiz(res.getCode());
                RepayPlan overDueRepayPlan = repayPlanBO
                    .getRepayPlanCurMonth(repayBiz.getCode());
                // 还款计划状态是否更新
                overDueRepayPlan.setCurNodeCode(ERepayPlanNode.OVERDUE
                    .getCode());

                // 更新逾期还款信息
                refreshRepayInfo(overdueMenu, repayBiz, overDueRepayPlan,
                    res.getOverdueAmount());

                overdueMenu.setStatus(EOverdueMenuStatus.YCL.getCode());
                overdueMenu.setHandleDatetime(new Date());
                overdueAmount += StringValidater.toLong(res.getOverdueAmount());
            }
            if (overdueMenu.getOverdueAmount() != overdueAmount) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "填入的逾期金额与导入的逾期金额不符！");
            }
        }

        overdueMenuBO.refreshOverdueMenu(overdueMenu);
    }

    @Override
    public void deleteOverdueMenu(List<String> codeList) {
        for (String code : codeList) {
            OverdueMenu overdueMenu = overdueMenuBO.getOverdueMenu(code);
            if (EOverdueMenuStatus.YCL.getCode()
                .equals(overdueMenu.getStatus())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(), "客户姓名为"
                        + overdueMenu.getRealName() + "的名单已处理，不能删除！");
            }
            overdueMenuBO.removeOverdueMenu(code);
        }
    }

    @Override
    public Paginable<OverdueMenu> queryOverdueMenuPage(int start, int limit,
            OverdueMenu condition) {
        return overdueMenuBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<OverdueMenu> queryOverdueMenuList(OverdueMenu condition) {
        return overdueMenuBO.queryOverdueMenuList(condition);
    }

    @Override
    public OverdueMenu getOverdueMenu(String code) {
        OverdueMenu data = overdueMenuBO.getOverdueMenu(code);
        if (StringUtils.isNotBlank(data.getRepayBizCode())) {
            RepayBiz repayBiz = repayBizBO.getRepayBiz(data.getRepayBizCode());
            data.setRepayBiz(repayBiz);
        }
        return data;
    }

}
