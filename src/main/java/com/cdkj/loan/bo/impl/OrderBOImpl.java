/**
 * @Title OrderBOImpl.java 
 * @Package com.xnjr.mall.bo.impl 
 * @Description 
 * @author xieyj  
 * @date 2016年5月25日 上午8:15:46 
 * @version V1.0   
 */
package com.cdkj.loan.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cdkj.loan.bo.IExpressRuleBO;
import com.cdkj.loan.bo.IOrderBO;
import com.cdkj.loan.bo.base.PaginableBOImpl;
import com.cdkj.loan.common.AmountUtil;
import com.cdkj.loan.common.DateUtil;
import com.cdkj.loan.common.ProvinceUtil;
import com.cdkj.loan.core.OrderNoGenerater;
import com.cdkj.loan.dao.IOrderDAO;
import com.cdkj.loan.dao.IProductOrderDAO;
import com.cdkj.loan.domain.Cart;
import com.cdkj.loan.domain.CommitOrderPOJO;
import com.cdkj.loan.domain.Order;
import com.cdkj.loan.domain.ProductOrder;
import com.cdkj.loan.domain.ProductSpecs;
import com.cdkj.loan.dto.res.XN003020Res;
import com.cdkj.loan.enums.EBoolean;
import com.cdkj.loan.enums.EGeneratePrefix;
import com.cdkj.loan.enums.EOrderStatus;
import com.cdkj.loan.enums.EPayType;
import com.cdkj.loan.enums.ESystemCode;
import com.cdkj.loan.exception.BizException;

/** 
 * @author: xieyj 
 * @since: 2016年5月25日 上午8:15:46 
 * @history:
 */
@Component
public class OrderBOImpl extends PaginableBOImpl<Order> implements IOrderBO {

    protected static final Logger logger = LoggerFactory
        .getLogger(OrderBOImpl.class);

    @Autowired
    private IOrderDAO orderDAO;

    @Autowired
    private IProductOrderDAO productOrderDAO;

    @Autowired
    private IExpressRuleBO expressRuleBO;

    @Override
    public int userCancel(String code, String userId, String remark) {
        int count = 0;
        if (StringUtils.isNotBlank(code)) {
            Order data = new Order();
            data.setCode(code);
            data.setUpdater(userId);
            data.setUpdateDatetime(new Date());
            data.setRemark(remark);
            data.setStatus(EOrderStatus.YHYC.getCode());
            count = orderDAO.updateUserCancel(data);
        }
        return count;
    }

    @Override
    public int platCancel(String code, String updater, String remark,
            String status) {
        int count = 0;
        if (StringUtils.isNotBlank(code)) {
            Order data = new Order();
            data.setCode(code);
            data.setUpdater(updater);
            data.setUpdateDatetime(new Date());
            data.setRemark(remark);
            data.setStatus(status);
            count = orderDAO.updatePlatCancel(data);
        }
        return count;
    }

    @Override
    public List<Order> queryOrderList(Order condition) {
        return orderDAO.selectList(condition);
    }

    @Override
    public Order getOrder(String code) {
        Order data = null;
        if (StringUtils.isNotBlank(code)) {
            Order condition = new Order();
            condition.setCode(code);
            data = orderDAO.select(condition);
            if (data == null) {
                throw new BizException("xn0000", "订单编号不存在");
            }
            ProductOrder imCondition = new ProductOrder();
            imCondition.setOrderCode(code);
            List<ProductOrder> productOrderList = productOrderDAO
                .selectList(imCondition);
            data.setProductOrderList(productOrderList);
        }
        return data;
    }

    @Override
    public int refreshPayYESuccess(Order order, Long payAmount,
            String payType) {
        int count = 0;
        if (order != null && StringUtils.isNotBlank(order.getCode())) {
            Date now = new Date();
            order.setStatus(EOrderStatus.PAY_YES.getCode());
            order.setPayDatetime(now);
            order.setPayAmount(payAmount);
            order.setPayType(payType);
            order.setUpdater(order.getApplyUser());
            order.setUpdateDatetime(now);
            order.setRemark("订单已成功支付");
            count = orderDAO.updatePayYESuccess(order);
        }
        return count;
    }

