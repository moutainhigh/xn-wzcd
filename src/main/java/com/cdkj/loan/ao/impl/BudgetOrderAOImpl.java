package com.cdkj.loan.ao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdkj.loan.ao.IBudgetOrderAO;
import com.cdkj.loan.ao.IRepointDetailAO;
import com.cdkj.loan.bo.IAccountBO;
import com.cdkj.loan.bo.IAdvanceFundBO;
import com.cdkj.loan.bo.IBankBO;
import com.cdkj.loan.bo.IBankSubbranchBO;
import com.cdkj.loan.bo.IBankcardBO;
import com.cdkj.loan.bo.IBudgetOrderBO;
import com.cdkj.loan.bo.IBudgetOrderFeeBO;
import com.cdkj.loan.bo.IBudgetOrderGpsBO;
import com.cdkj.loan.bo.ICarDealerBO;
import com.cdkj.loan.bo.ICarDealerProtocolBO;
import com.cdkj.loan.bo.ICollectBankcardBO;
import com.cdkj.loan.bo.ICreditBO;
import com.cdkj.loan.bo.ICreditUserBO;
import com.cdkj.loan.bo.IDepartmentBO;
import com.cdkj.loan.bo.IGpsBO;
import com.cdkj.loan.bo.IInsuranceCompanyBO;
import com.cdkj.loan.bo.ILoanCsBO;
import com.cdkj.loan.bo.ILogisticsBO;
import com.cdkj.loan.bo.INodeFlowBO;
import com.cdkj.loan.bo.IRepayBizBO;
import com.cdkj.loan.bo.IRepayPlanBO;
import com.cdkj.loan.bo.IRepointDetailBO;
import com.cdkj.loan.bo.ISYSBizLogBO;
import com.cdkj.loan.bo.ISYSConfigBO;
import com.cdkj.loan.bo.ISYSUserBO;
import com.cdkj.loan.bo.ISmsOutBO;
import com.cdkj.loan.bo.IUserBO;
import com.cdkj.loan.bo.base.Paginable;
import com.cdkj.loan.common.AmountUtil;
import com.cdkj.loan.common.DateUtil;
import com.cdkj.loan.common.PhoneUtil;
import com.cdkj.loan.common.SysConstants;
import com.cdkj.loan.core.StringValidater;
import com.cdkj.loan.domain.AdvanceFund;
import com.cdkj.loan.domain.Bank;
import com.cdkj.loan.domain.BankSubbranch;
import com.cdkj.loan.domain.BudgetOrder;
import com.cdkj.loan.domain.BudgetOrderFee;
import com.cdkj.loan.domain.BudgetOrderGps;
import com.cdkj.loan.domain.CarDealer;
import com.cdkj.loan.domain.CarDealerProtocol;
import com.cdkj.loan.domain.CollectBankcard;
import com.cdkj.loan.domain.Credit;
import com.cdkj.loan.domain.CreditUser;
import com.cdkj.loan.domain.Department;
import com.cdkj.loan.domain.InsuranceCompany;
import com.cdkj.loan.domain.LoanCs;
import com.cdkj.loan.domain.NodeFlow;
import com.cdkj.loan.domain.RepayBiz;
import com.cdkj.loan.domain.RepayPlan;
import com.cdkj.loan.domain.RepointDetail;
import com.cdkj.loan.domain.SYSConfig;
import com.cdkj.loan.domain.SYSUser;
import com.cdkj.loan.domain.User;
import com.cdkj.loan.dto.req.XN632120Req;
import com.cdkj.loan.dto.req.XN632120ReqRepointDetail;
import com.cdkj.loan.dto.req.XN632141Req;
import com.cdkj.loan.dto.req.XN632142Req;
import com.cdkj.loan.dto.req.XN632143Req;
import com.cdkj.loan.dto.req.XN632144Req;
import com.cdkj.loan.dto.req.XN632191Req;
import com.cdkj.loan.dto.req.XN632192Req;
import com.cdkj.loan.dto.req.XN632193Req;
import com.cdkj.loan.dto.req.XN632194Req;
import com.cdkj.loan.dto.req.XN632200Req;
import com.cdkj.loan.dto.req.XN632220Req;
import com.cdkj.loan.dto.req.XN632230Req;
import com.cdkj.loan.dto.req.XN632270Req;
import com.cdkj.loan.dto.req.XN632271Req;
import com.cdkj.loan.dto.req.XN632272Req;
import com.cdkj.loan.dto.req.XN632280Req;
import com.cdkj.loan.dto.req.XN632341Req;
import com.cdkj.loan.dto.res.XN632234Res;
import com.cdkj.loan.dto.res.XN632290Res;
import com.cdkj.loan.dto.res.XN632291Res;
import com.cdkj.loan.enums.EAccountType;
import com.cdkj.loan.enums.EAdvanceFundNode;
import com.cdkj.loan.enums.EAdvanceType;
import com.cdkj.loan.enums.EApproveResult;
import com.cdkj.loan.enums.EAssureType;
import com.cdkj.loan.enums.EBankType;
import com.cdkj.loan.enums.EBizErrorCode;
import com.cdkj.loan.enums.EBizLogType;
import com.cdkj.loan.enums.EBoolean;
import com.cdkj.loan.enums.EBudgetFrozenStatus;
import com.cdkj.loan.enums.EBudgetOrderFeeWay;
import com.cdkj.loan.enums.EBudgetOrderNode;
import com.cdkj.loan.enums.EBudgetType;
import com.cdkj.loan.enums.EButtonCode;
import com.cdkj.loan.enums.ECollectBankcardType;
import com.cdkj.loan.enums.ECurrency;
import com.cdkj.loan.enums.EEnterFileStatus;
import com.cdkj.loan.enums.EFbhStatus;
import com.cdkj.loan.enums.EGpsFeeWay;
import com.cdkj.loan.enums.EGpsTypeProtocol;
import com.cdkj.loan.enums.EIsAdvanceFund;
import com.cdkj.loan.enums.ELoanPeriod;
import com.cdkj.loan.enums.ELoanRole;
import com.cdkj.loan.enums.ELogisticsType;
import com.cdkj.loan.enums.ELyAmountType;
import com.cdkj.loan.enums.EMakeCardStatus;
import com.cdkj.loan.enums.EOtherType;
import com.cdkj.loan.enums.ERateType;
import com.cdkj.loan.enums.ERepointDetailStatus;
import com.cdkj.loan.enums.ERepointDetailType;
import com.cdkj.loan.enums.ERepointDetailUseMoneyPurpose;
import com.cdkj.loan.enums.EServiceChargeWay;
import com.cdkj.loan.enums.EShouldBackStatus;
import com.cdkj.loan.enums.EUseMoneyPurpose;
import com.cdkj.loan.enums.EUserKind;
import com.cdkj.loan.exception.BizException;

@Service
public class BudgetOrderAOImpl implements IBudgetOrderAO {

    @Autowired
    private IBudgetOrderBO budgetOrderBO;

    @Autowired
    private ICreditUserBO creditUserBO;

    @Autowired
    private INodeFlowBO nodeFlowBO;

    @Autowired
    private ISYSBizLogBO sysBizLogBO;

    @Autowired
    private IBankBO bankBO;

    @Autowired
    private IBankSubbranchBO bankSubbranchBO;

    @Autowired
    private ICarDealerBO carDealerBO;

    @Autowired
    private IInsuranceCompanyBO insuranceCompanyBO;

    @Autowired
    private IDepartmentBO departmentBO;

    @Autowired
    private ISYSUserBO sysUserBO;

    @Autowired
    private IUserBO userBO;

    @Autowired
    private IGpsBO gpsBO;

    @Autowired
    private IBudgetOrderGpsBO budgetOrderGpsBO;

    @Autowired
    private ILogisticsBO logisticsBO;

    @Autowired
    private IAdvanceFundBO advanceFundBO;

    @Autowired
    private ICollectBankcardBO collectBankcardBO;

    @Autowired
    private IRepointDetailBO repointDetailBO;

    @Autowired
    private ICarDealerProtocolBO carDealerProtocolBO;

    @Autowired
    private IBankcardBO bankcardBO;

    @Autowired
    private IRepayBizBO repayBizBO;

    @Autowired
    private IRepayPlanBO repayPlanBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private ISmsOutBO smsOutBO;

    @Autowired
    private IBudgetOrderFeeBO budgetOrderFeeBO;

    @Autowired
    private ILoanCsBO loanCsBO;

    @Autowired
    private ISYSConfigBO sysConfigBO;

    @Autowired
    private IRepointDetailAO repointDetailAO;

    @Autowired
    private ICreditBO creditBO;

