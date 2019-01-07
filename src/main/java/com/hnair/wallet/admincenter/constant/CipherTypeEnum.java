package com.hnair.wallet.admincenter.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 密钥配置类型
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CipherTypeEnum {

    MERCHANT_TRADE_TOKEN(3,"商户交易签名token"),

    ALIPAY_APP_ID(200,"支付宝appid"),
    ALIPAY_PRIVATE_KEY(201,"支付宝私钥"),
    ALIPAY_PUBLIC_KEY(202,"支付宝公钥"),
    ALIPAY_SIGN_TYPE(203,"支付宝签名类型"),

    WXPAY_APP_ID(300,"微信支付appid"),
    WXPAY_MCH_ID(301,"微信支付mchid"),
    WXPAY_PRIVATE_KEY(302,"微信支付私钥");

    @Getter
    private final  Integer type;

    @Getter
    private final  String name;

    private final static Map<Integer, CipherTypeEnum> TYPE_MAP = new LinkedHashMap<>();

    static {
        for (CipherTypeEnum type : CipherTypeEnum.values()) {
            TYPE_MAP.put(type.getType(), type);
        }
    }

    public static Map<Integer, CipherTypeEnum> getChiperTypes() {
        return TYPE_MAP;
    }

    public static CipherTypeEnum of(Integer type) {
        if (type == null) {
            return null;
        }
        return TYPE_MAP.get(type);
    }


    public static CipherTypeEnum getChiperType(Integer chiperType) {
        if(chiperType==null){
            return null;
        }
        for (CipherTypeEnum type : CipherTypeEnum.values()) {
            if (type.getType().equals(chiperType)) {
                return type;
            }
        }
        return null;
    }

}
