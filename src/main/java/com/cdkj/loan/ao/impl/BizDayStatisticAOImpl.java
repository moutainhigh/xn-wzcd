package com.cdkj.loan.ao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdkj.loan.ao.IBizDayStatisticAO;
import com.cdkj.loan.bo.IBizDayStatisticBO;
import com.cdkj.loan.bo.IBudgetOrderBO;
import com.cdkj.loan.bo.IDepartmentBO;
import com.cdkj.loan.bo.IRepayPlanBO;
import com.cdkj.loan.bo.base.Paginable;
import com.cdkj.loan.common.AmountUtil;
import com.cdkj.loan.common.DateUtil;
import com.cdkj.loan.domain.BizDayStatistic;
import com.cdkj.loan.domain.BudgetOrder;
import com.cdkj.loan.domain.Department;
import com.cdkj.loan.domain.RepayPlan;
import com.cdkj.loan.dto.req.XN630901Req;
import com.cdkj.loan.enums.EDepartmentType;
import com.cdkj.loan.exception.BizException;

/**
 * 
 * @author: jiafr 
 * @since: 2018年8月6日 下午5:49:52 
 * @history:
 */
@Service
public class BizDayStatisticAOImpl implements IBizDayStatisticAO {

    @Autowired
    private IBizDayStatisticBO bizDayStatisticBO;

    @Autowired
    private IDepartmentBO departmentBO;

    @Autowired
    private IBudgetOrderBO budgetOrderBO;

    @Autowired
    private IRepayPlanBO repayPlanBO;

    @Override
    public String addBizDayStatistic(BizDayStatistic data) {
        return bizDayStatisticBO.saveBizDayStatistic(data);
    }

    @Override
    public int editBizDayStatistic(BizDayStatistic data) {
        if (!bizDayStatisticBO.isBizDayStatisticExist(data.getId())) {
            throw new BizException("xn0000", "记录编号不存在");
        }
        return bizDayStatisticBO.refreshBizDayStatistic(data);
    }

    @Override
    public int dropBizDayStatistic(String code) {
        if (!bizDayStatisticBO.isBizDayStatisticExist(code)) {
            throw new BizException("xn0000", "记录编号不存在");
        }
        return bizDayStatisticBO.removeBizDayStatistic(code);
    }