    @Override
    @Transactional
    public void editBudgetOrder(XN632120Req req) {
        BudgetOrder data = budgetOrderBO
            .getBudgetOrder(req.getBudgetOrderCode());
        if (!EBudgetOrderNode.START_NODE.getCode().equals(data.getCurNodeCode())
                && !EBudgetOrderNode.FILL_AGAIN.getCode()
                    .equals(data.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是预算单申请节点，不能操作");
        }

        if (EBudgetType.OUT.getCode().equals(req.getType())) {// 外单
            editForeignBudgetOrder(data, req);
            return;
        }

        data.setApplyUserCompany(req.getApplyUserCompany());
        data.setApplyUserDuty(req.getApplyUserDuty());
        data.setMarryState(req.getMarryState());
        data.setCustomerType(req.getCustomerType());
        data.setCarDealerCode(req.getCarDealerCode());
        data.setOriginalPrice(StringValidater.toLong(req.getOriginalPrice()));

        data.setCarModel(req.getCarModel());
        data.setFrameNo(req.getFrameNo());
        data.setLoanPeriods(StringValidater.toInteger(req.getLoanPeriods()));
        data.setInvoicePrice(StringValidater.toLong(req.getInvoicePrice()));
        data.setRateType(req.getRateType());

        data.setBankRate(StringValidater.toDouble(req.getBankRate()));
        Long loanAmount = 0L;
        if (null == req.getLoanAmount()) {
            loanAmount = data.getLoanAmount();
        } else {
            loanAmount = StringValidater.toLong(req.getLoanAmount());
            data.setLoanAmount(loanAmount);
        }
        Long invoicePrice = StringValidater.toLong(req.getInvoicePrice());
        // 我司贷款成数=贷款金额/发票价格
        data.setCompanyLoanCs(AmountUtil.div(loanAmount, invoicePrice));

        // 获取我司贷款成数区间 标准
        LoanCs loanCsCondition = new LoanCs();
        loanCsCondition.setType(data.getShopWay());
        List<LoanCs> loanCsList = loanCsBO.queryLoanCsList(loanCsCondition);
        LoanCs resultCs = null;
        if (CollectionUtils.isNotEmpty(loanCsList)) {
            resultCs = loanCsList.get(0);
        }
        if (null != resultCs) {
            // 判断新我司贷款成数是否在标准内
            if (data.getCompanyLoanCs() > resultCs.getMaxCs()
                    || data.getCompanyLoanCs() < resultCs.getMinCs()) {
                // 不在我司准入贷款成数标准内
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前贷款成数不在我司贷款成数有效范围内");
            }
        }

        data.setIsAdvanceFund(req.getIsAdvanceFund());
        Long fee = StringValidater.toLong(req.getFee());// 服务费
        data.setFee(fee);
        double feeRate = AmountUtil.div(fee, loanAmount);
        data.setGlobalRate(
            feeRate + StringValidater.toDouble(req.getBankRate()));// 综合利率=服务费/贷款金额+银行利率
        data.setCarDealerSubsidy(
            StringValidater.toLong(req.getCarDealerSubsidy()));// 厂家贴息
        Long totalAmount = loanAmount + fee;// 贷款总额=贷款额+服务费
        data.setBankLoanCs(AmountUtil.div(totalAmount, invoicePrice));// 银行贷款成数=(贷款金额+服务费)/发票价格

        data.setApplyUserMonthIncome(
            StringValidater.toLong(req.getApplyUserMonthIncome()));
        data.setApplyUserSettleInterest(
            StringValidater.toLong(req.getApplyUserSettleInterest()));
        data.setApplyUserBalance(
            StringValidater.toLong(req.getApplyUserBalance()));
        data.setApplyUserJourShowIncome(req.getApplyUserJourShowIncome());

        data.setApplyUserIsPrint(req.getApplyUserIsPrint());
        data.setGhMonthIncome(StringValidater.toLong(req.getGhMonthIncome()));
        data.setGhSettleInterest(
            StringValidater.toLong(req.getGhSettleInterest()));
        data.setGhBalance(StringValidater.toLong(req.getGhBalance()));
        data.setGhJourShowIncome(req.getGhJourShowIncome());
        data.setGhIsPrint(req.getGhIsPrint());

        data.setEmergencyName1(req.getEmergencyName1());
        data.setEmergencyRelation1(req.getEmergencyRelation1());
        data.setEmergencyMobile1(req.getEmergencyMobile1());

        data.setEmergencyName2(req.getEmergencyName2());
        data.setEmergencyRelation2(req.getEmergencyRelation2());
        data.setEmergencyMobile2(req.getEmergencyMobile2());

        data.setGuarantor1MonthIncome(
            StringValidater.toLong(req.getGuarantor1MonthIncome()));
        data.setGuarantor1SettleInterest(
            StringValidater.toLong(req.getGuarantor1SettleInterest()));
        data.setGuarantor1Balance(
            StringValidater.toLong(req.getGuarantor1Balance()));
        data.setGuarantor1JourShowIncome(req.getGuarantor1JourShowIncome());

        data.setGuarantor1IsPrint(req.getGuarantor1IsPrint());
        data.setGuarantor2MonthIncome(
            StringValidater.toLong(req.getGuarantor2MonthIncome()));
        data.setGuarantor2SettleInterest(
            StringValidater.toLong(req.getGuarantor2SettleInterest()));
        data.setGuarantor2Balance(
            StringValidater.toLong(req.getGuarantor2Balance()));
        data.setGuarantor2JourShowIncome(req.getGuarantor2JourShowIncome());

        data.setGuarantor2IsPrint(req.getGuarantor2IsPrint());
        data.setIsHouseProperty(req.getIsHouseProperty());
        data.setHouseProperty(req.getHouseProperty());
        data.setIsHouseContract(req.getIsHouseContract());
        data.setHouseInvoice(req.getHouseInvoice());

        data.setIsLicense(req.getIsLicense());
        data.setLicense(req.getLicense());
        data.setIsSiteProve(req.getIsSiteProve());
        data.setSiteProve(req.getSiteProve());
        data.setSiteArea(req.getSiteArea());

        data.setCarType(req.getCarType());
        data.setIsDriceLicense(req.getIsDriceLicense());
        data.setDriceLicense(req.getDriceLicense());
        data.setOtherPropertyNote(req.getOtherPropertyNote());
        data.setApplyBirthAddress(req.getApplyBirthAddress());

        data.setApplyNowAddress(req.getApplyNowAddress());
        data.setHouseType(req.getHouseType());
        data.setGhBirthAddress(req.getGhBirthAddress());
        data.setGuarantor1BirthAddress(req.getGuarantor1BirthAddress());
        data.setGuarantor2BirthAddress(req.getGuarantor2BirthAddress());

        data.setOtherNote(req.getOtherNote());
        SYSConfig sysConfigoil = sysConfigBO
            .getSYSConfig(SysConstants.BUDGET_OIL_SUBSIDY_RATE);
        Double OilSubsidyBFB = StringValidater
            .toDouble(sysConfigoil.getCvalue());
        Long OilSubsidy = AmountUtil.mul(data.getLoanAmount(), OilSubsidyBFB);
        data.setOilSubsidy(OilSubsidy);
        data.setOilSubsidyKil(StringValidater.toDouble(req.getOilSubsidyKil()));
        data.setIsPlatInsure(req.getIsPlatInsure());

        // 正常单 根据协议计算出各种手续费
        XN632291Res xn632291Res = carDealerProtocolBO
            .calProtocolFee(data.getCode(), data.getCarDealerCode());
        data.setOilSubsidy(StringValidater.toLong(xn632291Res.getOilSubsidy()));// 油补
        data.setGpsDeduct(StringValidater.toLong(xn632291Res.getGpsDeduct()));// GPS提成
        data.setGpsFee(StringValidater.toLong(xn632291Res.getGpsFee()));// GPS收费
        data.setLyAmount(StringValidater.toLong(xn632291Res.getLyAmount()));// 履约保证金
        data.setFxAmount(StringValidater.toLong(xn632291Res.getFxAmount()));// 风险担保金
        data.setOtherFee(StringValidater.toLong(xn632291Res.getOtherFee()));// 杂费

        Long serviceCharge = StringValidater.toLong(xn632291Res.getLyAmount())
                + StringValidater.toLong(xn632291Res.getFxAmount())
                + StringValidater.toLong(xn632291Res.getGpsFee())
                + StringValidater.toLong(xn632291Res.getOtherFee());
        // 收客户手续费合计：履约保证金+担保风险金+GPS收费+杂费
        data.setServiceCharge(serviceCharge);
        data.setServiceChargeWay(req.getServiceChargeWay());// 手续费收取方式
        data.setGpsFeeWay(req.getGpsFeeWay());// gps费收取方式

        data.setMarryDivorce(req.getMarryDivorce());
        data.setApplyUserHkb(req.getApplyUserHkb());
        data.setBankBillPdf(req.getBankBillPdf());
        data.setSingleProvePdf(req.getSingleProvePdf());
        data.setIncomeProvePdf(req.getIncomeProvePdf());

        data.setLiveProvePdf(req.getLiveProvePdf());
        data.setBuildProvePdf(req.getBuildProvePdf());
        data.setHkbFirstPage(req.getHkbFirstPage());
        data.setHkbMainPage(req.getHkbMainPage());
        data.setGhHkb(req.getGhHkb());

        data.setGuarantor1Hkb(req.getGuarantor1Hkb());
        data.setGuarantor2Hkb(req.getGuarantor2Hkb());
        data.setHousePic(req.getHousePic());
        data.setHouseUnitPic(req.getHouseUnitPic());
        data.setHouseDoorPic(req.getHouseDoorPic());

        data.setHouseRoomPic(req.getHouseRoomPic());
        data.setHouseCustomerPic(req.getHouseCustomerPic());
        data.setHouseSaleCustomerPic(req.getHouseSaleCustomerPic());
        data.setCompanyNamePic(req.getCompanyNamePic());
        data.setCompanyPlacePic(req.getCompanyPlacePic());

        data.setCompanyWorkshopPic(req.getCompanyWorkshopPic());
        data.setCompanySaleCustomerPic(req.getCompanySaleCustomerPic());
        data.setSecondHgz(req.getSecondHgz());
        data.setSecondOdometer(req.getSecondOdometer());
        data.setSecondCarFrontPic(req.getSecondCarFrontPic());

        data.setSecondConsolePic(req.getSecondConsolePic());
        data.setSecond300Pdf(req.getSecond300Pdf());
        data.setSecondQxbPic(req.getSecondQxbPic());
        data.setSecondCarInPic(req.getSecondCarInPic());
        data.setSecondNumber(req.getSecondNumber());

        data.setOtherFilePdf(req.getOtherFilePdf());
        data.setOtherApplyNote(req.getOtherApplyNote());
        data.setApplyDatetime(new Date());

        String preNodeCode = data.getCurNodeCode();// 当前节点
        if (EButtonCode.SEND.getCode().equals(req.getDealType())) {
            EBudgetOrderNode node = EBudgetOrderNode.getMap().get(nodeFlowBO
                .getNodeFlowByCurrentNode(data.getCurNodeCode()).getNextNode());
            data.setCurNodeCode(node.getCode());
            // 日志记录
            sysBizLogBO.saveNewAndPreEndSYSBizLog(data.getCode(),
                EBizLogType.BUDGET_ORDER, data.getCode(), preNodeCode,
                node.getCode(), node.getValue(), req.getOperator());
        }

        Department company = departmentBO.getDepartment(data.getCompanyCode());
        CreditUser user = creditUserBO.getCreditUserByCreditCode(
            data.getCreditCode(), ELoanRole.APPLY_USER);
        CarDealer carDealer = carDealerBO.getCarDealer(data.getCarDealerCode());
        // 协议外返点（手填） 和 应退按揭款(不垫资 退给客户 手填)
        List<XN632120ReqRepointDetail> repointDetailList = req
            .getRepointDetailList();// 前端填写的不垫资应退按揭款 和 协议外返点
        for (XN632120ReqRepointDetail xn632120ReqRepointDetail : repointDetailList) {
            // 应退按揭款 不垫资
            if (EUseMoneyPurpose.MORTGAGE.getCode()
                .equals(xn632120ReqRepointDetail.getUseMoneyPurpose())) {
                if (EIsAdvanceFund.YES.getCode()
                    .equals(data.getIsAdvanceFund())) {
                    continue;// 垫资 应退按揭款就是垫资金额 直接生成 不能手填
                }
                data.setShouldBackAmount(StringValidater
                    .toLong(xn632120ReqRepointDetail.getRepointAmount()));// 应退按揭款金额
                data.setShouldBackUserName(
                    xn632120ReqRepointDetail.getCarDealerName());
                data.setShouldBackAccountNo(
                    xn632120ReqRepointDetail.getAccountNo());
                data.setShouldBackOpenBankName(
                    xn632120ReqRepointDetail.getOpenBankName());
                data.setShouldBackAccountName(
                    xn632120ReqRepointDetail.getAccountName());
            }
            // 协议外返点
            if (EUseMoneyPurpose.PROTOCOL_OUTER.getCode()
                .equals(xn632120ReqRepointDetail.getUseMoneyPurpose())) {
                RepointDetail repointDetail = new RepointDetail();
                repointDetail.setType(ERepointDetailType.NORMAL.getCode());
                repointDetail.setCompanyCode(data.getCompanyCode());
                repointDetail.setCompanyName(company.getName());
                repointDetail.setBudgetCode(data.getCode());
                repointDetail.setUserName(data.getCustomerName());
                repointDetail.setIdNo(user.getIdNo());
                repointDetail.setCarDealerName(
                    xn632120ReqRepointDetail.getCarDealerName());// 单位名称（汽车经销商）
                repointDetail.setCarType(data.getCarType());
                repointDetail.setLoanAmount(data.getLoanAmount());
                repointDetail.setBankRate(data.getBankRate());
                Double benchmarkRate = benchmarkRate(data);
                repointDetail.setBenchmarkRate(benchmarkRate);
                repointDetail.setFee(fee);
                repointDetail.setUseMoneyPurpose(
                    EUseMoneyPurpose.PROTOCOL_OUTER.getCode());
                repointDetail.setRepointAmount(StringValidater
                    .toLong(xn632120ReqRepointDetail.getRepointAmount()));// 返点金额
                repointDetail
                    .setAccountNo(xn632120ReqRepointDetail.getAccountNo());// 账号
                repointDetail.setOpenBankName(
                    xn632120ReqRepointDetail.getOpenBankName());// 开户行
                repointDetail
                    .setAccountName(xn632120ReqRepointDetail.getAccountName());// 户名
                repointDetail.setCurNodeCode(
                    ERepointDetailStatus.TODO_MAKE_BILL.getCode());
                repointDetailBO.saveRepointDetail(repointDetail);
            }
        }
        // 计算协议内返点（返回的返点明细对象里只有 ：返点用款用途 汽车经销商收款账号编号 和这个账号对应比例的返点金额 基准利率）
        RepointDetail innerRepointDetail = repointDetailAO
            .calculateRepointDetail(data);
        innerRepointDetail.setType(ERepointDetailType.NORMAL.getCode());
        innerRepointDetail.setCompanyCode(data.getCompanyCode());
        innerRepointDetail.setCompanyName(company.getName());
        innerRepointDetail.setBudgetCode(data.getCode());
        innerRepointDetail.setUserName(data.getCustomerName());

        innerRepointDetail.setIdNo(user.getIdNo());
        innerRepointDetail.setCarDealerCode(data.getCarDealerCode());
        innerRepointDetail.setCarDealerName(carDealer.getFullName());
        innerRepointDetail.setCarType(data.getCarModel());
        innerRepointDetail.setLoanAmount(data.getLoanAmount());

        innerRepointDetail.setBankRate(data.getGlobalRate());
        innerRepointDetail.setFee(fee);
        CollectBankcard collectBankcard = collectBankcardBO
            .getCollectBankcard(innerRepointDetail.getAccountCode());
        innerRepointDetail.setAccountNo(collectBankcard.getBankcardNumber());
        innerRepointDetail.setOpenBankName(collectBankcard.getSubbranch());
        innerRepointDetail.setAccountName(collectBankcard.getRealName());
        innerRepointDetail
            .setCurNodeCode(ERepointDetailStatus.TODO_MAKE_BILL.getCode());
        repointDetailBO.saveRepointDetail(innerRepointDetail);
        // 垫资应退按揭款 直接生成 不能手填 并且就是垫资金额
        if (EIsAdvanceFund.YES.getCode().equals(data.getIsAdvanceFund())) {
            calculateShouldBackMorgage(data);
            data.setShouldBackStatus(
                EShouldBackStatus.NO_NEED_REFUND.getCode());
        }
        // 删除
        budgetOrderGpsBO.removeBudgetOrderGpsList(data.getCode());
        // 添加
        budgetOrderGpsBO.saveBudgetOrderGpsList(data.getCode(),
            req.getGpsList());

        budgetOrderBO.refresh(data);
    }

    @Override
    @Transactional
    public void approveAreaManager(String code, String operator,
            String approveResult, String approveNote) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.AREA_AUDIT.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是区域总经理准入审核节点，不能操作");
        }

