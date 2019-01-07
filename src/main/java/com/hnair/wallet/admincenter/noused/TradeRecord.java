package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * TradeRecord Entity.
 */
@Data
public class TradeRecord implements Serializable{
	
	//列信息
	private Integer id;
	
	private Long userId;
	
	private String tradeNo;
	
	private String channelOrderNo;
	
	private Integer accountType;
	
	private LocalDateTime tradeTime;
	
	private Integer tradeType;
	
	private BigDecimal tradeBalance;
	
	private Integer thirdTradeType;
	
	private String thirdTradeNo;
	
	private String tradeRemark;
	
	private BigDecimal tradeCompleteBalance;
	
	private String operator;
	
	private String operateId;
	
	private LocalDateTime operateTime;
	
	private LocalDateTime lastModifiedTime;

		
}

