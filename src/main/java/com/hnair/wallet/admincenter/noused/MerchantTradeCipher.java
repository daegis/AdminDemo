package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;


/**
 * MerchantTradeCipher Entity.
 */
@Data
public class MerchantTradeCipher implements Serializable {

    //列信息
    private Integer id;

    private String merchantId;

    private Integer tradeType;

    private String tradeToken;

}