    @Override
    public int refreshPaySuccess(Order order, Long payAmount, String payCode) {
        int count = 0;
        if (order != null && StringUtils.isNotBlank(order.getCode())) {
            Date now = new Date();
            order.setStatus(EOrderStatus.PAY_YES.getCode());
            order.setPayDatetime(now);
            order.setPayAmount(payAmount);
            order.setPayCode(payCode);
            order.setUpdater(order.getApplyUser());
            order.setUpdateDatetime(now);
            order.setRemark("订单已成功支付");
            count = orderDAO.updatePaySuccess(order);
        }
        return count;
    }

    @Override
    public int refreshRelationPaySuccess(Order order, Long payAmount,
            String payType, String payGroup, String payCode) {
        int count = 0;
        if (order != null && StringUtils.isNotBlank(order.getCode())) {
            Date now = new Date();
            order.setStatus(EOrderStatus.PAY_YES.getCode());
            order.setDkAmount(0L);
            order.setDkJfAmount(0L);
            order.setPayDatetime(now);
            order.setPayAmount(payAmount);
            order.setPayType(payType);
            order.setPayGroup(payGroup);
            order.setPayCode(payCode);
            order.setUpdater(order.getApplyUser());
            order.setUpdateDatetime(now);
            order.setRemark("订单已成功支付");
            count = orderDAO.updatePayRelationSuccess(order);
        }
        return count;
    }

    @Override
    public int promptToSend(Order data) {
        int count = 0;
        if (null != data) {
            count = orderDAO.updatePromptTimes(data);
        }
        return count;
    }

    @Override
    public int deliverLogistics(String code, String logisticsCompany,
            String logisticsCode, String deliverer, String deliveryDatetime,
            String pdf, String updater, String remark) {
        Order order = new Order();
        order.setCode(code);
        order.setStatus(EOrderStatus.SEND.getCode());
        order.setDeliverer(deliverer);
        order.setDeliveryDatetime(
            DateUtil.strToDate(deliveryDatetime, DateUtil.DATA_TIME_PATTERN_1));
        order.setLogisticsCompany(logisticsCompany);
        order.setLogisticsCode(logisticsCode);
        order.setPdf(pdf);
        order.setUpdater(updater);
        order.setUpdateDatetime(new Date());
        order.setRemark(remark);
        return orderDAO.updateDeliverLogistics(order);
    }

    @Override
    public int confirm(Order order, String signer, String remark) {
        int count = 0;
        if (order != null && StringUtils.isNotBlank(order.getCode())) {
            order.setStatus(EOrderStatus.RECEIVE.getCode());
            order.setSigner(signer);
            order.setSignDatetime(new Date());
            order.setRemark(remark);
            count = orderDAO.updateConfirm(order);
        }
        return count;
    }

    @Override
    public int comment(Order order) {
        int count = 0;
        if (order != null && StringUtils.isNotBlank(order.getCode())) {
            order.setStatus(EOrderStatus.COMMENT.getCode());
            order.setRemark(EOrderStatus.COMMENT.getValue());
            count = orderDAO.updateComment(order);
        }
        return count;
    }