    @Override
    public Paginable<BizDayStatistic> queryBizDayStatisticPage(int start,
            int limit, BizDayStatistic condition) {
        return bizDayStatisticBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<BizDayStatistic> queryBizDayStatisticList(
            BizDayStatistic condition) {
        return bizDayStatisticBO.queryBizDayStatisticList(condition);
    }

    @Override
    public BizDayStatistic getBizDayStatistic(String code) {
        return bizDayStatisticBO.getBizDayStatistic(code);
    }

    @Override
    public void doBizDayStatistic() {
        Department companyCondition = new Department();
        companyCondition.setType(EDepartmentType.SUBBRANCH_COMPANY.getCode());
        List<Department> list = departmentBO
            .queryDepartmentList(companyCondition);
        for (Department department : list) {// 分公司集合
            BizDayStatistic data = new BizDayStatistic();
            data.setCompanyCode(department.getCode());// 分公司编号
            BudgetOrder budgetOrderCondition = new BudgetOrder();
            budgetOrderCondition.setBankFkDatetimeStart(DateUtil
                .getTodayStart());
            budgetOrderCondition.setBankFkDatetimeEnd(DateUtil.getTodayEnd());
            budgetOrderCondition.setCompanyCode(department.getCode());
            List<BudgetOrder> budgetOrderList = budgetOrderBO
                .queryBudgetOrderList(budgetOrderCondition);
            Long debitAmount = 0L;// 分公司当天银行放款总额
            if (CollectionUtils.isNotEmpty(budgetOrderList)) {
                for (BudgetOrder budgetOrder : budgetOrderList) {
                    debitAmount += getLong(budgetOrder.getBankFkAmount());
                }
            }
            data.setDebitAmount(debitAmount);// 借方发生额
            RepayPlan repayPlanCondition = new RepayPlan();
            repayPlanCondition.setRepayDatetimeStart(DateUtil.getTodayStart());
            repayPlanCondition.setRepayDatetimeEnd(DateUtil.getTodayEnd());
            repayPlanCondition.setCompanyCode(department.getCode());
            List<RepayPlan> repayPlanList = repayPlanBO
                .queryRepayPlanList(repayPlanCondition);
            Long loanAmount = 0L;// 分公司当天客户还款总额
            Long loanOverdueAmount = 0L; // 分公司当天客户逾期总额
            if (CollectionUtils.isNotEmpty(repayPlanList)) {
                for (RepayPlan repayPlan : repayPlanList) {
                    loanAmount += getLong(repayPlan.getPayedAmount());
                    loanOverdueAmount += getLong(repayPlan.getOverdueAmount());
                }
            }
            data.setLoanAmount(loanAmount);// 贷方发生额
            data.setLoanOverdueAmount(loanOverdueAmount);// 贷方逾期金额
            BizDayStatistic bizDayStatistic = bizDayStatisticBO
                .getBizDayStatisticByDate(DateUtil.strToDate(
                    calDate(DateUtil.dateToStr(new Date(),
                        DateUtil.FRONT_DATE_FORMAT_STRING), -1),
                    DateUtil.FRONT_DATE_FORMAT_STRING), department.getCode());
            Long balance = 0L;
            Long overdueBalance = 0L;
            if (null == bizDayStatistic) {
                balance = data.getDebitAmount() - data.getLoanAmount();
                overdueBalance = data.getLoanOverdueAmount();
            } else {
                balance = bizDayStatistic.getBalance() + data.getDebitAmount()
                        - data.getLoanAmount();
                overdueBalance = bizDayStatistic.getOverdueBalance()
                        + data.getLoanOverdueAmount();
            }
            data.setBalance(balance);// 当日余额
            data.setOverdueBalance(overdueBalance);// 当日逾期余额
            data.setOverdueRate(AmountUtil.div(
                getLong(data.getOverdueBalance()), getLong(data.getBalance())));// 当日逾期率
            data.setDate(DateUtil.strToDate(
                DateUtil.getToday(DateUtil.FRONT_DATE_FORMAT_STRING),
                DateUtil.FRONT_DATE_FORMAT_STRING));// 日期
            bizDayStatisticBO.saveBizDayStatistic(data);
        }
    }

    @Override
    public List<BizDayStatistic> carLoanBizStatistic(XN630901Req req) {
        String date = "";
        if (null != req.getDate()) {
            date = calDate(req.getDate(), -1);// 查询日期的前一天
        } else {
            date = calDate(DateUtil.dateToStr(new Date(),
                DateUtil.FRONT_DATE_FORMAT_STRING), -1);// 今天的前一天
        }
        BizDayStatistic condition = new BizDayStatistic();
        condition.setDate(DateUtil.strToDate(date,
            DateUtil.FRONT_DATE_FORMAT_STRING));
        List<BizDayStatistic> list = bizDayStatisticBO
            .queryBizDayStatisticList(condition);
        if (CollectionUtils.isNotEmpty(list)) {
            for (BizDayStatistic data : list) {
                Department company = departmentBO.getDepartment(data
                    .getCompanyCode());
                data.setCompanyName(company.getName());// 分公司名称
                BizDayStatistic preBizDayStatistic = bizDayStatisticBO
                    .getBizDayStatisticByDate(DateUtil.strToDate(
                        calDate(req.getDate(), -2),
                        DateUtil.FRONT_DATE_FORMAT_STRING), data
                        .getCompanyCode());// 该分公司前两天的数据
                if (null != preBizDayStatistic) {
                    data.setPreBalance(String.valueOf(preBizDayStatistic
                        .getBalance()));
                    data.setPreOverdueBalance(String.valueOf(preBizDayStatistic
                        .getOverdueBalance()));
                    data.setPreOverdueRate(String.valueOf(preBizDayStatistic
                        .getOverdueRate()));
                }
            }
        }
        return list;
    }

    private Long getLong(Object obj) {
        if (null == obj) {
            return 0L;
        } else {
            return (Long) obj;
        }
    }

    private String calDate(String date, int i) {
        Date strToDate = DateUtil.strToDate(date,
            DateUtil.FRONT_DATE_FORMAT_STRING);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate);
        calendar.add(Calendar.DATE, i);
        Date time = calendar.getTime();
        return DateUtil.dateToStr(time, DateUtil.FRONT_DATE_FORMAT_STRING);
    }
}
