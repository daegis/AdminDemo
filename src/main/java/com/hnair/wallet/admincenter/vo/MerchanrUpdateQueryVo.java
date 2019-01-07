package com.hnair.wallet.admincenter.vo;

import lombok.Data;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/24/2018 3:54 PM
 */
@Data
public class MerchanrUpdateQueryVo {
    private Integer id;
    private String merchantId;
    private String ipWhiteList;
    private String merchantTradeToken;
    private String aliAppid;
    private String aliSignType;
    private String aliPublicKey;
    private String aliPrivateKey;
    private String wxpayAppid;
    private String wxpayMchid;
    private String wxpayPrivateKey;
}