        // 之前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        if (EApproveResult.PASS.getCode().equals(approveResult)) {
            budgetOrder.setCurNodeCode(nodeFlowBO
                .getNodeFlowByCurrentNode(EBudgetOrderNode.AREA_AUDIT.getCode())
                .getNextNode());
        } else {
            budgetOrder.setCurNodeCode(nodeFlowBO
                .getNodeFlowByCurrentNode(EBudgetOrderNode.AREA_AUDIT.getCode())
                .getBackNode());
        }
        budgetOrderBO.refreshAreaManagerApprove(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void approveBranchCompany(String code, String operator,
            String approveResult, String approveNote) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.COMPANY_AUDIT.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是准入审查省分公司总经理审核节点，不能操作");
        }
        // 之前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        if (EApproveResult.PASS.getCode().equals(approveResult)) {
            budgetOrder
                .setCurNodeCode(nodeFlowBO
                    .getNodeFlowByCurrentNode(
                        EBudgetOrderNode.COMPANY_AUDIT.getCode())
                    .getNextNode());
        } else {
            budgetOrder
                .setCurNodeCode(nodeFlowBO
                    .getNodeFlowByCurrentNode(
                        EBudgetOrderNode.COMPANY_AUDIT.getCode())
                    .getBackNode());
        }
        budgetOrderBO.refreshBranchCompanyApprove(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void approveGlobalManager(String code, String operator,
            String approveResult, String approveNote) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        if (!EBudgetOrderNode.SECOND_AUDIT.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是准入审查二审节点，不能操作");
        }
        if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点处于物流传递中，不能操作");
        }
        String preCurrentNode = budgetOrder.getCurNodeCode();// 当前节点
        if (EApproveResult.PASS.getCode().equals(approveResult)) {
            // 审核通过
            // 产生手续费
            budgetOrderFeeBO.saveBudgetOrderFee(budgetOrder, operator);
            // 判断预算单是否垫资
            if (EIsAdvanceFund.NO.getCode()
                .equals(budgetOrder.getIsAdvanceFund())) {
                // 不垫资 进入银行放款流程第一步
                EBudgetOrderNode bankLoanNode = null;
                Department company = departmentBO
                    .getDepartment(budgetOrder.getCompanyCode());
                if ("温州市".equals(company.getCityNo())) {
                    // 本地
                    bankLoanNode = EBudgetOrderNode.SALESMAN_SEND_LOGISTICS;
                } else {
                    // 外地
                    bankLoanNode = EBudgetOrderNode.BRANCH_SEND_LOGISTICS;
                }
                budgetOrder.setCurNodeCode(bankLoanNode.getCode());
                sysBizLogBO.saveSYSBizLog(budgetOrder.getCode(),
                    EBizLogType.BANK_LOAN_COMMIT, budgetOrder.getCode(),
                    bankLoanNode.getCode(), bankLoanNode.getValue(), operator);
                budgetOrderBO.bankLoanConfirmSubmitBank(budgetOrder);
                // 当前节点
                String curNodeCode = budgetOrder.getCurNodeCode();
                String nextNodeCode = nodeFlowBO
                    .getNodeFlowByCurrentNode(curNodeCode).getNextNode();
                // 生成资料传递
                logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
                    budgetOrder.getCode(), budgetOrder.getSaleUserId(),
                    curNodeCode, nextNodeCode);
                // 产生物流单后改变状态为物流传递中
                budgetOrder.setIsLogistics(EBoolean.YES.getCode());
                budgetOrderBO.updateIsLogistics(budgetOrder);
            } else {
                // 垫资
                // 预算单节点改为垫资审核（进入垫资审核流程）
                budgetOrder.setCurNodeCode(
                    EBudgetOrderNode.ADVANCE_FUND_AUDIT.getCode());
                // 生成垫资单判断是本地公司业务还是外地公司业务
                Department company = departmentBO
                    .getDepartment(budgetOrder.getCompanyCode());
                if ("温州市".equals(company.getCityNo())) {
                    // 本地业务 打款给汽车经销商
                    AdvanceFund data = new AdvanceFund();
                    data.setBudgetCode(budgetOrder.getCode());
                    data.setType(EAdvanceType.PARENT_BIZ.getCode());
                    data.setCustomerName(budgetOrder.getCustomerName());
                    data.setCompanyCode(budgetOrder.getCompanyCode());
                    data.setCarDealerCode(budgetOrder.getCarDealerCode());
                    data.setUseAmount(budgetOrder.getShouldBackAmount());// 应退按揭款
                    data.setLoanAmount(budgetOrder.getLoanAmount());
                    data.setServiceCharge(budgetOrder.getServiceCharge());
                    data.setServiceChargeWay(budgetOrder.getServiceChargeWay());
                    data.setGpsFee(budgetOrder.getGpsFee());
                    data.setGpsFeeWay(budgetOrder.getGpsFeeWay());
                    data.setLoanBankCode(budgetOrder.getLoanBankCode());
                    data.setIsAdvanceFund(budgetOrder.getIsAdvanceFund());
                    CollectBankcard condition = new CollectBankcard();
                    condition.setCompanyCode(budgetOrder.getCarDealerCode());
                    List<CollectBankcard> list = collectBankcardBO
                        .queryCollectBankcardList(condition);
                    String collectBankcardCode = null;
                    for (CollectBankcard collectBankcard : list) {
                        if (ECollectBankcardType.DEALER_COLLECT.getCode()
                            .equals(collectBankcard.getType())) {
                            // 经销商的收款账号
                            collectBankcardCode = collectBankcard.getCode();
                        }
                    }
                    // 汽车经销商的账号
                    data.setCollectBankcardCode(collectBankcardCode);
                    data.setUpdater(operator);
                    data.setUpdateDatetime(new Date());
                    data.setCurNodeCode(
                        EAdvanceFundNode.PARENT_CONFIRM.getCode());

                    String advanceFundCode = advanceFundBO
                        .saveAdvanceFund(data);

                    sysBizLogBO.saveSYSBizLog(budgetOrder.getCode(),
                        EBizLogType.ADVANCE_FUND_PARENT, advanceFundCode,
                        EAdvanceFundNode.PARENT_CONFIRM.getCode(),
                        EAdvanceFundNode.PARENT_CONFIRM.getValue(), operator);

                } else {
                    // 外地业务 打款给分公司
                    AdvanceFund data = new AdvanceFund();
                    data.setBudgetCode(budgetOrder.getCode());
                    data.setType(EAdvanceType.BRANCH_BIZ.getCode());
                    data.setCustomerName(budgetOrder.getCustomerName());
                    data.setCompanyCode(budgetOrder.getCompanyCode());
                    data.setCarDealerCode(budgetOrder.getCarDealerCode());
                    // 用款应该是预算单的应退按揭款 现在暂时用贷款金额 用款用途做完后换成用款用途的应退按揭款
                    data.setUseAmount(budgetOrder.getShouldBackAmount());
                    data.setLoanAmount(budgetOrder.getLoanAmount());
                    data.setServiceCharge(budgetOrder.getServiceCharge());
                    data.setServiceChargeWay(budgetOrder.getServiceChargeWay());
                    data.setGpsFee(budgetOrder.getGpsFee());
                    data.setGpsFeeWay(budgetOrder.getGpsFeeWay());
                    data.setLoanBankCode(budgetOrder.getLoanBankCode());
                    data.setIsAdvanceFund(budgetOrder.getIsAdvanceFund());
                    CollectBankcard condition = new CollectBankcard();
                    condition.setCompanyCode(budgetOrder.getCompanyCode());
                    List<CollectBankcard> list = collectBankcardBO
                        .queryCollectBankcardList(condition);
                    String collectBankcardCode = null;
                    for (CollectBankcard collectBankcard : list) {
                        if (ECollectBankcardType.PLATFORM.getCode()
                            .equals(collectBankcard.getType())) {
                            // 公司普通账户
                            collectBankcardCode = collectBankcard.getCode();
                        }
                    }
                    // 分公司的账号
                    data.setCollectBankcardCode(collectBankcardCode);
                    data.setUpdater(operator);
                    data.setUpdateDatetime(new Date());
                    data.setCurNodeCode(
                        EAdvanceFundNode.BRANCH_CONFIRM.getCode());

                    String advanceFundCode = advanceFundBO
                        .saveAdvanceFund(data);

                    sysBizLogBO.saveSYSBizLog(budgetOrder.getCode(),
                        EBizLogType.ADVANCE_FUND_BRANCH, advanceFundCode,
                        EAdvanceFundNode.BRANCH_CONFIRM.getCode(),
                        EAdvanceFundNode.BRANCH_CONFIRM.getValue(), operator);
                }
            }
        } else {
            // 审核不通过
            budgetOrder
                .setCurNodeCode(
                    nodeFlowBO
                        .getNodeFlowByCurrentNode(
                            EBudgetOrderNode.SECOND_AUDIT.getCode())
                        .getBackNode());
        }
        budgetOrderBO.refreshGlobalManagerApprove(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void canceOrder(String code, String operator, String cancelNote) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        // 之前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        budgetOrder.setCurNodeCode(EBudgetOrderNode.START_NODE.getCode());
        budgetOrderBO.canceOrder(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void bankLoanCommit(String code, Date bankCommitDatetime,
            String bankCommitNote, String operator) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.BANK_LOAN_COMMIT.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是确认提交银行节点，不能操作");
        }

        // 当前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        String nextNodeCode = getNextNodeCode(budgetOrder.getCurNodeCode(),
            EBoolean.YES.getCode());

        budgetOrder.setCurNodeCode(nextNodeCode);
        budgetOrder.setCode(code);
        budgetOrder.setBankCommitDatetime(bankCommitDatetime);
        budgetOrder.setBankCommitNote(bankCommitNote);
        budgetOrder.setOperator(operator);
        budgetOrder.setOperateDatetime(new Date());
        budgetOrderBO.refreshBankLoanCommit(budgetOrder);

        // 日志记录

        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());

        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    public void bankPointPushHasLoanList(XN632144Req req) {
        List<String> codeList = req.getCodeList();
        for (String code : codeList) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            if (!EBudgetOrderNode.BANK_POINT_PUSH_LOAN_LIST.getCode()
                .equals(budgetOrder.getCurNodeCode())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点不是银行驻点推送已放款名单节点，不能操作");
            }
        }
        for (String code : codeList) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            budgetOrder.setHasLoanListPic(req.getHasLoanListPic());
            String preCurNodeCode = budgetOrder.getCurNodeCode();// 当前节点
            NodeFlow nodeFlow = nodeFlowBO
                .getNodeFlowByCurrentNode(preCurNodeCode);
            budgetOrder.setCurNodeCode(nodeFlow.getNextNode());// 当前节点的下一个节点
            budgetOrderBO.bankPointPushHasLoanList(budgetOrder);
            // 日志记录
            EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
                .get(budgetOrder.getCurNodeCode());
            sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
                EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurNodeCode,
                nodeFlow.getNextNode(), currentNode.getValue(),
                req.getOperator());
        }

    }

    @Override
    @Transactional
    public void bankLoanConfirm(XN632141Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());

        if (!EBudgetOrderNode.CONFIRM_RECEIVABLES.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是银行放款确认收款节点，不能操作");
        }
        // 当前节点
        String curNodeCode = budgetOrder.getCurNodeCode();
        NodeFlow nodeFlow = nodeFlowBO.getNodeFlowByCurrentNode(curNodeCode);
        budgetOrder.setCurNodeCode(nodeFlow.getNextNode());
        budgetOrder.setCode(req.getCode());
        budgetOrder
            .setBankFkAmount(StringValidater.toLong(req.getBankFkAmount()));
        budgetOrder.setBankFkDatetime(DateUtil.strToDate(
            req.getBankFkDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
        budgetOrder.setBankReceiptCode(req.getBankReceiptCode());

        budgetOrder.setBankReceiptNumber(req.getBankReceiptNumber());
        budgetOrder.setBankReceiptPdf(req.getBankReceiptPdf());
        budgetOrder.setBankReceiptNote(req.getBankReceiptNote());
        budgetOrder.setOperator(req.getOperator());
        budgetOrder.setOperateDatetime(new Date());

        BankSubbranch data = bankSubbranchBO
            .getBankSubbranch(budgetOrder.getLoanBankCode());
        if ("ICBC" == data.getBankType()) {
            budgetOrder
                .setMakeCardStatus(EMakeCardStatus.PENDING_CARD.getCode());
        } else {
            budgetOrder
                .setMakeCardStatus(EMakeCardStatus.PENDING_RECORD.getCode());
        }
        budgetOrderBO.refreshBankLoanConfirm(budgetOrder);

        /****** 生成还款业务 ******/
        // 检查用户是否已经注册过
        User user = userBO.getUser(budgetOrder.getMobile(),
            EUserKind.Customer.getCode());
        String userId = null;
        if (user == null) {
            // 用户代注册并实名认证
            userId = userBO.doRegisterAndIdentify(budgetOrder.getMobile(),
                budgetOrder.getIdKind(), budgetOrder.getCustomerName(),
                budgetOrder.getIdNo());
            distributeAccount(userId, budgetOrder.getMobile(),
                EUserKind.Customer.getCode());
        } else {
            userId = user.getUserId();
        }

        // 绑定用户银行卡
        String bankcardCode = bankcardBO.bind(userId,
            budgetOrder.getCustomerName(), budgetOrder.getBankCardNumber(),
            budgetOrder.getLoanBankCode(), budgetOrder.getLoanBankName());

        // 自动生成还款业务
        RepayBiz repayBiz = repayBizBO.generateCarLoanRepayBiz(budgetOrder,
            userId, bankcardCode, req.getOperator());

        budgetOrderBO.updateRepayBizCode(budgetOrder.getCode(),
            repayBiz.getCode(), userId);

        // 自动生成还款计划
        repayPlanBO.genereateNewRepayPlan(repayBiz);
        /****** 生成还款业务 ******/

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), curNodeCode,
            nodeFlow.getNextNode(), currentNode.getValue(), req.getOperator());
    }

    @Override
    @Transactional
    public void carPledgeCommit(String code, Date pledgeCommitDatetime,
            String pledgeCommitNote, String operator) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.LOCAL_SUBMIT_BANK.getCode()
            .equals(budgetOrder.getPledgeCurNodeCode())
                && !EBudgetOrderNode.OUT_SUBMIT_BANK.getCode()
                    .equals(budgetOrder.getPledgeCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是车辆抵押确认提交银行节点，不能操作");
        }

        // 当前节点
        String preCurrentNode = budgetOrder.getPledgeCurNodeCode();
        String nextNodeCode = getNextNodeCode(preCurrentNode,
            EBoolean.YES.getCode());

        budgetOrder.setPledgeCurNodeCode(nextNodeCode);
        budgetOrder.setCode(code);
        budgetOrder.setPledgeCommitDatetime(pledgeCommitDatetime);
        budgetOrder.setPledgeCommitNote(pledgeCommitNote);
        budgetOrder.setOperator(operator);
        budgetOrder.setOperateDatetime(new Date());
        budgetOrderBO.refreshCarPledgeCommit(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getPledgeCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void carPledgeConfirm(XN632191Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        String preCurrentNode = budgetOrder.getPledgeCurNodeCode();// 当前抵押流程节点

        if (!EBudgetOrderNode.TODO_LOCAL_PLEDGE_ACHIEVE.getCode()
            .equals(preCurrentNode)
                && !EBudgetOrderNode.TODO_OUT_PLEDGE_ACHIEVE.getCode()
                    .equals(preCurrentNode)) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是车辆抵押流程待提交抵押完成节点，不能操作");
        }
        if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点处于物流传递中，不能操作");
        }

        if (EBudgetOrderNode.TODO_LOCAL_PLEDGE_ACHIEVE.getCode()
            .equals(preCurrentNode)) {
            // 温州本地 抵押完成后预算单入档状态改为待入档
            budgetOrder.setEnterFileStatus(EEnterFileStatus.TODO.getCode());
            budgetOrderBO.updateEnterFileStatus(budgetOrder);
        }

        if (EBudgetOrderNode.TODO_OUT_PLEDGE_ACHIEVE.getCode()
            .equals(preCurrentNode)) {
            // 外地 生成资料传递（分公司寄送抵押材料给总公司）
            NodeFlow nodeFlow = nodeFlowBO
                .getNodeFlowByCurrentNode(preCurrentNode);
            NodeFlow flow = nodeFlowBO
                .getNodeFlowByCurrentNode(nodeFlow.getNextNode());
            logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
                budgetOrder.getCode(), budgetOrder.getSaleUserId(),
                nodeFlow.getNextNode(), flow.getNextNode());
            // 产生物流单后改变状态为物流传递中
            budgetOrder.setIsLogistics(EBoolean.YES.getCode());
            budgetOrderBO.updateIsLogistics(budgetOrder);
        }
        // 更改预算单车辆抵押流程节点
        String nextNodeCode = getNextNodeCode(preCurrentNode,
            EBoolean.YES.getCode());
        budgetOrder.setPledgeCurNodeCode(nextNodeCode);
        budgetOrder.setCode(req.getCode());
        budgetOrder.setOperator(req.getOperator());
        budgetOrder.setOperateDatetime(new Date());
        budgetOrder.setCarNumber(req.getCarNumber());
        budgetOrder.setGreenBigCode(req.getGreenBigCode());
        budgetOrder.setGreenBigSmj(req.getGreenBigSmj());
        SYSUser user = sysUserBO.getUser(req.getOperator());
        budgetOrder.setOperateDepartment(user.getDepartmentCode());
        budgetOrderBO.refreshCarPledgeConfirm(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), req.getOperator());
    }

    // 逻辑
    // 1、前提条件判断
    // 2、没完善可再次补充，完善则入档完成
    @Override
    @Transactional
    public void carLoanArchive(XN632200Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        if (!EEnterFileStatus.TODO.getCode()
            .equals(budgetOrder.getEnterFileStatus())
                && !EEnterFileStatus.TODO_MAKEUP.getCode()
                    .equals(budgetOrder.getEnterFileStatus())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "预算单入档状态不是待入档或待补录，不能操作");
        }

        if (EBoolean.YES.getCode().equals(req.getIsComplete())
                && null == req.getStorePlace()) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "存放位置不能为空！");
        }

        if (StringUtils.isNotBlank(req.getEmergencyMobile1())) {
            PhoneUtil.checkMobile(req.getEmergencyMobile1());
        }
        if (StringUtils.isNotBlank(req.getEmergencyMobile2())) {
            PhoneUtil.checkMobile(req.getEmergencyMobile2());
        }
        if (StringUtils.isNotBlank(req.getGuarantor1Mobile())) {
            PhoneUtil.checkMobile(req.getGuarantor1Mobile());
        }
        if (StringUtils.isNotBlank(req.getGuarantor2Mobile())) {
            PhoneUtil.checkMobile(req.getGuarantor2Mobile());
        }
        budgetOrder.setInsuranceCompanyCode(req.getInsuranceCompanyCode());
        budgetOrder.setCarColor(req.getCarColor());
        budgetOrder.setCarBrand(req.getCarBrand());

        budgetOrder.setEngineNo(req.getEngineNo());

        budgetOrder.setForceInsurance(req.getForceInsurance());

        budgetOrder.setCommerceInsurance(req.getCommerceInsurance());
        budgetOrder.setInsuranceEffectDatetime(
            DateUtil.strToDate(req.getInsuranceEffectDatetime(),
                DateUtil.FRONT_DATE_FORMAT_STRING));

        budgetOrder.setInsuranceBank(req.getInsuranceBank());
        budgetOrder.setSaleUserId(req.getSaleUserId());
        budgetOrder.setBankContractCode(req.getBankContractCode());
        budgetOrder.setRegCertificateCode(req.getRegCertificateCode());

        budgetOrder.setGuarantor1Name(req.getGuarantor1Name());
        budgetOrder.setGuarantor1Mobile(req.getGuarantor1Mobile());
        budgetOrder.setGuarantor2Name(req.getGuarantor2Name());
        budgetOrder.setGuarantor2Mobile(req.getGuarantor2Mobile());
        budgetOrder.setBankCardNumber(req.getBankCardNumber());
        budgetOrder
            .setBillDatetime(StringValidater.toInteger(req.getBillDatetime()));

        if (EBoolean.YES.getCode().equals(req.getIsComplete())) {
            budgetOrder.setEnterFileStatus(EEnterFileStatus.ACHIEVE.getCode());
        } else {
            budgetOrder
                .setEnterFileStatus(EEnterFileStatus.TODO_MAKEUP.getCode());
        }
        budgetOrder
            .setMonthAmount(StringValidater.toLong(req.getRepayMonthAmount()));
        budgetOrder.setRepayBankDate(
            StringValidater.toInteger(req.getRepayBankDate()));
        budgetOrder.setRepayFirstMonthAmount(
            StringValidater.toLong(req.getRepayFirstMonthAmount()));
        budgetOrder.setRepayFirstMonthDatetime(
            DateUtil.strToDate(req.getRepayFirstMonthDatetime(),
                DateUtil.FRONT_DATE_FORMAT_STRING));
        budgetOrder.setRepayMonthAmount(
            StringValidater.toLong(req.getRepayMonthAmount()));

        budgetOrder.setIsComplete(req.getIsComplete());
        budgetOrder.setStorePlace(req.getStorePlace());
        budgetOrder.setFileRemark(req.getFileRemark());

        budgetOrder.setOperator(req.getOperator());
        budgetOrder.setOperateDatetime(new Date());
        SYSUser sysUser = sysUserBO.getUser(req.getOperator());
        budgetOrder.setOperateDepartment(sysUser.getDepartmentCode());

        if (CollectionUtils.isNotEmpty(req.getFileList())) {
            StringBuilder fileListBuilder = new StringBuilder();
            for (String file : req.getFileList()) {
                fileListBuilder.append(file).append(",");
            }
            String string = fileListBuilder.toString();
            String fileList = string.substring(0, string.length() - 1);
            budgetOrder.setFileList(fileList);
        }

        budgetOrderBO.refreshCarLoanArchive(budgetOrder);
        // 更新还款业务
        repayBizBO.refreshRepayBiz(budgetOrder);
        // 更新还款计划
        repayPlanBO.refreshRepayPlan(budgetOrder);
    }

    // 分配账号
    private void distributeAccount(String userId, String mobile, String kind) {
        List<String> currencyList = new ArrayList<String>();
        currencyList.add(ECurrency.CNY.getCode());
        currencyList.add(ECurrency.JF.getCode());

        for (String currency : currencyList) {
            accountBO.distributeAccount(userId, mobile,
                EAccountType.getAccountType(kind), currency);
        }
    }

    // 获取下一个节点
    public String getNextNodeCode(String curNodeCode, String approveResult) {
        NodeFlow nodeFolw = nodeFlowBO.getNodeFlowByCurrentNode(curNodeCode);
        String nextNodeCode = null;
        if (EBoolean.YES.getCode().equals(approveResult)) {
            nextNodeCode = nodeFolw.getNextNode();
        } else if (EBoolean.NO.getCode().equals(approveResult)) {
            nextNodeCode = nodeFolw.getBackNode();
        }
        return nextNodeCode;
    }

    @Override
    public Paginable<BudgetOrder> queryBudgetOrderPage(int start, int limit,
            BudgetOrder condition) {
        Paginable<BudgetOrder> page = budgetOrderBO.getPaginable(start, limit,
            condition);
        List<BudgetOrder> list = page.getList();
        for (BudgetOrder budgetOrder : list) {
            initBudget(budgetOrder);
        }
        return page;
    }

    @Override
    public List<BudgetOrder> queryBudgetOrderList(BudgetOrder condition) {
        List<BudgetOrder> list = budgetOrderBO.queryBudgetOrderList(condition);

        for (BudgetOrder budgetOrder : list) {
            initBudget(budgetOrder);
        }

        return list;
    }

    @Override
    public BudgetOrder getBudgetOrder(String code) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        initBudget(budgetOrder);

        // 获取我司贷款成数区间
        LoanCs loanCsCondition = new LoanCs();
        loanCsCondition.setType(budgetOrder.getShopWay());
        List<LoanCs> loanCsList = loanCsBO.queryLoanCsList(loanCsCondition);
        if (CollectionUtils.isNotEmpty(loanCsList)) {
            LoanCs resultCs = loanCsList.get(0);
            budgetOrder.setCompanyLoanCsSection(
                resultCs.getMinCs() + "-" + resultCs.getMaxCs());
        }
        return budgetOrder;
    }

    private void initBudget(BudgetOrder budgetOrder) {
        String file = budgetOrder.getFileList();
        if (StringUtils.isNotBlank(file)) {
            String[] fileArray = file.split(",");
            List<String> fileList = new ArrayList<String>();
            for (String data : fileArray) {
                fileList.add(data);
            }
            budgetOrder.setFileListArray(fileList);
        }

        if (StringUtils.isNotBlank(budgetOrder.getBankReceiptCode())) {
            CollectBankcard receiptBank = collectBankcardBO
                .getCollectBankcard(budgetOrder.getBankReceiptCode());
            if (null != receiptBank) {
                budgetOrder.setBankReceiptName(receiptBank.getBankName());
            }
        }

        if (StringUtils.isNotBlank(budgetOrder.getCarDealerCode())) {
            CarDealer carDealer = carDealerBO
                .getCarDealer(budgetOrder.getCarDealerCode());

            budgetOrder.setCarDealerName(carDealer.getFullName());
            budgetOrder.setCarDealerPhone(carDealer.getContactPhone());
        }

        if (StringUtils.isNotBlank(budgetOrder.getInsuranceCompanyCode())) {
            InsuranceCompany insuranceCompany = insuranceCompanyBO
                .getInsuranceCompany(budgetOrder.getInsuranceCompanyCode());
            budgetOrder.setInsuranceCompanyName(insuranceCompany.getName());
        }

        if (StringUtils.isNotBlank(budgetOrder.getLoanBankCode())) {
            Bank loanBank = bankBO
                .getBankBySubbranch(budgetOrder.getLoanBankCode());
            budgetOrder.setLoanBankName(loanBank.getBankName());
        }

        if (StringUtils.isNotBlank(budgetOrder.getLoanBankCode())) {
            BankSubbranch subbranch = bankSubbranchBO
                .getBankSubbranch(budgetOrder.getLoanBankCode());

            budgetOrder.setBankSubbranch(subbranch);
        }

        if (StringUtils.isNotBlank(budgetOrder.getOperateDepartment())) {
            Department department = departmentBO
                .getDepartment(budgetOrder.getOperateDepartment());

            budgetOrder.setOperateDepartmentName(department.getName());
        }

        // 业务公司名称
        if (StringUtils.isNotBlank(budgetOrder.getCompanyCode())) {
            Department company = departmentBO
                .getDepartment(budgetOrder.getCompanyCode());
            budgetOrder.setCompanyName(company.getName());
        }

        if (StringUtils.isNotBlank(budgetOrder.getOperator())) {
            SYSUser user = sysUserBO.getUser(budgetOrder.getOperator());
            budgetOrder.setOperatorName(user.getRealName());
        }

        if (StringUtils.isNotBlank(budgetOrder.getSaleUserId())) {
            SYSUser saleUser = sysUserBO.getUser(budgetOrder.getSaleUserId());
            budgetOrder.setSaleUserName(saleUser.getRealName());
        }
        List<BudgetOrderGps> budgetOrderGpsList = budgetOrderGpsBO
            .queryBudgetOrderGpsList(budgetOrder.getCode());
        if (CollectionUtils.isNotEmpty(budgetOrderGpsList)) {
            budgetOrder.setBudgetOrderGpsList(budgetOrderGpsList);
        }

        if (StringUtils.isNotBlank(budgetOrder.getCreditCode())) {
            Credit credit = creditBO.getCredit(budgetOrder.getCreditCode());
            budgetOrder.setCredit(credit);
        }

        if (StringUtils.isNotBlank(budgetOrder.getGuarantor1IdNo())) {
            if (budgetOrder.getGuarantor1IdNo().length() != 18) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "担保人1身份证号不合法！");
            }
            String sex = getSexByIdNo(budgetOrder.getGuarantor1IdNo());
            budgetOrder.setGuarantor1Sex(sex);
        }

        if (StringUtils.isNotBlank(budgetOrder.getGuarantor2IdNo())) {
            if (budgetOrder.getGuarantor1IdNo().length() != 18) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "担保人2身份证号不合法！");
            }
            String sex = getSexByIdNo(budgetOrder.getGuarantor2IdNo());
            budgetOrder.setGuarantor2Sex(sex);
        }

        if (StringUtils.isNotBlank(budgetOrder.getRepayBizCode())) {
            List<RepayPlan> planList = repayPlanBO
                .queryRepayPlanListByRepayBizCode(
                    budgetOrder.getRepayBizCode());
            budgetOrder.setRepayPlansList(planList);
        }
        // 担保打印人
        if (StringUtils.isNotBlank(budgetOrder.getGuarantPrintUser())) {
            SYSUser user = sysUserBO.getUser(budgetOrder.getGuarantPrintUser());
            budgetOrder.setGuarantPrintName(user.getRealName());
        }

        // 获取返点列表
        /*
         * List<RepointDetail> shouldBackRepointList = repointDetailBO
         * .queryRepointDetailList(budgetOrder.getCode(),
         * ERepointDetailUseMoneyPurpose.SHOULD_BACK.getCode());
         */
        ArrayList<RepointDetail> shouldBackRepointList = new ArrayList<RepointDetail>();
        RepointDetail mortgageRepointDetail = new RepointDetail();
        mortgageRepointDetail
            .setUseMoneyPurpose(EUseMoneyPurpose.MORTGAGE.getCode());
        mortgageRepointDetail
            .setRepointAmount(budgetOrder.getShouldBackAmount());
        if (EIsAdvanceFund.YES.getCode()
            .equals(budgetOrder.getIsAdvanceFund())) {
            // 垫资
            CollectBankcard condition = new CollectBankcard();
            condition.setCompanyCode(budgetOrder.getCarDealerCode());
            condition.setType(ECollectBankcardType.DEALER_COLLECT.getCode());
            List<CollectBankcard> list = collectBankcardBO
                .queryCollectBankcardByCompanyCodeAndType(condition);
            CollectBankcard collectBankcard = list.get(0);
            CarDealer carDealer = carDealerBO
                .getCarDealer(budgetOrder.getCarDealerCode());
            mortgageRepointDetail.setCarDealerName(carDealer.getFullName());
            mortgageRepointDetail.setAccountName(collectBankcard.getRealName());
            mortgageRepointDetail
                .setAccountNo(collectBankcard.getBankcardNumber());
            mortgageRepointDetail
                .setOpenBankName(collectBankcard.getSubbranch());
        } else {
            // 不垫资
            mortgageRepointDetail
                .setCarDealerName(budgetOrder.getShouldBackUserName());
            mortgageRepointDetail
                .setAccountName(budgetOrder.getShouldBackAccountName());
            mortgageRepointDetail
                .setAccountNo(budgetOrder.getShouldBackAccountNo());
            mortgageRepointDetail
                .setOpenBankName(budgetOrder.getShouldBackOpenBankName());
        }
        shouldBackRepointList.add(mortgageRepointDetail);
        budgetOrder.setRepointDetailList1(shouldBackRepointList);

        List<RepointDetail> proInRepointList = repointDetailBO
            .queryRepointDetailList(budgetOrder.getCode(),
                ERepointDetailUseMoneyPurpose.PROIN_REPOINT.getCode());
        budgetOrder.setRepointDetailList2(proInRepointList);

        List<RepointDetail> proOutRepointList = repointDetailBO
            .queryRepointDetailList(budgetOrder.getCode(),
                ERepointDetailUseMoneyPurpose.PROOUT_REPOINT.getCode());
        budgetOrder.setRepointDetailList3(proOutRepointList);
    }

    private String getSexByIdNo(String idNo) {
        /**
         * 根据身份编号获取性别
         * @param idCard 身份编号
         * @return 性别(M-男，F-女，N-未知)
         */
        String sGender = null;

        String sCardNum = idNo.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "男";
        } else {
            sGender = "女";
        }
        return sGender;
    }

    @Override
    public Paginable<BudgetOrder> queryBudgetOrderPageByRoleCode(int start,
            int limit, BudgetOrder condition) {
        Paginable<BudgetOrder> page = budgetOrderBO
            .getPaginableByRoleCode(start, limit, condition);
        List<BudgetOrder> list = page.getList();
        for (BudgetOrder budgetOrder : list) {
            initBudget(budgetOrder);
        }
        return page;
    }

    @Override
    @Transactional
    public void approveMakeCard(String code, String makeCardRemark,
            String operator) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        if (!EMakeCardStatus.PENDING_CARD.getCode()
            .equals(budgetOrder.getMakeCardStatus())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前状态不是待制卡状态，不能操作！");
        }
        BankSubbranch data = bankSubbranchBO
            .getBankSubbranch(budgetOrder.getLoanBankCode());
        if ("ICBC" != data.getBankType()) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "银行行别不是工行，不能操作！");
        }
        budgetOrder
            .setMakeCardStatus(EMakeCardStatus.ALREADY_MADE_CARD.getCode());
        budgetOrder.setMakeCardRemark(makeCardRemark);
        budgetOrder.setMakeCardOperator(operator);
        budgetOrderBO.approveMakeCard(budgetOrder);
    }

    @Override
    @Transactional
    public void cardMaking(String code, String bankCardNumber,
            String makeCardRemark) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        if (!EMakeCardStatus.PENDING_RECORD.getCode()
            .equals(budgetOrder.getMakeCardStatus())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前状态不是待回录状态，不能操作！");
        }

        BankSubbranch data = bankSubbranchBO
            .getBankSubbranch(budgetOrder.getLoanBankCode());
        if ("ICBC" == data.getBankType()) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "银行行别是工行，不能操作！");
        }
        budgetOrder.setMakeCardStatus(EMakeCardStatus.BACK_RECORD.getCode());
        budgetOrder.setBankCardNumber(bankCardNumber);
        budgetOrder.setMakeCardRemark(makeCardRemark);
        budgetOrderBO.refreshCardMaking(budgetOrder);
    }

    @Override
    @Transactional
    public void entryPreservation(XN632220Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        budgetOrder.setDeliveryDatetime(DateUtil.strToDate(
            req.getDeliveryDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
        Long loanAmount = budgetOrder.getLoanAmount();
        Long currentInvoicePrice = StringValidater
            .toLong(req.getCurrentInvoicePrice());

        // 新我司贷款成数
        double companyLoanCs = AmountUtil.div(loanAmount, currentInvoicePrice);

        // 判断现发票价格和发票价格是否匹配

        if (currentInvoicePrice.longValue() == budgetOrder.getInvoicePrice()
            .longValue()) {
            // 匹配
            budgetOrder.setIsRightInvoice(EBoolean.YES.getCode());

        } else {
            // 不匹配
            budgetOrder.setIsRightInvoice(EBoolean.NO.getCode());
            // 获取我司贷款成数区间 标准
            LoanCs loanCsCondition = new LoanCs();
            loanCsCondition.setType(budgetOrder.getShopWay());
            List<LoanCs> loanCsList = loanCsBO.queryLoanCsList(loanCsCondition);
            LoanCs resultCs = null;
            if (CollectionUtils.isNotEmpty(loanCsList)) {
                resultCs = loanCsList.get(0);
            }
            if (null != resultCs) {
                // 判断新我司贷款成数是否在标准内
                if (companyLoanCs >= resultCs.getMaxCs()
                        || companyLoanCs <= resultCs.getMinCs()) {
                    // 不在我司准入贷款成数标准内 进入发票不匹配流程
                    budgetOrder.setCancelNodeCode(budgetOrder.getCurNodeCode());
                    budgetOrder
                        .setFrozenStatus(EBudgetFrozenStatus.FROZEN.getCode());
                    budgetOrder.setCurNodeCode(
                        EBudgetOrderNode.INVOICE_MISMATCH_APPLY.getCode());
                    EBudgetOrderNode node = EBudgetOrderNode.getMap()
                        .get(budgetOrder.getCurNodeCode());
                    sysBizLogBO.saveSYSBizLog(budgetOrder.getCode(),
                        EBizLogType.INVOICE_MISMATCH, budgetOrder.getCode(),
                        node.getCode(), node.getValue(), req.getOperator());
                    budgetOrderBO.invoiceMismatchApply(budgetOrder);
                }
            }
        }

        budgetOrder.setCurrentInvoicePrice(
            StringValidater.toLong(req.getCurrentInvoicePrice()));
        // 原贷款成数
        budgetOrder.setPreCompanyLoanCs(budgetOrder.getCompanyLoanCs());
        // 新贷款成数
        budgetOrder.setCompanyLoanCs(companyLoanCs);
        budgetOrder.setInvoice(req.getInvoice());
        budgetOrder.setCertification(req.getCertification());

        budgetOrder.setForceInsurance(req.getForceInsurance());
        budgetOrder.setBusinessInsurance(req.getBusinessInsurance());
        budgetOrder.setMotorRegCertification(req.getMotorRegCertification());
        budgetOrder.setPdPdf(req.getPdPdf());
        budgetOrder.setFbhRemark(req.getFbhRemark());
        budgetOrder.setFbhStatus(EFbhStatus.TO_PENDING_ENTRY.getCode());
        budgetOrderBO.entryPreservation(budgetOrder);
    }

    @Override
    @Transactional
    public void invoiceMismatchApply(XN632230Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        if (!EBudgetOrderNode.INVOICE_MISMATCH_APPLY.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是发票不匹配申请节点，不能操作！");
        }
        XN632234Res res = modifyLoanAmountCalculateData(req.getCode(),
            req.getLoanAmount());

        // 1个贷款金额 3个贷款成数 6个费用1个应退按揭款
        budgetOrder.setPreLoanAmount(budgetOrder.getLoanAmount());
        budgetOrder.setLoanAmount(StringValidater.toLong(req.getLoanAmount()));
        budgetOrder.setPreCompanyLoanCs(budgetOrder.getCompanyLoanCs());
        budgetOrder
            .setCompanyLoanCs(StringValidater.toDouble(res.getCompanyLoanCs()));
        budgetOrder.setPreBankLoanCs(budgetOrder.getBankLoanCs());
        budgetOrder
            .setBankLoanCs(StringValidater.toDouble(res.getBankLoanCs()));
        budgetOrder.setPreGlobalRate(budgetOrder.getGlobalRate());
        budgetOrder
            .setGlobalRate(StringValidater.toDouble(res.getGlobalRate()));
        if (null != res.getFxAmount() && !"".equals(res.getFxAmount())) {
            budgetOrder.setPreFxAmount(budgetOrder.getFxAmount());
            budgetOrder.setFxAmount(StringValidater.toLong(res.getFxAmount()));
        }
        if (null != res.getLyAmount() && !"".equals(res.getLyAmount())) {
            budgetOrder.setPreLyAmount(budgetOrder.getLyAmount());
            budgetOrder.setLyAmount(StringValidater.toLong(res.getLyAmount()));
        }
        if (null != res.getGpsFee() && !"".equals(res.getGpsFee())) {
            budgetOrder.setPreGpsFee(budgetOrder.getGpsFee());
            budgetOrder.setGpsFee(StringValidater.toLong(res.getGpsFee()));
        }
        if (null != res.getOtherFee() && !"".equals(res.getOtherFee())) {
            budgetOrder.setPreOtherFee(budgetOrder.getOtherFee());
            budgetOrder.setOtherFee(StringValidater.toLong(res.getOtherFee()));
        }
        budgetOrder.setPreGpsDeduct(budgetOrder.getGpsDeduct());
        budgetOrder.setGpsDeduct(StringValidater.toLong(res.getGpsDeduct()));
        budgetOrder.setPreOilSubsidy(budgetOrder.getOilSubsidy());
        budgetOrder.setOilSubsidy(StringValidater.toLong(res.getOilSubsidy()));
        budgetOrder.setPreShouldBackAmount(budgetOrder.getShouldBackAmount());
        // 生成新返点明细数据
        List<XN632290Res> list = res.getList();
        for (XN632290Res xn632290Res : list) {
            if (EUseMoneyPurpose.MORTGAGE.getCode().equals(// 应退按揭款 垫资
                xn632290Res.getUseMoneyPurpose())) {
                budgetOrder
                    .setPreShouldBackAmount(budgetOrder.getShouldBackAmount());// 原来的应退按揭款
                budgetOrder.setShouldBackAmount(
                    StringValidater.toLong(xn632290Res.getRepointAmount()));// 新应退按揭款
            }
            if (EUseMoneyPurpose.PROTOCOL_INNER.getCode()
                .equals(xn632290Res.getUseMoneyPurpose())) {// 协议内返点
                Department company = departmentBO
                    .getDepartment(budgetOrder.getCompanyCode());
                CreditUser user = creditUserBO.getCreditUserByCreditCode(
                    budgetOrder.getCreditCode(), ELoanRole.APPLY_USER);
                CarDealer carDealer = carDealerBO
                    .getCarDealer(budgetOrder.getCarDealerCode());
                RepointDetail repointDetail = new RepointDetail();
                repointDetail.setType(ERepointDetailType.NEW.getCode());
                repointDetail.setCompanyCode(budgetOrder.getCompanyCode());
                repointDetail.setCompanyName(company.getName());
                repointDetail.setBudgetCode(budgetOrder.getCode());
                repointDetail.setUserName(budgetOrder.getCustomerName());

                repointDetail.setIdNo(user.getIdNo());
                repointDetail.setCarDealerCode(budgetOrder.getCarDealerCode());
                repointDetail.setCarDealerName(carDealer.getFullName());
                repointDetail.setCarType(budgetOrder.getCarModel());
                repointDetail.setLoanAmount(budgetOrder.getLoanAmount());

                repointDetail.setBankRate(budgetOrder.getGlobalRate());
                repointDetail.setBenchmarkRate(
                    StringValidater.toDouble(xn632290Res.getBenchmarkRate()));
                repointDetail.setFee(budgetOrder.getFee());
                repointDetail.setUseMoneyPurpose(
                    EUseMoneyPurpose.PROTOCOL_INNER.getCode());
                repointDetail.setRepointAmount(
                    StringValidater.toLong(xn632290Res.getRepointAmount()));
                repointDetail.setAccountCode(xn632290Res.getAccountCode());
                repointDetail.setCurNodeCode(
                    ERepointDetailStatus.TODO_MAKE_BILL.getCode());
                repointDetailBO.saveRepointDetail(repointDetail);
            }
        }
        // 当前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        // 下个节点
        String nextNode = nodeFlowBO
            .getNodeFlowByCurrentNode(
                EBudgetOrderNode.INVOICE_MISMATCH_APPLY.getCode())
            .getNextNode();
        budgetOrder.setCurNodeCode(nextNode);
        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), req.getOperator());
        budgetOrderBO.applyInvoiceMismatch(budgetOrder);

        // 协议外返点 不重新计算 更改原返点数据的状态为发票不匹配产生的新数据 为了审核过后批量处理数据
        List<RepointDetail> repointDetailList = repointDetailBO
            .queryRepointDetailList(budgetOrder.getCode(),
                EUseMoneyPurpose.PROTOCOL_OUTER.getCode());
        for (RepointDetail outRepointDetail : repointDetailList) {
            outRepointDetail.setType(ERepointDetailType.NEW.getCode());
            repointDetailBO.updateRepointDetailType(outRepointDetail);
        }
    }

    @Override
    @Transactional
    public void invoiceMismatchApprove(String code, String approveResult,
            String approveNote, String operator) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.APPROVE_APPLY.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是发票不匹配审核节点，不能操作");
        }

        // 之前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        if (EApproveResult.PASS.getCode().equals(approveResult)) {
            budgetOrder
                .setCurNodeCode(nodeFlowBO
                    .getNodeFlowByCurrentNode(
                        EBudgetOrderNode.APPROVE_APPLY.getCode())
                    .getNextNode());
        } else {

            budgetOrder
                .setCurNodeCode(nodeFlowBO
                    .getNodeFlowByCurrentNode(
                        EBudgetOrderNode.APPROVE_APPLY.getCode())
                    .getBackNode());
            // 审核不通过
            // 1.删除1个贷款金额3个贷款成数和6个费用的新数据 还原共10项原数据
            budgetOrder.setLoanAmount(budgetOrder.getPreLoanAmount());
            budgetOrder.setPreLoanAmount(null);

            budgetOrder.setCompanyLoanCs(budgetOrder.getPreCompanyLoanCs());
            budgetOrder.setPreCompanyLoanCs(0);
            budgetOrder.setBankLoanCs(budgetOrder.getPreBankLoanCs());
            budgetOrder.setPreBankLoanCs(0);
            budgetOrder.setGlobalRate(budgetOrder.getPreGlobalRate());
            budgetOrder.setPreGlobalRate(0);

            budgetOrder.setFxAmount(budgetOrder.getPreFxAmount());
            budgetOrder.setPreFxAmount(null);
            budgetOrder.setLyAmount(budgetOrder.getPreLyAmount());
            budgetOrder.setPreLyAmount(null);
            budgetOrder.setGpsFee(budgetOrder.getPreGpsFee());
            budgetOrder.setPreGpsFee(null);
            budgetOrder.setOtherFee(budgetOrder.getPreOtherFee());
            budgetOrder.setPreOtherFee(null);
            budgetOrder.setOilSubsidy(budgetOrder.getPreOilSubsidy());
            budgetOrder.setPreOilSubsidy(null);
            budgetOrder.setGpsDeduct(budgetOrder.getPreGpsDeduct());
            budgetOrder.setPreGpsDeduct(null);

            // 2.将原来的手续费设置为失效
            budgetOrderFeeBO.refreshBudgetOrderNoEffect(budgetOrder.getCode());

            // 3.删除新返点数据和原返点数据
            repointDetailBO.delete(budgetOrder.getCode());
        }

        budgetOrderBO.invoiceMismatchApprove(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void invoiceMismatchSecondApprove(String code, String approveResult,
            String approveNote, String operator) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);

        if (!EBudgetOrderNode.TWO_APPROVE_APPLY.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是发票不匹配二审节点，不能操作");
        }
        // 之前节点
        String preCurrentNode = budgetOrder.getCurNodeCode();
        if (EApproveResult.PASS.getCode().equals(approveResult)) {
            // 二审通过
            budgetOrder.setCurNodeCode(nodeFlowBO
                .getNodeFlowByCurrentNode(
                    EBudgetOrderNode.TWO_APPROVE_APPLY.getCode())
                .getNextNode());
            // 计算出新应收手续费总额并且更新应收总额 履约保证金+担保风险金+GPS收费+杂费
            if (EBudgetOrderFeeWay.TRANSFER.getCode()
                .equals(budgetOrder.getServiceChargeWay())) {
                Long totalFee = budgetOrder.getFee() + budgetOrder.getLyAmount()
                        + budgetOrder.getFxAmount() + budgetOrder.getGpsFee()
                        + budgetOrder.getOtherFee();
                BudgetOrderFee budgetOrderFee = new BudgetOrderFee();
                budgetOrderFee.setShouldAmount(totalFee);

                BudgetOrderFee condition = new BudgetOrderFee();
                List<BudgetOrderFee> list = budgetOrderFeeBO
                    .queryBudgetOrderFeeList(condition);
                BudgetOrderFee preBudgetOrderFee = list.get(0);

                if (totalFee > preBudgetOrderFee.getShouldAmount()) {
                    budgetOrderFee.setIsSettled(EBoolean.NO.getCode());
                } else {
                    budgetOrderFee
                        .setIsSettled(preBudgetOrderFee.getIsSettled());
                }

                budgetOrderFeeBO.updateShouldAmountAndIsSettled(budgetOrderFee);
            }
            // TODO 手续费收取方式是按揭款扣 在退按揭款时使用新计算出的手续费

            // 删除原返点数据
            repointDetailBO.deletePreRepointDetail(budgetOrder.getCode(),
                ERepointDetailType.NORMAL.getCode());

            budgetOrderBO.twoApproveNo(budgetOrder);// TODO 暂用这个改节点

            // budgetOrderBO.twoApproveYes(budgetOrder);

        } else {

            // 二审不通过
            budgetOrder.setCurNodeCode(nodeFlowBO
                .getNodeFlowByCurrentNode(
                    EBudgetOrderNode.TWO_APPROVE_APPLY.getCode())
                .getBackNode());
            // 1.删除1个贷款金额3个贷款成数和6个费用的新数据 还原共10项原数据
            budgetOrder.setLoanAmount(budgetOrder.getPreLoanAmount());
            budgetOrder.setPreLoanAmount(null);

            budgetOrder.setCompanyLoanCs(budgetOrder.getPreCompanyLoanCs());
            budgetOrder.setPreCompanyLoanCs(0);
            budgetOrder.setBankLoanCs(budgetOrder.getPreBankLoanCs());
            budgetOrder.setPreBankLoanCs(0);
            budgetOrder.setGlobalRate(budgetOrder.getPreGlobalRate());
            budgetOrder.setPreGlobalRate(0);

            budgetOrder.setFxAmount(budgetOrder.getPreFxAmount());
            budgetOrder.setPreFxAmount(null);
            budgetOrder.setLyAmount(budgetOrder.getPreLyAmount());
            budgetOrder.setPreLyAmount(null);
            budgetOrder.setGpsFee(budgetOrder.getPreGpsFee());
            budgetOrder.setPreGpsFee(null);
            budgetOrder.setOtherFee(budgetOrder.getPreOtherFee());
            budgetOrder.setPreOtherFee(null);
            budgetOrder.setOilSubsidy(budgetOrder.getPreOilSubsidy());
            budgetOrder.setPreOilSubsidy(null);
            budgetOrder.setGpsDeduct(budgetOrder.getPreGpsDeduct());
            budgetOrder.setPreGpsDeduct(null);
            budgetOrderBO.invoiceMismatchApprove(budgetOrder);// 更新数据和更新节点

            // 2.将原来的手续费设置为失效
            budgetOrderFeeBO.refreshBudgetOrderNoEffect(budgetOrder.getCode());

            // 3.删除新返点数据和原返点数据
            repointDetailBO.delete(budgetOrder.getCode());

            // budgetOrderBO.twoApproveNo(budgetOrder);

        }

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), operator);
    }

    @Override
    @Transactional
    public void mortgageRefund(String code, String shouldBackBankcardCode,
            String shouldBackDatetime, String shouldBackBillPdf) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        Long loanAmount = budgetOrder.getLoanAmount();// 贷款金额
        long fee = 0;
        if (EServiceChargeWay.MORTGAGE.getCode()
            .equals(budgetOrder.getServiceChargeWay())) {
            fee = budgetOrder.getFee();
        }
        long gpsFee = 0;
        if (EGpsFeeWay.MORTGAGE.getCode().equals(budgetOrder.getGpsFeeWay())) {
            gpsFee = budgetOrder.getGpsFee();
        }
        long carDealerSubsidy = 0;
        if (null != budgetOrder.getCarDealerSubsidy()) {
            carDealerSubsidy = budgetOrder.getCarDealerSubsidy();
        }
        // 应退按揭款合计 = 贷款金额 - 收客户手续费（按揭款扣）- GPS费（按揭款扣）- 厂家贴息
        Long shouldBackAmount = loanAmount - fee - gpsFee - carDealerSubsidy;

        budgetOrder.setShouldBackAmount(shouldBackAmount);
        budgetOrder.setShouldBackBankcardCode(shouldBackBankcardCode);
        budgetOrder.setShouldBackDatetime(DateUtil.strToDate(shouldBackDatetime,
            DateUtil.FRONT_DATE_FORMAT_STRING));
        budgetOrder.setShouldBackBillPdf(shouldBackBillPdf);
        budgetOrderBO.mortgageRefund(budgetOrder);
    }

    @Override
    @Transactional
    public void applyCancel(XN632270Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        String preCurrentNode = budgetOrder.getCurNodeCode();
        budgetOrder.setZfReason(req.getZfReason());
        budgetOrder.setFrozenStatus(EBudgetFrozenStatus.FROZEN.getCode());
        budgetOrder.setCancelNodeCode(budgetOrder.getCurNodeCode());
        budgetOrder.setIsSubmitCancel(EBoolean.YES.getCode());
        // 节点
        EBudgetOrderNode currentNode = EBudgetOrderNode.APPLY_CANCEL;
        String nextNode = nodeFlowBO
            .getNodeFlowByCurrentNode(currentNode.getCode()).getNextNode();
        currentNode = EBudgetOrderNode.getMap().get(nextNode);
        budgetOrder.setCurNodeCode(currentNode.getCode());

        budgetOrderBO.applyCancel(budgetOrder);

        // 写日志
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_CANCEL, budgetOrder.getCode(), preCurrentNode,
            budgetOrder.getCurNodeCode(), null, req.getOperator());
    }

    @Override
    @Transactional
    public void cancelBizAudit(XN632271Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());

        if (!EBudgetOrderNode.APPROVE_CANCEL.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是审核节点，不能操作");
        }
        String preCurrentNode = budgetOrder.getCurNodeCode();
        if (EApproveResult.PASS.getCode().equals(req.getApproveResult())) {
            // 判断是否已垫资 如果已经垫资 下一个节点是财务审核节点 未垫资 下一个节点时废流程结束节点
            if (EIsAdvanceFund.NO.getCode()
                .equals(budgetOrder.getIsAdvanceFund())) {// 没垫资情况
                budgetOrder.setCurNodeCode(
                    EBudgetOrderNode.CANCEL_APPLY_END.getCode());
                budgetOrder
                    .setFrozenStatus(EBudgetFrozenStatus.NORMAL.getCode());
            } else {// 垫资情况
                String currentNode = nodeFlowBO
                    .getNodeFlowByCurrentNode(budgetOrder.getCurNodeCode())
                    .getNextNode();
                budgetOrder.setCurNodeCode(currentNode);
            }

            // 更新gps使用状态为待使用
            budgetOrderGpsBO.removeBudgetOrderGpsList(budgetOrder.getCode());
        } else if (EApproveResult.NOT_PASS.getCode()
            .equals(req.getApproveResult())) {
            budgetOrder.setCurNodeCode(budgetOrder.getCancelNodeCode());
            budgetOrder.setFrozenStatus(EBudgetFrozenStatus.NORMAL.getCode());
        }

        budgetOrderBO.cancelBizAudit(budgetOrder);
        // 写日志
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_CANCEL, budgetOrder.getCode(), preCurrentNode,
            budgetOrder.getCurNodeCode(), req.getApproveNote(),
            req.getOperator());
    }

    @Override
    @Transactional
    public void financeConfirm(XN632272Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());

        if (!EBudgetOrderNode.FINANCE_CONFIRM_RECEIVABLES.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是财务确认收款节点，不能操作");
        }
        String preCurrentNode = budgetOrder.getCurNodeCode();
        budgetOrder.setZfSkAmount(StringValidater.toLong(req.getZfSkAmount()));
        budgetOrder.setZfSkBankcardCode(req.getZfSkBankcardCode());
        budgetOrder.setZfSkReceiptDatetime(DateUtil.strToDate(
            req.getZfSkReceiptDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
        budgetOrder.setZfFinanceRemark(req.getZfFinanceRemark());
        budgetOrder.setCurNodeCode(
            nodeFlowBO.getNodeFlowByCurrentNode(preCurrentNode).getNextNode());
        budgetOrderBO.financeConfirm(budgetOrder);

        // 日志记录
        EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
            .get(budgetOrder.getCurNodeCode());
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurrentNode,
            currentNode.getCode(), currentNode.getValue(), req.getOperator());
    }

    @Override
    @Transactional
    public void receiptAndReturn(XN632280Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        budgetOrder.setZfSkBankcardCode(req.getZfSkBankcardCode());
        budgetOrder.setZfSkAmount(StringValidater.toLong(req.getZfSkAmount()));
        budgetOrder.setZfSkReceiptDatetime(DateUtil.strToDate(
            req.getZfSkReceiptDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
        budgetOrder.setZfFinanceRemark(req.getZfFinanceRemark());
        budgetOrder.setIsSubmitCancel(EBoolean.NO.getCode());
        budgetOrderBO.receiptAndReturn(budgetOrder);
        /*
         * if (ETakeBackAdvanceFundType.CUSTOMER_CANCEL.getCode().equals(
         * req.getType())) { // 客户作废 } if
         * (ETakeBackAdvanceFundType.ADVANCE_FUND_RETURN.getCode().equals(
         * req.getType())) { // 垫资款退回 }
         */
    }

    @Override
    @Transactional
    public void remindingProcess(String code) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        if (!EBoolean.NO.getCode().equals(budgetOrder.getIsSubmitCancel())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "不是未提交作废申请状态，不能操作！");
        }
        budgetOrder.setIsSubmitCancel(EBoolean.YES.getCode());
        budgetOrder.setCurNodeCode(EBudgetOrderNode.TO_APPLY_CANCEL.getCode());
        budgetOrderBO.remindingProcess(budgetOrder);
    }

    @Override
    @Transactional
    public void renewInsuranceRemind(String code) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
        int insuranceRemindCount = budgetOrder.getInsuranceRemindCount() + 1;
        budgetOrderBO.renewInsuranceRemind(code, insuranceRemindCount);

        String mobile = budgetOrder.getMobile();
        String content = "尊敬的" + PhoneUtil.hideMobile(mobile)
                + "用户，您的保险即将到期，请及时续保！";
        smsOutBO.sendSmsOut(mobile, content);
    }

    @Override
    public void renewInsurance(XN632341Req req) {
        BudgetOrder data = new BudgetOrder();
        data.setCode(req.getCode());
        data.setInsuranceCompanyCode(req.getInsuranceCompanyCode());
        data.setInsuranceApplyDatetime(
            DateUtil.strToDate(req.getInsuranceApplyDatetime(),
                DateUtil.FRONT_DATE_FORMAT_STRING));
        data.setInsuranceEndDatetime(DateUtil.strToDate(
            req.getInsuranceEndDatetime(), DateUtil.FRONT_DATE_FORMAT_STRING));
        data.setInsuranceForcePdf(req.getInsuranceForcePdf());

        data.setInsuranceBusinessPdf(req.getInsuranceBusinessPdf());
        data.setInsuranceNote(req.getInsuranceNote());
        data.setOperator(req.getOperator());
        data.setOperateDatetime(new Date());
        budgetOrderBO.renewInsurance(data);
    }

    @Override
    public Paginable<BudgetOrder> queryBudgetOrderPageByDz(int start, int limit,
            BudgetOrder condition) {
        return budgetOrderBO.getPaginable(start, limit, condition);
    }

    @Override
    @Transactional
    public XN632234Res modifyLoanAmountCalculateData(String code,
            String loanAmount) {
        XN632234Res res = new XN632234Res();
        BudgetOrder data = budgetOrderBO.getBudgetOrder(code);
        Long invoicePrice = data.getCurrentInvoicePrice();// 现发票价
        // 我司贷款成数：贷款金额 / 发票价格
        res.setCompanyLoanCs(String.valueOf(
            AmountUtil.div(StringValidater.toLong(loanAmount), invoicePrice)));
        double feeRate = AmountUtil.div(data.getFee(),
            StringValidater.toLong(loanAmount));
        // 综合利率： 服务费/贷款金额+银行利率
        res.setGlobalRate(String.valueOf(feeRate + data.getBankRate()));
        Long totalAmount = StringValidater.toLong(loanAmount) + data.getFee();
        // 银行贷款成数：(贷款金额+服务费) / 发票价格
        res.setBankLoanCs(
            String.valueOf(AmountUtil.div(totalAmount, invoicePrice)));
        // 手续费:履约保证金+担保风险金+GPS收费+杂费
        String carDealerCode = data.getCarDealerCode();
        Bank bank = bankBO.getBankBySubbranch(data.getLoanBankCode());
        EBankType eBankType = null;
        if (EBankType.GH.getCode().equals(bank.getBankCode())) {
            eBankType = EBankType.GH;
        } else if (EBankType.ZH.getCode().equals(bank.getBankCode())) {
            eBankType = EBankType.ZH;
        } else if (EBankType.JH.getCode().equals(bank.getBankCode())) {
            eBankType = EBankType.JH;
        }
        CarDealerProtocol carDealerProtocol = carDealerProtocolBO
            .getCarDealerProtocolByCarDealerCode(carDealerCode,
                eBankType.getCode());
        // 担保风险金
        if (EAssureType.PERCENT.getCode()
            .equals(carDealerProtocol.getAssureType())) {
            res.setFxAmount(String
                .valueOf(AmountUtil.mul(StringValidater.toLong(loanAmount),
                    carDealerProtocol.getAssureRate())));
        }
        // 履约保证金
        if (ELyAmountType.PERCENT.getCode()
            .equals(carDealerProtocol.getLyAmountType())) {
            res.setLyAmount(String
                .valueOf(AmountUtil.mul(StringValidater.toLong(loanAmount),
                    carDealerProtocol.getLyAmountRate())));
        }
        // gps收费
        if (EGpsTypeProtocol.PERCENT.getCode()
            .equals(carDealerProtocol.getGpsType())) {
            res.setGpsFee(String
                .valueOf(AmountUtil.mul(StringValidater.toLong(loanAmount),
                    carDealerProtocol.getGpsRate())));
        }
        // 杂费
        if (EOtherType.PERCENT.getCode()
            .equals(carDealerProtocol.getOtherType())) {
            res.setOtherFee(String
                .valueOf(AmountUtil.mul(StringValidater.toLong(loanAmount),
                    carDealerProtocol.getOtherRate())));
        }
        SYSConfig sysConfigoil = sysConfigBO
            .getSYSConfig(SysConstants.BUDGET_OIL_SUBSIDY_RATE);
        Double oilSubsidyBFB = StringValidater
            .toDouble(sysConfigoil.getCvalue());
        Long oilSubsidy = AmountUtil.mul(StringValidater.toLong(loanAmount),
            oilSubsidyBFB);
        // 油补
        res.setOilSubsidy(String.valueOf(oilSubsidy));
        SYSConfig sysConfig = sysConfigBO
            .getSYSConfig(SysConstants.BUDGET_GPS_DEDUCT_RATE);
        Double gpsBFB = StringValidater.toDouble((sysConfig.getCvalue()));
        Long gpsDeduct = AmountUtil.mul(StringValidater.toLong(loanAmount),
            gpsBFB);
        // gps提成
        res.setGpsDeduct(String.valueOf(gpsDeduct));
        // 协议内返点明细
        RepointDetail repointDetail = repointDetailAO
            .calculateRepointDetail(data);
        ArrayList<XN632290Res> list = new ArrayList<XN632290Res>();
        CarDealer carDealer = carDealerBO.getCarDealer(data.getCarDealerCode());
        XN632290Res innerRepointDetail = new XN632290Res();
        innerRepointDetail
            .setUseMoneyPurpose(EUseMoneyPurpose.PROTOCOL_INNER.getCode());
        innerRepointDetail
            .setRepointAmount(String.valueOf(repointDetail.getRepointAmount()));
        innerRepointDetail.setAccountCode(repointDetail.getAccountCode());
        innerRepointDetail.setCompanyName(carDealer.getFullName());
        CollectBankcard collectBankcard = collectBankcardBO
            .getCollectBankcard(repointDetail.getAccountCode());
        innerRepointDetail
            .setBankcardNumber(collectBankcard.getBankcardNumber());
        innerRepointDetail.setSubbranch(collectBankcard.getBankName());
        innerRepointDetail
            .setBenchmarkRate(String.valueOf(repointDetail.getBenchmarkRate()));
        list.add(innerRepointDetail);
        // 应退按揭款
        calculateShouldBackMorgage(data);// 重新计算
        XN632290Res shouldBackMortgage = new XN632290Res();
        shouldBackMortgage
            .setUseMoneyPurpose(EUseMoneyPurpose.MORTGAGE.getCode());
        shouldBackMortgage
            .setRepointAmount(String.valueOf(data.getShouldBackAmount()));
        if (EIsAdvanceFund.NO.getCode().equals(data.getIsAdvanceFund())) {// 展示给调用接口的前端看的
            // 不垫资 应退按揭款 退给个人 有收款相关信息
            shouldBackMortgage.setCompanyName(data.getShouldBackUserName());
            shouldBackMortgage.setBankcardNumber(data.getShouldBackAccountNo());
            shouldBackMortgage.setSubbranch(data.getShouldBackOpenBankName());
            shouldBackMortgage.setAccountName(data.getShouldBackAccountName());
        }
        res.setList(list);
        return res;
    }

    @Override
    @Transactional
    public BudgetOrder loanContractPrint(XN632142Req req) {
        BudgetOrder budgetOrder = getBudgetOrder(req.getCode());
        if (!EBudgetOrderNode.LOAN_PRINT.getCode()
            .equals(budgetOrder.getCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是打印岗打印节点，不能操作");
        }
        if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点处于物流传递中，不能操作");
        }

        if (EFbhStatus.PENDING_ENTRY.getCode()
            .equals(budgetOrder.getFbhStatus())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "未录入发保合，不能操作");
        }

        // 当前节点
        String curNodeCode = budgetOrder.getCurNodeCode();
        String nextNodeCode = getNextNodeCode(curNodeCode,
            EBoolean.YES.getCode());

        budgetOrder.setPostcode(req.getPostcode());
        budgetOrder.setFamilyPhone(req.getFamilyPhone());
        budgetOrder.setApplyUserCompanyPhone(req.getApplyUserCompanyPhone());
        budgetOrder.setGhMobile(req.getGhMobile());
        budgetOrder.setGhCompanyName(req.getGhCompanyName());
        budgetOrder.setCarBrand(req.getCarBrand());
        budgetOrder.setEngineNo(req.getEngineNo());
        budgetOrder.setGuarantorNowAddress(req.getGuarantorNowAddress());
        budgetOrder.setGuarantorFamilyPhone(req.getGuarantorFamilyPhone());
        budgetOrder.setGuarantorCompanyName(req.getGuarantorCompanyName());
        budgetOrder.setGuarantorCompanyPhone(req.getGuarantorFamilyPhone());
        budgetOrder
            .setGuarantorCompanyAddress(req.getGuarantorCompanyAddress());
        budgetOrder.setInsuranceCompany(req.getInsuranceCompany());
        budgetOrder.setGuarantApplyUserNote(req.getGuarantApplyUserNote());
        budgetOrder.setGuarantPrintTemplateId(req.getGuarantPrintTemplateId());
        budgetOrder.setGuarantPrintUser(req.getOperater());
        budgetOrder.setGuarantPrintDatetime(new Date());
        // budgetOrder.setCurNodeCode(nextNodeCode);
        budgetOrderBO.loanContractPrint(budgetOrder);

        // 生成资料传递
        logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
            budgetOrder.getCode(), budgetOrder.getSaleUserId(), curNodeCode,
            nextNodeCode);
        // 产生物流单后改变状态为物流传递中
        budgetOrder.setIsLogistics(EBoolean.YES.getCode());
        budgetOrderBO.updateIsLogistics(budgetOrder);

        // 写日志
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), curNodeCode,
            nextNodeCode, EBoolean.YES.getCode(), req.getOperater());

        return budgetOrder;
    }

    @Override
    @Transactional
    public BudgetOrder pledgeContractPrint(XN632192Req req) {
        BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(req.getCode());
        if (!EBudgetOrderNode.LOCAL_PRINTPOST_PRINT.getCode()
            .equals(budgetOrder.getPledgeCurNodeCode())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点不是车辆抵押（本地）打印岗打印节点，不能操作");
        }
        if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
            throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                "当前节点处于物流传递中，不能操作");
        }
        // 当前抵押流程节点
        String preNodeCode = budgetOrder.getPledgeCurNodeCode();
        String nextNodeCode = getNextNodeCode(preNodeCode,
            EBoolean.YES.getCode());

        budgetOrder.setFrameNo(req.getFrameNo());
        budgetOrder.setEngineNo(req.getEngineNo());
        budgetOrder.setPledgePrintTemplateId(req.getPledgePrintTemplateId());
        budgetOrder.setPledgePrintUser(req.getOperator());
        budgetOrder.setPledgePrintDatetime(new Date());
        // budgetOrder.setPledgeCurNodeCode(nextNodeCode);
        budgetOrderBO.pledgeContractPrint(budgetOrder);

        // 生成资料传递
        logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
            budgetOrder.getCode(), budgetOrder.getSaleUserId(), preNodeCode,
            nextNodeCode);
        // 产生物流单后改变状态为物流传递中
        budgetOrder.setIsLogistics(EBoolean.YES.getCode());
        budgetOrderBO.updateIsLogistics(budgetOrder);

        // 日志记录
        sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
            EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preNodeCode,
            nextNodeCode, null, req.getOperator());

        return budgetOrder;
    }

    private void editForeignBudgetOrder(BudgetOrder data, XN632120Req req) {

        data.setApplyUserCompany(req.getApplyUserCompany());
        data.setApplyUserDuty(req.getApplyUserDuty());
        data.setCustomerType(req.getCustomerType());
        data.setOutCarDealerName(req.getOutCarDealerName());
        data.setOriginalPrice(StringValidater.toLong(req.getOriginalPrice()));

        data.setCarModel(req.getCarModel());
        data.setFrameNo(req.getFrameNo());
        data.setLoanPeriods(StringValidater.toInteger(req.getLoanPeriods()));
        data.setInvoicePrice(StringValidater.toLong(req.getInvoicePrice()));
        data.setRateType(req.getRateType());

        data.setBankRate(StringValidater.toDouble(req.getBankRate()));
        Long loanAmount = 0L;
        if (null == req.getLoanAmount()) {
            loanAmount = data.getLoanAmount();
        } else {
            loanAmount = StringValidater.toLong(req.getLoanAmount());
            data.setLoanAmount(loanAmount);
        }
        Long invoicePrice = StringValidater.toLong(req.getInvoicePrice());
        data.setCompanyLoanCs(AmountUtil.div(loanAmount, invoicePrice));// 我司贷款成数

        // 获取我司贷款成数区间 标准
        LoanCs loanCsCondition = new LoanCs();
        loanCsCondition.setType(data.getShopWay());
        List<LoanCs> loanCsList = loanCsBO.queryLoanCsList(loanCsCondition);
        LoanCs resultCs = null;
        if (CollectionUtils.isNotEmpty(loanCsList)) {
            resultCs = loanCsList.get(0);
        }
        if (null != resultCs) {
            // 判断新我司贷款成数是否在标准内
            if (data.getCompanyLoanCs() >= resultCs.getMaxCs()
                    || data.getCompanyLoanCs() <= resultCs.getMinCs()) {
                // 不在我司准入贷款成数标准内
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "贷款成数不在我司贷款成数有效范围");
            }
        }

        data.setIsAdvanceFund(req.getIsAdvanceFund());
        Long fee = StringValidater.toLong(req.getFee());
        data.setFee(fee);
        double feeRate = AmountUtil.div(fee, loanAmount);
        data.setGlobalRate(
            feeRate + StringValidater.toDouble(req.getBankRate()));// 综合利率
        data.setCarDealerSubsidy(
            StringValidater.toLong(req.getCarDealerSubsidy()));

        Long totalAmount = loanAmount + fee;// 总费用 贷款总额
        data.setBankLoanCs(AmountUtil.div(totalAmount, invoicePrice));// 银行贷款成数
        data.setApplyUserMonthIncome(
            StringValidater.toLong(req.getApplyUserMonthIncome()));
        data.setApplyUserSettleInterest(
            StringValidater.toLong(req.getApplyUserSettleInterest()));
        data.setApplyUserBalance(
            StringValidater.toLong(req.getApplyUserBalance()));
        data.setApplyUserJourShowIncome(req.getApplyUserJourShowIncome());

        data.setApplyUserIsPrint(req.getApplyUserIsPrint());
        data.setGhMonthIncome(StringValidater.toLong(req.getGhMonthIncome()));
        data.setGhSettleInterest(
            StringValidater.toLong(req.getGhSettleInterest()));
        data.setGhBalance(StringValidater.toLong(req.getGhBalance()));
        data.setGhJourShowIncome(req.getGhJourShowIncome());

        data.setGhIsPrint(req.getGhIsPrint());
        data.setGuarantor1MonthIncome(
            StringValidater.toLong(req.getGuarantor1MonthIncome()));
        data.setGuarantor1SettleInterest(
            StringValidater.toLong(req.getGuarantor1SettleInterest()));
        data.setGuarantor1Balance(
            StringValidater.toLong(req.getGuarantor1Balance()));
        data.setGuarantor1JourShowIncome(req.getGuarantor1JourShowIncome());

        data.setGuarantor1IsPrint(req.getGuarantor1IsPrint());
        data.setGuarantor2MonthIncome(
            StringValidater.toLong(req.getGuarantor2MonthIncome()));
        data.setGuarantor2SettleInterest(
            StringValidater.toLong(req.getGuarantor2SettleInterest()));
        data.setGuarantor2Balance(
            StringValidater.toLong(req.getGuarantor2Balance()));
        data.setGuarantor2JourShowIncome(req.getGuarantor2JourShowIncome());

        data.setGuarantor2IsPrint(req.getGuarantor2IsPrint());
        data.setIsHouseProperty(req.getIsHouseProperty());
        data.setHouseProperty(req.getHouseProperty());
        data.setIsHouseContract(req.getIsHouseContract());
        data.setHouseInvoice(req.getHouseInvoice());

        data.setIsLicense(req.getIsLicense());
        data.setLicense(req.getLicense());
        data.setIsSiteProve(req.getIsSiteProve());
        data.setSiteProve(req.getSiteProve());
        data.setSiteArea(req.getSiteArea());

        data.setCarType(req.getCarType());
        data.setIsDriceLicense(req.getIsDriceLicense());
        data.setDriceLicense(req.getDriceLicense());
        data.setOtherPropertyNote(req.getOtherPropertyNote());
        data.setApplyBirthAddress(req.getApplyBirthAddress());

        data.setApplyNowAddress(req.getApplyNowAddress());
        data.setHouseType(req.getHouseType());
        data.setGhBirthAddress(req.getGhBirthAddress());
        data.setGuarantor1BirthAddress(req.getGuarantor1BirthAddress());
        data.setGuarantor2BirthAddress(req.getGuarantor2BirthAddress());

        data.setOtherNote(req.getOtherNote());
        SYSConfig sysConfigoil = sysConfigBO
            .getSYSConfig(SysConstants.BUDGET_OIL_SUBSIDY_RATE);
        Double OilSubsidyBFB = StringValidater
            .toDouble(sysConfigoil.getCvalue());
        Long OilSubsidy = AmountUtil.mul(data.getLoanAmount(), OilSubsidyBFB);
        data.setOilSubsidy(OilSubsidy);// 油补
        data.setOilSubsidyKil(StringValidater.toDouble(req.getOilSubsidyKil()));
        data.setIsPlatInsure(req.getIsPlatInsure());
        SYSConfig sysConfig = sysConfigBO
            .getSYSConfig(SysConstants.BUDGET_GPS_DEDUCT_RATE);
        Double gpsBFB = StringValidater.toDouble((sysConfig.getCvalue()));
        Long gpsDeduct = AmountUtil.mul(data.getLoanAmount(), gpsBFB);
        data.setGpsDeduct(gpsDeduct);// GPS提成

        // 外单手动填写手续费
        data.setGpsFee(StringValidater.toLong(req.getGpsFee()));
        data.setGpsFeeWay(req.getGpsFeeWay());

        data.setLyAmount(StringValidater.toLong(req.getLyAmount()));
        data.setFxAmount(StringValidater.toLong(req.getFxAmount()));
        data.setOtherFee(StringValidater.toLong(req.getOtherFee()));
        data.setServiceChargeWay(req.getServiceChargeWay());

        Long serviceCharge = data.getLyAmount() + data.getFxAmount()
                + data.getGpsFee() + data.getOtherFee();
        // 收客户手续费合计：履约保证金+担保风险金+GPS收费+杂费
        data.setServiceCharge(serviceCharge);

        data.setMarryDivorce(req.getMarryDivorce());

        data.setApplyUserHkb(req.getApplyUserHkb());
        data.setBankBillPdf(req.getBankBillPdf());
        data.setSingleProvePdf(req.getSingleProvePdf());
        data.setIncomeProvePdf(req.getIncomeProvePdf());
        data.setLiveProvePdf(req.getLiveProvePdf());

        data.setBuildProvePdf(req.getBuildProvePdf());
        data.setHkbFirstPage(req.getHkbFirstPage());
        data.setHkbMainPage(req.getHkbMainPage());
        data.setGhHkb(req.getGhHkb());

        data.setGuarantor1Hkb(req.getGuarantor1Hkb());
        data.setGuarantor2Hkb(req.getGuarantor2Hkb());
        data.setHousePic(req.getHousePic());
        data.setHouseUnitPic(req.getHouseUnitPic());

        data.setHouseDoorPic(req.getHouseDoorPic());
        data.setHouseRoomPic(req.getHouseRoomPic());
        data.setHouseCustomerPic(req.getHouseCustomerPic());
        data.setHouseSaleCustomerPic(req.getHouseSaleCustomerPic());
        data.setCompanyNamePic(req.getCompanyNamePic());

        data.setCompanyPlacePic(req.getCompanyPlacePic());
        data.setCompanyWorkshopPic(req.getCompanyWorkshopPic());
        data.setCompanySaleCustomerPic(req.getCompanySaleCustomerPic());
        data.setSecondHgz(req.getSecondHgz());
        data.setSecondOdometer(req.getSecondOdometer());
        data.setSecondCarFrontPic(req.getSecondCarFrontPic());
        data.setSecondConsolePic(req.getSecondConsolePic());
        data.setSecond300Pdf(req.getSecond300Pdf());
        data.setSecondQxbPic(req.getSecondQxbPic());
        data.setSecondCarInPic(req.getSecondCarInPic());
        data.setSecondNumber(req.getSecondNumber());

        data.setOtherFilePdf(req.getOtherFilePdf());
        data.setOtherApplyNote(req.getOtherApplyNote());
        data.setApplyDatetime(new Date());

        String preNodeCode = data.getCurNodeCode();
        if (EButtonCode.SEND.getCode().equals(req.getDealType())) {
            EBudgetOrderNode node = EBudgetOrderNode.getMap().get(nodeFlowBO
                .getNodeFlowByCurrentNode(data.getCurNodeCode()).getNextNode());
            data.setCurNodeCode(node.getCode());

            // 日志记录
            sysBizLogBO.saveNewAndPreEndSYSBizLog(data.getCode(),
                EBizLogType.BUDGET_ORDER, data.getCode(), preNodeCode,
                node.getCode(), node.getValue(), req.getOperator());
        }

        // 协议外返点 和 应退按揭款
        List<XN632120ReqRepointDetail> repointDetailList = req
            .getRepointDetailList();
        for (XN632120ReqRepointDetail xn632120ReqRepointDetail : repointDetailList) {
            RepointDetail repointDetail = new RepointDetail();
            // 应退按揭款
            if (EUseMoneyPurpose.MORTGAGE.getCode()
                .equals(xn632120ReqRepointDetail.getUseMoneyPurpose())) {
                data.setShouldBackAmount(StringValidater
                    .toLong(xn632120ReqRepointDetail.getRepointAmount()));
                data.setShouldBackStatus(EBoolean.NO.getCode());
                repointDetail
                    .setUseMoneyPurpose(EUseMoneyPurpose.MORTGAGE.getCode());
                if (EIsAdvanceFund.YES.getCode()
                    .equals(data.getIsAdvanceFund())) {
                    repointDetail.setCompanyName(
                        xn632120ReqRepointDetail.getCarDealerName());
                } else {
                    repointDetail.setCompanyName(
                        xn632120ReqRepointDetail.getAccountName());
                }

            }
            // 协议外返点
            if (EUseMoneyPurpose.PROTOCOL_OUTER.getCode()
                .equals(xn632120ReqRepointDetail.getUseMoneyPurpose())) {
                repointDetail.setUseMoneyPurpose(
                    EUseMoneyPurpose.PROTOCOL_OUTER.getCode());
                repointDetail.setType(ERepointDetailType.NORMAL.getCode());

                repointDetail.setBudgetCode(data.getCode());
                CreditUser user = creditUserBO.getCreditUserByCreditCode(
                    data.getCreditCode(), ELoanRole.APPLY_USER);
                repointDetail.setUserName(data.getCustomerName());
                repointDetail.setIdNo(user.getIdNo());

                repointDetail.setCarDealerName(
                    xn632120ReqRepointDetail.getCarDealerName());
                repointDetail.setCarType(data.getCarType());
                repointDetail.setLoanAmount(data.getLoanAmount());
                repointDetail.setBankRate(data.getBankRate());
                Double benchmarkRate = benchmarkRate(data);
                repointDetail.setBenchmarkRate(benchmarkRate);

                repointDetail.setFee(fee);
                repointDetail.setCurNodeCode(
                    ERepointDetailStatus.TODO_MAKE_BILL.getCode());
            }
            repointDetail.setCompanyCode(data.getCompanyCode());
            repointDetail.setRepointAmount(StringValidater
                .toLong(xn632120ReqRepointDetail.getRepointAmount()));
            repointDetail.setAccountNo(xn632120ReqRepointDetail.getAccountNo());
            repointDetail
                .setOpenBankName(xn632120ReqRepointDetail.getOpenBankName());
            repointDetail
                .setAccountName(xn632120ReqRepointDetail.getAccountName());
            repointDetailBO.saveRepointDetail(repointDetail);
        }

        // 删除
        budgetOrderGpsBO.removeBudgetOrderGpsList(data.getCode());
        // 添加
        budgetOrderGpsBO.saveBudgetOrderGpsList(data.getCode(),
            req.getGpsList());

        budgetOrderBO.refresh(data);

    }

    private Double benchmarkRate(BudgetOrder data) {
        Bank bank = bankBO.getBankBySubbranch(data.getLoanBankCode());
        CarDealerProtocol protocol = carDealerProtocolBO
            .getCarDealerProtocolByCarDealerCode(data.getCarDealerCode(),
                bank.getBankCode());
        Double benchmarkRate = 0.0;

        if (ELoanPeriod.ONE_YEAER.getCode().equals(data.getLoanPeriods())) {
            if (ERateType.CT.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatCtRate12();
            }
            if (ERateType.ZK.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatZkRate12();
            }
        }
        if (ELoanPeriod.TWO_YEAR.getCode().equals(data.getLoanPeriods())) {

            if (ERateType.CT.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatCtRate24();
            }
            if (ERateType.ZK.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatZkRate24();
            }

        }
        if (ELoanPeriod.THREE_YEAR.getCode().equals(data.getLoanPeriods())) {
            if (ERateType.CT.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatCtRate36();
            }
            if (ERateType.ZK.getCode().equals(data.getRateType())) {
                benchmarkRate = protocol.getPlatZkRate36();
            }
        }
        return benchmarkRate;
    }

    @Override
    @Transactional
    public void collateAchieve(XN632193Req req) {
        List<String> list = req.getList();
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            if (!EBudgetOrderNode.LOCAL_COLLATEPOST_COLLATE.getCode()
                .equals(budgetOrder.getPledgeCurNodeCode())
                    && !EBudgetOrderNode.OUT_COLLATEPOST_COLLATE.getCode()
                        .equals(budgetOrder.getPledgeCurNodeCode())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点不是车辆抵押理件岗理件节点，不能操作");
            }
            if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点处于物流传递中，不能操作");
            }
        }
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            String preCurNodeCode = budgetOrder.getPledgeCurNodeCode();
            // budgetOrder.setPledgeCurNodeCode(nodeFlowBO
            // .getNodeFlowByCurrentNode(preCurNodeCode).getNextNode());
            EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
                .get(budgetOrder.getPledgeCurNodeCode());
            // budgetOrderBO.collateAchieve(budgetOrder);
            // 日志记录
            sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
                EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurNodeCode,
                currentNode.getCode(), currentNode.getValue(),
                req.getOperator());
            // 生成资料传递
            NodeFlow nodeFlow = nodeFlowBO
                .getNodeFlowByCurrentNode(budgetOrder.getPledgeCurNodeCode());
            logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
                budgetOrder.getCode(), budgetOrder.getSaleUserId(),
                budgetOrder.getPledgeCurNodeCode(), nodeFlow.getNextNode());
            // 产生物流单后改变状态为物流传递中
            budgetOrder.setIsLogistics(EBoolean.YES.getCode());
            budgetOrderBO.updateIsLogistics(budgetOrder);
        }
    }

    @Override
    @Transactional
    public void pledgeBegin(XN632194Req req) {

        List<String> list = req.getList();
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            if (!EBudgetOrderNode.OUT_PLEDGE_BEGIN.getCode()
                .equals(budgetOrder.getPledgeCurNodeCode())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点不是车辆抵押开始节点，不能操作");
            }
        }
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            String preCurNodeCode = budgetOrder.getPledgeCurNodeCode();
            budgetOrder.setPledgeCurNodeCode(nodeFlowBO
                .getNodeFlowByCurrentNode(preCurNodeCode).getNextNode());
            EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
                .get(budgetOrder.getPledgeCurNodeCode());
            budgetOrderBO.collateAchieve(budgetOrder);
            // 日志记录
            sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
                EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurNodeCode,
                currentNode.getCode(), currentNode.getValue(),
                req.getOperator());
        }
    }

    @Override
    @Transactional
    public void bankLoanCollateAchieve(XN632143Req req) {

        List<String> list = req.getList();
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            if (!EBudgetOrderNode.BANK_LOAN_COLLATEPOST_COLLATE.getCode()
                .equals(budgetOrder.getCurNodeCode())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点不是银行放款理件完成节点，不能操作");
            }
            if (EBoolean.YES.getCode().equals(budgetOrder.getIsLogistics())) {
                throw new BizException(EBizErrorCode.DEFAULT.getCode(),
                    "当前节点处于物流传递中，不能操作");
            }
        }
        for (String code : list) {
            BudgetOrder budgetOrder = budgetOrderBO.getBudgetOrder(code);
            String preCurNodeCode = budgetOrder.getCurNodeCode();
            // budgetOrder.setCurNodeCode(nodeFlowBO
            // .getNodeFlowByCurrentNode(preCurNodeCode).getNextNode());
            EBudgetOrderNode currentNode = EBudgetOrderNode.getMap()
                .get(budgetOrder.getCurNodeCode());
            budgetOrderBO.loanBankCollateAchieve(budgetOrder);

            // 生成资料传递
            NodeFlow nodeFlow = nodeFlowBO
                .getNodeFlowByCurrentNode(budgetOrder.getCurNodeCode());
            logisticsBO.saveLogistics(ELogisticsType.BUDGET.getCode(),
                budgetOrder.getCode(), budgetOrder.getSaleUserId(),
                budgetOrder.getCurNodeCode(), nodeFlow.getNextNode());
            // 产生物流单后改变状态为物流传递中
            budgetOrder.setIsLogistics(EBoolean.YES.getCode());
            budgetOrderBO.updateIsLogistics(budgetOrder);

            // 日志记录
            sysBizLogBO.saveNewAndPreEndSYSBizLog(budgetOrder.getCode(),
                EBizLogType.BUDGET_ORDER, budgetOrder.getCode(), preCurNodeCode,
                currentNode.getCode(), currentNode.getValue(),
                req.getOperator());
        }
    }

    private BudgetOrder calculateShouldBackMorgage(BudgetOrder data) {
        // 计算应退按揭款(垫资)
        Long carDealerSubsidy = 0L;
        if (null != data.getCarDealerSubsidy()) {
            carDealerSubsidy = data.getCarDealerSubsidy();// 厂家贴息
        }
        // 应退按揭款金额=贷款金额-收客户手续费（按揭款扣）-GPS收费（按揭款扣）-厂家贴息
        Long shouldBackAmount = data.getLoanAmount() - carDealerSubsidy;
        Long sxFee = 0L;// 收客户手续费
        sxFee = data.getLyAmount() + data.getFxAmount() + data.getOtherFee();
        if (EServiceChargeWay.MORTGAGE.getCode()
            .equals(data.getServiceChargeWay())) {
            shouldBackAmount = shouldBackAmount - sxFee;
        }
        if (EGpsFeeWay.MORTGAGE.getCode().equals(data.getGpsFeeWay())) {
            shouldBackAmount = shouldBackAmount - data.getGpsFee();
        }
        data.setShouldBackAmount(shouldBackAmount);
        return data;
    }

}
