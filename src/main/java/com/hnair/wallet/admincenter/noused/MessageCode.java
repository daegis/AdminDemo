package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * MessageCode Entity.
 */
@Data
public class MessageCode implements Serializable{
	
	//列信息
	private Long id;
	
	private Long userId;
	
	private String region;
	
	private String mobile;
	
	private String identifyCode;
	
	private Short status;
	
	private LocalDateTime createTime;
	
	private LocalDateTime lastModifyTime;
	
	private Short msgCodeType;
	
	private String refTradeNo;

		
}

