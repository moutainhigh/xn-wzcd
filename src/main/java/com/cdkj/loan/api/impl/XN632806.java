package com.cdkj.loan.api.impl;

import com.cdkj.loan.ao.IArchiveAO;
import com.cdkj.loan.api.AProcessor;
import com.cdkj.loan.common.JsonUtil;
import com.cdkj.loan.dto.req.XN632806Req;
import com.cdkj.loan.exception.BizException;
import com.cdkj.loan.exception.ParaException;
import com.cdkj.loan.spring.SpringContextHolder;

/**
 * 详情查询人事档案
 * @author: jiafr 
 * @since: 2018年6月4日 下午9:29:18 
 * @history:
 */
public class XN632806 extends AProcessor {
    private IArchiveAO archiveAO = SpringContextHolder
        .getBean(IArchiveAO.class);

    private XN632806Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        return archiveAO.getArchive(req.getCode());
    }

    @Override
    public void doCheck(String inputparams, String operator)
            throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN632806Req.class);
    }
}
