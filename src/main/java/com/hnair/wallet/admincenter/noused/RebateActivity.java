package com.hnair.wallet.admincenter.noused;

import java.io.Serializable;

import lombok.Data;


/**
 * RebateActivity Entity.
 */
@Data
public class RebateActivity implements Serializable{
	
	private static final long serialVersionUID = -7849161879809466331L;

	//列信息
	private Integer id;
	
	private String activityName;
	
	private java.util.Date activityStartTime;
	
	private java.util.Date activityEndTime;
	
	private Short status;
	
	private Double thresholdAmount;
	
	private Integer rebateRatio;
	
	private java.util.Date createTime;
	
	private java.util.Date lastModifyTime;
	

		
	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
		
		
	public void setActivityName(String value) {
		this.activityName = value;
	}
	
	public String getActivityName() {
		return this.activityName;
	}
		
		
	public void setActivityStartTime(java.util.Date value) {
		this.activityStartTime = value;
	}
	
	public java.util.Date getActivityStartTime() {
		return this.activityStartTime;
	}
		
		
	public void setActivityEndTime(java.util.Date value) {
		this.activityEndTime = value;
	}
	
	public java.util.Date getActivityEndTime() {
		return this.activityEndTime;
	}
		
		
	public void setStatus(Short value) {
		this.status = value;
	}
	
	public Short getStatus() {
		return this.status;
	}
		
		
	public void setThresholdAmount(Double value) {
		this.thresholdAmount = value;
	}
	
	public Double getThresholdAmount() {
		return this.thresholdAmount;
	}
		
		
	public void setRebateRatio(Integer value) {
		this.rebateRatio = value;
	}
	
	public Integer getRebateRatio() {
		return this.rebateRatio;
	}
		
		
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
		
		
	public void setLastModifyTime(java.util.Date value) {
		this.lastModifyTime = value;
	}
	
	public java.util.Date getLastModifyTime() {
		return this.lastModifyTime;
	}
		
}

