package com.hnair.wallet.admincenter.model;

import lombok.Data;

import java.io.Serializable;


/**
 * AdmincenterRole Entity.
 */
@Data
public class AdmincenterRole implements Serializable {

    //列信息
    private Integer id;

    private String roleName;

    private String description;

}

