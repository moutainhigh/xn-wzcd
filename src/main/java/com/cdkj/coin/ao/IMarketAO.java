package com.cdkj.coin.ao;

import com.cdkj.coin.api.impl.XN625291;
import com.cdkj.coin.domain.Market;
import com.cdkj.coin.dto.req.XN625291Req;
import sun.jvm.hotspot.oops.Mark;

import java.util.List;

/**
 * Created by tianlei on 2017/十一月/13.
 */
public interface IMarketAO {

    Market marketByCoinType(String coinType);

    List<Market> marketListByReq(XN625291Req req);

}
