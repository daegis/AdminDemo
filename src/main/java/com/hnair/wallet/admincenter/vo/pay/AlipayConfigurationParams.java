package com.hnair.wallet.admincenter.vo.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/23/2018 9:04 PM
 */
@Data
public class AlipayConfigurationParams implements Serializable {
    private String appid;
    private String privateKey;
    private String publicKey;
    private String signType;
}
