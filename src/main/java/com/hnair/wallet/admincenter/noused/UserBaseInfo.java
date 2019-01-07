package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * UserBaseInfo Entity.
 */
@Data
public class UserBaseInfo implements Serializable{
	
	//列信息
	private Integer id;
	
	private Long userId;
	
	private String password;
	
	private String gender;
	
	private String birthday;
	
	private String email;
	
	private String region;
	
	private String thirdUserId;
	
	private String merchantId;
	
	private Integer registerChannelType;
	
	private String registerChannelTypeDes;
	
	private String realCertificationName;
	
	private String realCertificationType;
	
	private String realCertificationNum;
	
	private String realCertificationPhone;
	
	private LocalDateTime realCertificationDate;
	
	private Integer realCertificationStatus;

}

