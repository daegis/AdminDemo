package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;


/**
 * ProcessorResponseId Entity.
 */
@Data
public class ProcessorResponseId implements Serializable{
	
	//列信息
	private Long id;
	
	private Short requestType;
	
	private String responseId;
	
	private Long userId;
	
	private java.util.Date createTime;
	
	private java.util.Date lastModifiedTime;
	
	private Short status;

}

