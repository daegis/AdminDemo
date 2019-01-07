package com.hnair.wallet.admincenter.noused;

import java.io.Serializable;


/**
 * UserOperateRecord Entity.
 */
public class UserOperateRecord implements Serializable{
	
	//列信息
	private Long id;
	
	private Short operateType;
	
	private String oldVal;
	
	private String newVal;
	
	private Short operateUserType;
	
	private Long operateUserId;
	
	private java.util.Date operateTime;
	

		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
		
	public void setOperateType(Short value) {
		this.operateType = value;
	}
	
	public Short getOperateType() {
		return this.operateType;
	}
		
		
	public void setOldVal(String value) {
		this.oldVal = value;
	}
	
	public String getOldVal() {
		return this.oldVal;
	}
		
		
	public void setNewVal(String value) {
		this.newVal = value;
	}
	
	public String getNewVal() {
		return this.newVal;
	}
		
		
	public void setOperateUserType(Short value) {
		this.operateUserType = value;
	}
	
	public Short getOperateUserType() {
		return this.operateUserType;
	}
		
		
	public void setOperateUserId(Long value) {
		this.operateUserId = value;
	}
	
	public Long getOperateUserId() {
		return this.operateUserId;
	}
		
		
	public void setOperateTime(java.util.Date value) {
		this.operateTime = value;
	}
	
	public java.util.Date getOperateTime() {
		return this.operateTime;
	}
		
}

