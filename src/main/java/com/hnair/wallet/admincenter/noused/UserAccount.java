package com.hnair.wallet.admincenter.noused;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * UserAccount Entity.
 */
@Data
public class UserAccount implements Serializable {

    //列信息
    private Integer id;

    private Long userId;

    private Integer accountType;

    private String accountTypeDesc;

    private Integer accountStatus;

    private BigDecimal accountBalance;

    private LocalDateTime lastModifiedTime;

}

