package com.cdkj.loan.bo;

import java.util.List;

import com.cdkj.loan.bo.base.IPaginableBO;
import com.cdkj.loan.domain.ChannelBank;

/**
 * @author: xieyj 
 * @since: 2016年11月10日 下午8:30:39 
 * @history:
 */
public interface IChannelBankBO extends IPaginableBO<ChannelBank> {

    public boolean isChannelBankExist(Long id);

    public int saveChannelBank(ChannelBank data);

    public int removeChannelBank(Long id);

    public int refreshChannelBank(ChannelBank data);

    public List<ChannelBank> queryChannelBankList(ChannelBank condition);

    public ChannelBank getChannelBank(Long id);

    public ChannelBank getChannelBank(String bankCode);

}
