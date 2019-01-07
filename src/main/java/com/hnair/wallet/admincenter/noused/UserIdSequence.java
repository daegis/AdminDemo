package com.hnair.wallet.admincenter.noused;

import java.io.Serializable;


/**
 * UserIdSequence Entity.
 */
public class UserIdSequence implements Serializable{
	
	//列信息
	private Long id;
	
	private Integer val;
	

		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
		
	public void setVal(Integer value) {
		this.val = value;
	}
	
	public Integer getVal() {
		return this.val;
	}
		
}

