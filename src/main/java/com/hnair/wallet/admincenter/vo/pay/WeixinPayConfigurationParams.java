package com.hnair.wallet.admincenter.vo.pay;

import lombok.Data;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/24/2018 4:20 PM
 * WXPAY_APP_ID(300,"微信支付appid"),
 * WXPAY_MCH_ID(301,"微信支付mchid"),
 * WXPAY_PRIVATE_KEY(302,"微信支付私钥");
 */
@Data
public class WeixinPayConfigurationParams {
    private String wxpayAppid;
    private String wxpayMchid;
    private String wxpayPrivateKey;
}
