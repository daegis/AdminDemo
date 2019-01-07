package com.hnair.wallet.admincenter.vo;

import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.vo.pay.AlipayConfigurationParams;
import com.hnair.wallet.admincenter.vo.pay.WeixinPayConfigurationParams;
import lombok.Data;

import java.io.Serializable;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/23/2018 8:57 PM
 */
@Data
public class MerchantDetailVo implements Serializable {
    private Merchant merchant;
    private String MerchantTradeToken;
    private AlipayConfigurationParams aliParams;
    private WeixinPayConfigurationParams wxParams;
}
