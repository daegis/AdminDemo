package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * OrderRechargeInfo Entity.
 */
@Data
public class OrderRechargeInfo implements Serializable{
	
	//列信息
	private Integer id;
	
	private String tradeNo;
	
	private String channelOrderNo;
	
	private Long userId;
	
	private String merchantId;
	
	private LocalDateTime orderTime;
	
	private Integer orderStatus;
	
	private BigDecimal rechargeBalance;
	
	private LocalDateTime lastModifiedTime;
	
	private LocalDateTime payTime;
	
	private Integer thirdTradeType;
	
	private String thirdTradeNo;

}

