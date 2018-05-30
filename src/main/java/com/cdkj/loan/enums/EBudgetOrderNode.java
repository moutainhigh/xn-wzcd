package com.cdkj.loan.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 预算单节点
 * @author: CYL 
 * @since: 2018年5月28日 下午8:31:51 
 * @history:
 */
public enum EBudgetOrderNode {
    WRITE_BUDGET_ORDER("002_01", "填写准入申请单"), RISK_APPROVE("002_02",
            "风控专员审核"), RISK_CHARGE_APPROVE("002_03", "风控主管审核"), AGAIN_WRITE(
                    "002_04",
                    "重新填写准入申请单"), INTERVIEW("002_05", "面签"), BIZ_CHARGE_APPROVE(
                            "002_06", "业务总监审核"), ADVANCEFUND("002_07",
                                    "财务垫资"), AGAIN_INTERVIEW("002_08", "重新面签");

    public static Map<String, EBudgetOrderNode> getMap() {
        Map<String, EBudgetOrderNode> map = new HashMap<String, EBudgetOrderNode>();
        for (EBudgetOrderNode node : EBudgetOrderNode.values()) {
            map.put(node.getCode(), node);
        }
        return map;
    }

    EBudgetOrderNode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}