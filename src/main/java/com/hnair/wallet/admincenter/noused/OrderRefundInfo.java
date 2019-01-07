package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * OrderRefundInfo Entity.
 */
@Data
public class OrderRefundInfo implements Serializable{
	
	//列信息
	private Integer id;
	
	private String tradeNo;
	
	private String merchantId;
	
	private String channelOrderNo;
	
	private String channelRefundNo;
	
	private Integer accountType;
	
	private Long userId;
	
	private LocalDateTime refundTime;
	
	private Integer refundStatus;
	
	private BigDecimal refundBalance;
	
	private String refundReason;
	
	private LocalDateTime lastModifiedTime;

}

