package com.cdkj.loan.api.impl;

import com.cdkj.loan.ao.ICarDealerAO;
import com.cdkj.loan.api.AProcessor;
import com.cdkj.loan.common.JsonUtil;
import com.cdkj.loan.core.ObjValidater;
import com.cdkj.loan.dto.req.XN632064Req;
import com.cdkj.loan.dto.res.BooleanRes;
import com.cdkj.loan.exception.BizException;
import com.cdkj.loan.exception.ParaException;
import com.cdkj.loan.spring.SpringContextHolder;

/**
 * 汽车经销商协议上架
 * @author: jiafr 
 * @since: 2018年7月26日 下午9:38:36 
 * @history:
 */
public class XN632064 extends AProcessor {
    private ICarDealerAO carDealerAO = SpringContextHolder
        .getBean(ICarDealerAO.class);

    private XN632064Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        carDealerAO.carDealerProtocolUp(req);
        return new BooleanRes(true);
    }

    @Override
    public void doCheck(String inputparams, String operator)
            throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN632064Req.class);
        ObjValidater.validateReq(req);
    }
}
