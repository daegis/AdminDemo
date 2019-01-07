package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;


/**
 * HipayJob Entity.
 */
@Data
public class HipayJob implements Serializable{
	
	//列信息
	private Long jobId;
	
	private String jobName;
	
	private String jobClass;
	
	private Short jobType;
	
	private String cron;
	
	private String description;
	
	private Integer shardingTotalCount;
	
	private String shardingItemParameters;
	
	private String jobParameter;
	
	private String strategyClass;
	
	private Boolean streamingProcess;
	
	private Boolean failover;
	
	private Boolean misfire;
	
	private String monitorPort;
	
	private Integer reconcileIntervalMinutes;
	
	private Short status;
	
	private java.util.Date createTime;
	
	private java.util.Date updateTime;
	
}

