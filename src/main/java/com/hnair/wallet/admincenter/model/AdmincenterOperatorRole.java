package com.hnair.wallet.admincenter.model;

import lombok.Data;

import java.io.Serializable;


/**
 * AdmincenterOperatorRole Entity.
 */
@Data
public class AdmincenterOperatorRole implements Serializable {

    //列信息
    private Integer id;

    private Integer operatorId;

    private Integer roleId;



}

