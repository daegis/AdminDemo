package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;


/**
 * Merchant Entity.
 */
@Data
public class Merchant implements Serializable {

    //列信息
    private Integer id;

    private String merchantId;

    private String merchantName;

    private String ipWhiteList;


}

