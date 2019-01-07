package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * OrderConsumeInfo Entity.
 */
@Data
public class OrderConsumeInfo implements Serializable{
	
	//列信息
	private Integer id;
	
	private String tradeNo;
	
	private Long userId;
	
	private String merchantId;
	
	private Integer accountType;
	
	private String tradeRemark;
	
	private LocalDateTime orderTime;
	
	private LocalDateTime payTime;
	
	private Integer orderStatus;
	
	private BigDecimal consumeBalance;
	
	private BigDecimal refundableBalance;
	
	private String channelOrderNo;
	
	private String channelTradeToken;
	
	private LocalDateTime lastModifiedTime;

}