    @Override
    public String saveOrder(List<Cart> cartList, CommitOrderPOJO pojo) {
        // 生成订单基本信息
        Order order = new Order();
        String code = OrderNoGenerater
            .generate(EGeneratePrefix.ORDER.getCode());
        order.setCode(code);
        order.setReceiver(pojo.getReceiver());
        order.setReMobile(pojo.getReMobile());
        order.setReAddress(pojo.getReAddress());
        order.setApplyUser(pojo.getApplyUser());

        order.setApplyNote(pojo.getApplyNote());
        order.setApplyDatetime(new Date());
        order.setStatus(EOrderStatus.TO_PAY.getCode());

        order.setUpdater(pojo.getApplyUser());
        order.setUpdateDatetime(new Date());
        order.setRemark("订单新提交，待支付");
        // 计算订单金额
        Long amount = 0L;
        Double weight = 0.0;
        for (Cart cart : cartList) {
            ProductSpecs productSpecs = cart.getProductSpecs();
            if (null != productSpecs.getPrice()) {
                amount = amount
                        + (cart.getQuantity() * productSpecs.getPrice());
            }
            // 落地订单产品关联信息
            saveProductOrder(order.getCode(), productSpecs, cart.getQuantity());
            weight = weight + AmountUtil.mulAB(productSpecs.getWeight(),
                cart.getQuantity());
        }
        // 计算订单运费
        Long yunfei = 0L;
        if (!EBoolean.NO.getCode().equals(pojo.getIsNeedYunfei())) {
            // 运费设置
            String addRessProvince = ProvinceUtil
                .getProvince(pojo.getReAddress());
            XN003020Res expressRule = expressRuleBO.getPrice("浙江省",
                addRessProvince, weight, ESystemCode.HTWT.getCode(),
                ESystemCode.HTWT.getCode());// 运费人民币
            yunfei = expressRule.getExpressFee();
        }
        // 计算订单金额
        order.setAmount(amount);
        order.setYunfei(yunfei);
        order.setPayAmount(0L);
        // 落地订单
        orderDAO.insert(order);
        return code;
    }

    public int refreshYunfei(Order order, Long yunfei) {
        int count = 0;
        if (order.getYunfei().longValue() != yunfei.longValue()) {
            order.setYunfei(yunfei);
            count = orderDAO.updateYunfei(order);
        }
        return count;
    }

    private void saveProductOrder(String orderCode, ProductSpecs productSpecs,
            Integer quantity) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(
            OrderNoGenerater.generate(EGeneratePrefix.PRODUCT_ORDER.getCode()));
        productOrder.setOrderCode(orderCode);
        productOrder.setProductCode(productSpecs.getProductCode());
        productOrder.setProductSpecsCode(productSpecs.getCode());
        productOrder.setProductSpecsName(productSpecs.getName());
        productOrder.setQuantity(quantity);
        productOrder.setPrice(productSpecs.getPrice());
        productOrderDAO.insert(productOrder);
    }

    @Override
    public String addPayGroup(String code, EPayType payType) {
        String payGroup = null;
        if (StringUtils.isNotBlank(code)) {
            Order data = new Order();
            data.setCode(code);
            payGroup = OrderNoGenerater
                .generate(EGeneratePrefix.PAY_GROUP.getCode());
            data.setPayGroup(payGroup);
            data.setPayType(payType.getCode());
            orderDAO.updatePayGroup(data);
        }
        return payGroup;
    }

    @Override
    public String addPayGroup(String code, EPayType payType, Long dkCnyAmount,
            Long dkJfAmount) {
        String payGroup = null;
        if (StringUtils.isNotBlank(code)) {
            Order data = new Order();
            data.setCode(code);
            payGroup = OrderNoGenerater
                .generate(EGeneratePrefix.PAY_GROUP.getCode());
            data.setPayGroup(payGroup);
            data.setPayType(payType.getCode());
            data.setDkAmount(dkCnyAmount);
            data.setDkJfAmount(dkJfAmount);
            orderDAO.updatePayGroup(data);
        }
        return payGroup;
    }

    @Override
    public List<Order> queryOrderListByPayGroup(String payGroup) {
        Order condition = new Order();
        condition.setPayGroup(payGroup);
        return orderDAO.selectList(condition);
    }

    @Override
    public Long selectXFAmount(String userId) {
        return orderDAO.selectXFAmount(userId);
    }

    @Override
    public void removeOrder(String code) {
        Order data = new Order();
        data.setCode(code);
        data.setStatus(EOrderStatus.RECYCLE.getCode());
        data.setRemark("前端用户删除订单");
        orderDAO.updateRemove(data);
    }
}
